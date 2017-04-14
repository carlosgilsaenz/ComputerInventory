package com.example.android.computerinventory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.computerinventory.Data.InventoryContract.inventoryEntry;
import com.example.android.computerinventory.Data.InventoryDBHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InventoryActivity extends AppCompatActivity {

    InventoryDBHelper mDbHelper;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @OnClick(R.id.fab)
    public void fabClicked(){
        insertData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        ButterKnife.bind(this);

        //  Setup Toolbar, theme sets to noActionBar
        setSupportActionBar(mToolbar);

        //  Config Recycler View Layout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDbHelper = new InventoryDBHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertData(){
        //  Create Values to insert
        ContentValues values = new ContentValues();

        values.put(inventoryEntry.PRODUCT_NAME, "Laptop");
        values.put(inventoryEntry.PRODUCT_TYPE, 0);
        values.put(inventoryEntry.PRODUCT_QUANTITY, 1);
        values.put(inventoryEntry.PRODUCT_PRICE, 1000);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long newRowId = db.insert(inventoryEntry.TABLE_NAME, null, values);

        if(newRowId != -1){
            Toast.makeText(this,"Insert successful", Toast.LENGTH_SHORT).show();}

    }

    public void displayData(SQLiteDatabase db){
        TextView textView = (TextView) findViewById(R.id.empty_text_view);

        // projection of data to grab
        String[] projection = {
                BaseColumns._ID,
                inventoryEntry.PRODUCT_NAME,
                inventoryEntry.PRODUCT_TYPE,
                inventoryEntry.PRODUCT_QUANTITY,
                inventoryEntry.PRODUCT_PRICE};

        // Perform a query on the pets table
        Cursor cursor = db.query(
                inventoryEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        textView.setText("The Inventory contains " + cursor.getCount() + "items.\n\n");


    }
}
