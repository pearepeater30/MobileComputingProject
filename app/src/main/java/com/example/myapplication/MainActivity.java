package com.example.myapplication;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;
import info.sqlite.helper.DBHelper;
import info.sqlite.model.List_Item;
import info.sqlite.model.ShoppingList;
import recyclerView.ShoppingListAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private ShoppingListAdapter mAdapter;
    private List<ShoppingList> listofShoppingLists;
    private static final int requestPermissionID = 101;

    /**
     * Sets up everything at the creation of this activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**Set TItle of the main screen*/
        setTitle("All Lists");

        /** Setting up the Recycler View*/
        listofShoppingLists = new ArrayList<ShoppingList>();
        recyclerView = (RecyclerView) findViewById(R.id.ShoppingListView);
        mAdapter = new ShoppingListAdapter(this, listofShoppingLists);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, requestPermissionID);
        }

        /** Creating a layout manager and setting it for the recycler view*/
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        /**Assigning the animaotor for the recyclerview*/
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        /**Creating divides between items in the recycler view*/
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        /**Setting the adapter for the recycler view*/
        recyclerView.setAdapter(mAdapter);

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
                deleteData((ShoppingList) viewHolder.itemView.getTag());
                listofShoppingLists.remove(viewHolder.itemView.getTag());
                mAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView);

        /**Creates a floating action button*/
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        /**Setting up a listener for the floating Action Button*/
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**Starts a new activity AddList, which is used to create new lists*/
                startActivity(new Intent(MainActivity.this, AddList.class));
                /**Animations used to transition fom activity to activity*/
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        /**Inserting data into recycler view from Database */
        prepareData();


    }

    /**
     * Refreshing the list upon re-entering this activity
     */
    @Override
    protected void onResume(){
        super.onResume();
        listofShoppingLists.clear();
        mAdapter.notifyDataSetChanged();
        prepareData();
    }

    /**Method used to retrieve rows from the database*/
    private void prepareData(){
        /**Creating a new DBHelper Object*/
        dbHelper = new DBHelper(this);
        /**Retrieving the rows from the database*/
        List<ShoppingList> shoppingList = dbHelper.getAllLists();
        /**Addding the rows retrieved to the list of Shopping list*/
        listofShoppingLists.addAll(shoppingList);
    }

    /**Method used to delete lists from the database*/
    private void deleteData(ShoppingList shoppingList){
        /**store the name of deleted list*/
        String deletedList = shoppingList.getListName();
        /**create a new DBHelper object*/
        dbHelper = new DBHelper(this);
        /**deleting the item from the list*/
        dbHelper.deleteList(shoppingList);
        /**Creating a toast the let the user know that the item has been deleted*/
        Toast.makeText(getApplicationContext(), deletedList + " was successfully deleted", Toast.LENGTH_LONG).show();
    }
}
