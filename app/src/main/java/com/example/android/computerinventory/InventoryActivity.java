package com.example.android.computerinventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.computerinventory.Data.InventoryContract.inventoryEntry;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InventoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private final static int INVENTORY_LOADER_ID = 0;

    RecyclerAdapter mAdapter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @OnClick(R.id.fab)
    public void fabClicked(){
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        ButterKnife.bind(this);

        //  Setup Toolbar, theme sets to noActionBar
        setSupportActionBar(mToolbar);

        //  Set adapter with null value
        mAdapter = new RecyclerAdapter(this,null);

        //  Set adapter to RecyclerView
        mRecyclerView.setAdapter(mAdapter);

        //  Set Layout for RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getLoaderManager().initLoader(INVENTORY_LOADER_ID,null,this);
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

            Toast.makeText(this,"Number of Rows Deleted: " + rowsDeleted, Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // projection of data to grab
        String[] projection = {
                BaseColumns._ID,
                inventoryEntry.PRODUCT_NAME,
                inventoryEntry.PRODUCT_TYPE,
                inventoryEntry.PRODUCT_QUANTITY,
                inventoryEntry.PRODUCT_PRICE};

        return new CursorLoader(this, inventoryEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
