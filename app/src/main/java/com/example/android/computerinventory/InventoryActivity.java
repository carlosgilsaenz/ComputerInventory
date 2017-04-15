package com.example.android.computerinventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.computerinventory.Data.InventoryContract.inventoryEntry;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InventoryActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @OnClick(R.id.fab)
    public void fabClicked(){
        insertData();
        displayData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        ButterKnife.bind(this);

        //  Setup Toolbar, theme sets to noActionBar
        setSupportActionBar(mToolbar);

        displayData();
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
        if (id == R.id.action_delete) {
            int rowsDeleted = getContentResolver().delete(inventoryEntry.CONTENT_URI,null,null);

            displayData();

            Toast.makeText(this,"Number of Rows Deleted: " + rowsDeleted, Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertData(){
        //  Create Values to insert
        ContentValues values = new ContentValues();

        values.put(inventoryEntry.PRODUCT_NAME, "Desktop");
        values.put(inventoryEntry.PRODUCT_TYPE, 0);
        values.put(inventoryEntry.PRODUCT_QUANTITY, 0);
        values.put(inventoryEntry.PRODUCT_PRICE, 1);

        Uri uri = getContentResolver().insert(inventoryEntry.CONTENT_URI, values);

        if(ContentUris.parseId(uri) != -1){
            Toast.makeText(this,"Insert successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"Insert unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }

    public void displayData(){

        TextView textView = (TextView) findViewById(R.id.empty_text_view);

        // projection of data to grab
        String[] projection = {
                BaseColumns._ID,
                inventoryEntry.PRODUCT_NAME,
                inventoryEntry.PRODUCT_TYPE,
                inventoryEntry.PRODUCT_QUANTITY,
                inventoryEntry.PRODUCT_PRICE};

        // Perform a query on the pets table
        Cursor cursor = getContentResolver().query(inventoryEntry.CONTENT_URI,projection,null,null,null);

        textView.setText("The Inventory contains " + cursor.getCount() + " items.\n\n");

        int indexName = cursor.getColumnIndex(inventoryEntry.PRODUCT_NAME);
        int indexType = cursor.getColumnIndex(inventoryEntry.PRODUCT_TYPE);
        int indexQuantity = cursor.getColumnIndex(inventoryEntry.PRODUCT_QUANTITY);
        int indexPrice = cursor.getColumnIndex(inventoryEntry.PRODUCT_PRICE);

        while(cursor.moveToNext()){

            String stringName = cursor.getString(indexName);
            int intType = cursor.getInt(indexType);
            int intQuantity = cursor.getInt(indexQuantity);
            int intPrice = cursor.getInt(indexPrice);

            textView.append("Product name: " + stringName + "\n");
            textView.append("Product Type: " + intType + "\n");
            textView.append("Product Quantity: " + intQuantity + "\n");
            textView.append("Product Price " +  intPrice + "\n\n");
        }
    }
}
