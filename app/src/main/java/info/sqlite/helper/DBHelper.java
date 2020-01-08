package info.sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import info.sqlite.model.List_Item;
import info.sqlite.model.ShoppingList;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String LOG = "DatabaseHelper";

    public static final String DATABASE_NAME = "ShoppingList";
    public static final int DATABASE_VERSION = 1;

    /**
     * Names for the table and columns used to create the Master list table
     */
    public static final String TABLE_LIST = "masterList";
    public static final String COLUMN_LIST_ID = "ListID";
    public static final String COLUMN_LIST_NAME = "subListName";

    /**
     * Names for the table and columns used to create the Sub-list table
     */
    public static final String TABLE_LIST_ITEM = "subList";
    public static final String COLUMN_ITEMLIST_ID = "itemListID";
    public static final String COLUMN_LIST_ITEM_ID = "itemID";
    public static final String COLUMN_LIST_ITEM_NAME = "itemName";

    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    /**
     * Creating the tables using the parameters above
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE "+
                TABLE_LIST + " (" +
                COLUMN_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LIST_NAME + " VARCHAR(32)" +
                ");";
        final String SQL_CREATE_TABLE2 = "CREATE TABLE "+
                TABLE_LIST_ITEM + " (" +
                COLUMN_LIST_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEMLIST_ID + " INTEGER, " +
                COLUMN_LIST_ITEM_NAME + " VARCHAR(32), " +
                "FOREIGN KEY(" + COLUMN_ITEMLIST_ID + ") REFERENCES " + TABLE_LIST + "(" + COLUMN_LIST_ID + ")" +
                ");";
        db.execSQL(SQL_CREATE_TABLE2);
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS masterList");
        db.execSQL("DROP TABLE IF EXISTS subList");
        onCreate(db);
    }

    /**
     * Method used to get all lists for the masterlist.
     * @return
     */
    public List<ShoppingList> getAllLists(){
        /**creating an undefined ShoppingList object*/
        ShoppingList shoppingListItem = null;
        /**Creating an arraylist that stores all ShoppingList objects*/
        List<ShoppingList> listofShoppingLists = new ArrayList<ShoppingList>();
        /**Starting the connection to the database*/
        SQLiteDatabase db = this.getWritableDatabase();
        /**Making the query to retrieve all Shopping List*/
        Cursor cursor = db.rawQuery("select * from " + TABLE_LIST  + ";",null);
        /**For each row retrieved from the query, create a new ShoppingList item and add it to the arraylist*/
        while(cursor.moveToNext()){
            shoppingListItem = new ShoppingList(cursor.getInt(cursor.getColumnIndexOrThrow("ListID")),
                    cursor.getString(cursor.getColumnIndexOrThrow("subListName")));
            listofShoppingLists.add(shoppingListItem);
        }
        /**Close connection*/
        db.close();
        /**return the array list*/
        return listofShoppingLists;
    }

    /**
     * Method used to return all items for a list
     * @param list_ID
     * @return
     */
    public List<List_Item> getAllItemsfromList(int list_ID){
        /**Creating an undefind List_Item object*/
        List_Item item = null;
        /**Creating an arrayList that stores all List_Item objects*/
        List<List_Item> listOfItems = new ArrayList<List_Item>();
        /**Starting the connection to the database*/
        SQLiteDatabase db = this.getWritableDatabase();
        /**Creating the arguments for this query, which is the ID of the list.*/
        String[] whereArgs = {Integer.toString(list_ID)};
        /**Making the query to retrieve all items from the databasee*/
        Cursor cursor = db.query(TABLE_LIST_ITEM, null, COLUMN_ITEMLIST_ID + " = ?", whereArgs, null,null,null);
        /**For each row retrieved from the query, create a new List_Item object and add it to the arrayList*/
        while(cursor.moveToNext()){
            item = new List_Item(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ITEMLIST_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LIST_ITEM_ID)), cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LIST_ITEM_NAME)));
            listOfItems.add(item);
        }
        /**Close the connection to the database*/
        db.close();
        /**return the arrayList*/
        return listOfItems;
    }

    /**
     * Method used to create a new Shopping List
     * @param shoppingList
     */
    public void createList(ShoppingList shoppingList){
        /**Start connection to the database*/
        SQLiteDatabase db = this.getWritableDatabase();
        /**Creating a new ContentValue object*/
        ContentValues values = new ContentValues();
        /**Insert values into table*/
        values.put(COLUMN_LIST_NAME, shoppingList.getListName());
        /**Perform the insert query*/
        db.insert(TABLE_LIST, null, values);
        /**Close the connection to the database*/
        db.close();
    }

    public void addItemToList(List_Item listItem, int currentList){
        /**Start connection to the database*/
        SQLiteDatabase db = this.getWritableDatabase();
        /**Creating a new ContentValue object*/
        ContentValues values = new ContentValues();
        /**Insert values into table*/
        values.put(COLUMN_ITEMLIST_ID, currentList);
        values.put(COLUMN_LIST_ITEM_NAME, listItem.getItemName());
        /**Perform the insert query*/
        db.insert(TABLE_LIST_ITEM, null, values);
        /**Close the connection to the database*/
        db.close();
    }

    /**
     * Method used to delete a list
     * @param shoppingList
     */
    public void deleteList(ShoppingList shoppingList){
        /**Start connection to the database*/
        SQLiteDatabase db = this.getWritableDatabase();
        /**Creating the arguments for the query, which is the ID of the list to be deleted*/
        String whereClause = String.valueOf(shoppingList.getListID());
        /**Converting the arguments into an array*/
        String[] whereArgs = {whereClause};
        /**Perform a query to delete the list and delete items associated with the list*/
        db.delete(TABLE_LIST,COLUMN_LIST_ID + " = ?", whereArgs);
        db.delete(TABLE_LIST_ITEM, COLUMN_ITEMLIST_ID + " = ? ", whereArgs);
        /**Close the connection to the database*/
        db.close();
    }

    /**
     * Delete an Item from a list
     * @param listItem
     */
    public void deleteItem(List_Item listItem){
        /**Start connection to the database*/
        SQLiteDatabase db = this.getWritableDatabase();
        /**Creating the arguments for the query, which is the ID of the item to be deleted*/
        String whereClause = String.valueOf(listItem.getItem_ID());
        /**Converting the arguments into an array*/
        String[] whereArgs = {whereClause};
        /**Perform the query to delete the item*/
        db.delete(TABLE_LIST_ITEM, COLUMN_LIST_ITEM_ID + " = ?", whereArgs);
        /**Close the connection to the database*/
        db.close();
    }
}
