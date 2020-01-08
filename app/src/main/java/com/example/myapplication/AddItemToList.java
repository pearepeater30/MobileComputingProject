package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import info.sqlite.helper.DBHelper;
import info.sqlite.model.List_Item;
import info.sqlite.model.ShoppingList;

public class AddItemToList extends AppCompatActivity {

    private EditText addText;
    private DBHelper dbHelper;
    private Button addItemButton;
    private Button enterOCRButton;
    private int list_ID = 0;

    /**
     * Sets up everything at the creation of this activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_to_list);
        /**Sets title for this activity*/
        setTitle("Add new Item");
        Intent intent = getIntent();
        list_ID = intent.getIntExtra("List_ID",0);
        /**setting up the textbox, add button, and enter OCR mode button*/
        addText = (EditText) findViewById(R.id.insertItem);
        addItemButton = (Button) findViewById(R.id.addItemButton);
        enterOCRButton = (Button) findViewById(R.id.enterOCR);
        /**
         * Create an onclick listener for the add Item button
         */
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**Method used to insert an item into a list*/
                insertData();
            }
        });
        /**
         * Create an onclick listner for the enter OCR button
         */
        enterOCRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**Intent used to pass the list_ID to the OCRActivity */
                Intent intent = new Intent(AddItemToList.this, OCRActivity.class);
                intent.putExtra("List_ID", list_ID);
                /**Starting the new intent*/
                startActivity(intent);
                /**Animation used to transition from activity to activity*/
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
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
     * Method used to enter data into database
     */
    private void insertData(){
        int duration = Toast.LENGTH_SHORT;
        /**Check if there is text entered in the textbox, if not then display an error message saying so*/
        if (addText.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Name must be entered", duration).show();
        }

        else{
            /**Create a new DBHelper object*/
            dbHelper = new DBHelper(this);
            /**Creating a new list_item object*/
            List_Item newlist = new List_Item(0,list_ID ,addText.getText().toString());
            /**Inserting the object into the database*/
            dbHelper.addItemToList(newlist, list_ID);
            /**Displays the message that the item has successfully been added*/
            Toast.makeText(getApplicationContext(), "Item successfully added", duration).show();
            /**Return to previous activity*/
            this.finish();
        }

    }

}
