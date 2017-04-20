package com.example.android.computerinventory;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.computerinventory.Data.InventoryContract.inventoryEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private final static int INVENTORY_LOADER_ID = 0;

    private Uri mComputerUri;

    @BindView(R.id.detail_image_type)
    ImageView mImage;

    @BindView(R.id.detail_text_name)
    TextView mName;

    @BindView(R.id.detail_text_quantity_amt)
    TextView mQuantity;

    @BindView(R.id.detail_history)
    TextView mHistory;

    @BindView(R.id.detail_text_email)
    EditText mEmail;

    @BindView(R.id.detail_text_price)
    EditText mPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        mComputerUri = getIntent().getData();

        if(mComputerUri == null){
            displayMessage("Error with Data Selection");
        } else{
            getLoaderManager().initLoader(INVENTORY_LOADER_ID,null,this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.detail_menu_order:
                return true;
            case R.id.detail_menu_sell:
                return true;
            case R.id.detail_menu_delete:
                return true;
        }

        return super.onOptionsItemSelected(item);
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
            //  Get index for all items needed
            int name = data.getColumnIndex(inventoryEntry.PRODUCT_NAME);
            int type = data.getColumnIndex(inventoryEntry.PRODUCT_TYPE);
            int price = data.getColumnIndex(inventoryEntry.PRODUCT_PRICE);
            int quantity = data.getColumnIndex(inventoryEntry.PRODUCT_QUANTITY);
            int email = data.getColumnIndex(inventoryEntry.MANUFACTURE_EMAIL);

            setImageType(data.getInt(type));

            mName.setText(data.getString(name));
            mEmail.setText(data.getString(email));
            mQuantity.setText(""+ data.getInt(quantity));
            mPrice.setText("" + data.getInt(price));

        } else {
            displayMessage("Load failed");
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }

    public void addQuantity(View view){

        int oldQuantity = Integer.parseInt(mQuantity.getText().toString());
        String string = "" + ++oldQuantity;

        mQuantity.setText(string);
    }

    public void minusQuantity(View view){

        int oldQuantity = Integer.parseInt(mQuantity.getText().toString());

        if(oldQuantity != 0){
            String string = "" + --oldQuantity;

            mQuantity.setText(string);
        }
    }

    public void saveButton(View view){
        //  get quantity
        int quantity = Integer.parseInt(mQuantity.getText().toString());

        //  get email
        String email = mEmail.getText().toString().trim();

        //  Get price
        String priceString = mPrice.getText().toString().trim();
        int price;

        //  confirm price is valid
        if(priceString.isEmpty() || priceString.equals("")){
            displayMessage("Price is invalid");
            return;
        } else{
            price = Integer.parseInt(priceString);
        }

        //TODO: add more stuff
    }

    public void displayMessage(String string){
        Toast.makeText(this,string, Toast.LENGTH_SHORT).show();
    }

    public void setImageType(int type){
        switch (type) {
            case 0:
                mImage.setImageResource(R.drawable.ic_desktop);
                break;
            case 1:
                mImage.setImageResource(R.drawable.ic_laptop);
                break;
            default:
                mImage.setImageResource(R.drawable.ic_tablet);
                break;
        }
    }
}
