package com.example.android.computerinventory;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.computerinventory.Data.InventoryContract.inventoryEntry;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private final static int INVENTORY_LOADER_ID = 0;

    private Uri mComputerUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mComputerUri = getIntent().getData();

        if(mComputerUri == null){
            displayMessage("Error with Data Selection");
        } else{
            getLoaderManager().initLoader(INVENTORY_LOADER_ID,null,this);
        }
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // projection of data to grab
        String[] projection = {
                BaseColumns._ID,
                inventoryEntry.PRODUCT_NAME,
                inventoryEntry.PRODUCT_TYPE,
                inventoryEntry.MANUFACTURE_EMAIL,
                inventoryEntry.PRODUCT_QUANTITY,
                inventoryEntry.PRODUCT_PRICE};

        return new CursorLoader(this, mComputerUri, projection, null, null, null);    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {

        if(data.moveToFirst()){
            TextView textView = (TextView) findViewById(R.id.detail_text_view);

            String name = data.getString(data.getColumnIndex(inventoryEntry.PRODUCT_NAME));
            textView.setText("Product Name : " + name + "\n");

            String email = data.getString(data.getColumnIndex(inventoryEntry.MANUFACTURE_EMAIL));
            textView.append("Manufacture's  Email: " + email + "\n");

        } else {
            displayMessage("Load failed");
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }

    public void displayMessage(String string){
        Toast.makeText(this,string, Toast.LENGTH_SHORT).show();
    }

}
