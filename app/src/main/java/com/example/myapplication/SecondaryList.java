package com.example.myapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import info.sqlite.helper.DBHelper;
import info.sqlite.model.List_Item;
import info.sqlite.model.ShoppingList;
import recyclerView.ItemListAdapter;

import java.util.ArrayList;
import java.util.List;

public class SecondaryList extends AppCompatActivity {

    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private ItemListAdapter mAdapter;
    private List<List_Item> listOfItems;
    private TextView listTitle;
    private int list_ID = 0;
    TextView listID;

    /**
     * Sets up everything at the creation of this activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary_list);
        /**Using intents, retrieving the ID of the this list.*/
        Intent intent = getIntent();
        /**retrieving the name of the list using intents*/
        String newTitle = intent.getStringExtra("List_Name");
        list_ID = intent.getIntExtra("List_ID",0);
        /**Setting the title page to the one retrieved*/
        setTitle(newTitle);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            listTitle = (TextView) findViewById(R.id.textView);
            listTitle.setText(newTitle);
        }
        /**Setting up the recyclerview*/
        listOfItems = new ArrayList<List_Item>();
        recyclerView = (RecyclerView) findViewById(R.id.itemListView);
        mAdapter = new ItemListAdapter(this, listOfItems);

        /** Creating a layout manager and setting it for the recycler view*/
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        /**Assigning the animaotor for the recyclerview*/
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        /**Creating divides between items in the recycler view*/
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        /**Setting the adapter for the recycler view*/
        recyclerView.setAdapter(mAdapter);

        /**Displaying the list ID*/
        listID = findViewById(R.id.listIDText);
        listID.setText("List ID:" + Integer.toString(list_ID));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**
         * Setting up a listener used to swipe and delete lists.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            /**
             * perform database deletion and deletion in the recyclerview
             * @param viewHolder
             * @param i
             */
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                deleteData((List_Item) viewHolder.itemView.getTag(), list_ID);
                listOfItems.remove(viewHolder.itemView.getTag());
                mAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView);


        /**Creates a floating action button*/
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        /**Setting up a listener for the floating Action Button*/
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**Pass the list_ID to the AddItemToList activity upon switch so the list_ID knows which list to add to*/
                Intent intent = new Intent(SecondaryList.this, AddItemToList.class);
                intent.putExtra("List_ID", list_ID);
                /**Starts a new activity AddList, which is used to create new lists*/
                startActivity(intent);
                /**Animations used to transition fom activity to activity*/
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        prepareData();
    }
    /**
     * Refreshing the list upon re-entering this activity
     */
    @Override
    protected void onResume(){
        super.onResume();
        listOfItems.clear();
        mAdapter.notifyDataSetChanged();
        prepareData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * Method used to insert data from the database into the recyclerview
     */
    private void prepareData(){
        dbHelper = new DBHelper(this);
        List<List_Item> shoppingList = dbHelper.getAllItemsfromList(list_ID);
        listOfItems.addAll(shoppingList);
    }

    /**
     * Method used to delete an item from a list upon swiping the item in the recyclerview
     * @param item
     * @param list_ID
     */
    private void deleteData(List_Item item, int list_ID){
        String deletedItem = item.getItemName();
        dbHelper = new DBHelper(this);
        dbHelper.deleteItem(item);
        Toast.makeText(getApplicationContext(), deletedItem + " was successfully deleted", Toast.LENGTH_LONG).show();
    }
}
