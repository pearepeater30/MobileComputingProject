package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import info.sqlite.helper.DBHelper;
import info.sqlite.model.ShoppingList;

public class AddList extends AppCompatActivity {

    private EditText addText;
    private DBHelper dbHelper;
    private Button addListButton;

    /**
     * Sets up everything at the creation of this activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);
        /**Sets title for this activity*/
        setTitle("Add New List");
        /**setting up the textbox and add button*/
        addText = (EditText) findViewById(R.id.insertList);
        addListButton = (Button) findViewById(R.id.addListButton);
        /**Create a onClickListener for the button*/
        addListButton.setOnClickListener(new View.OnClickListener() {
            /**
             * On button click, do insertData() method
             * @param v
             */
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
    }

    /**
     * Method used to create a new list
     */
    private void insertData(){
        int duration = Toast.LENGTH_SHORT;
        /**Checks if there is text entered, if not then show a message that a name must be entered*/
        if (addText.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Name must be entered", duration).show();
        }
        else{
            /**Create a new DBHelper object*/
            dbHelper = new DBHelper(this);
            /**Creating a new Shopping List object*/
            ShoppingList newlist = new ShoppingList(0, addText.getText().toString());
            /**Insert new list into DB*/
            dbHelper.createList(newlist);
            /**Display a message showing that the object has been successfully created*/
            Toast.makeText(getApplicationContext(), "Item successfully added", duration).show();
            /**Return to previous scene*/
            this.finish();
        }

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
}
