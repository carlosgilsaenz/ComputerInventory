package com.example.android.computerinventory;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.computerinventory.Data.InventoryContract.inventoryEntry;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String SET_TYPE = "text/plain";

    private static final String SET_DATA = "mailto:";

    private static final String STRING_SEPARATOR = "_,_";

    private static final int INVENTORY_LOADER_ID = 0;

    private boolean mInventoryHasChanged;

    private String mHistoryString;

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

    @OnClick(R.id.detail_button_minus)
    public void minusQuantity(){
        //  Checks Quantity value and decrements
        if (checkQuantity()) {

            int oldQuantity = Integer.parseInt(mQuantity.getText().toString());

            String string = "" + --oldQuantity;

            mQuantity.setText(string);

            mInventoryHasChanged = true;
        }
    }

    @OnClick(R.id.detail_button_add)
    public void addQuantity(){
        //  Simply adds to quantity
        int oldQuantity = Integer.parseInt(mQuantity.getText().toString());
        String string = "" + ++oldQuantity;

        mQuantity.setText(string);

        mInventoryHasChanged = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        //  Used determine un-tracked changes on back press
        mInventoryHasChanged = false;
        mEmail.setOnTouchListener(mTouchListener);
        mPrice.setOnTouchListener(mTouchListener);

        mComputerUri = getIntent().getData();

        //  Verify Uri is valid and starts loader
        if(mComputerUri == null){
            displayMessage(R.string.detail_act_URI_ERROR);
            finish();
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
                //  Sends email
                String email[] = {mEmail.getText().toString().trim()};

                String purchaseOrder = getString(R.string.detail_act_purchase_order) + mName.getText().toString().trim();

                if(email[0].isEmpty() || email[0].equals("") || !email[0].contains("@")){
                    displayMessage(R.string.detail_act_email_invalid);
                }  else{
                    Intent intent = new Intent(android.content.Intent.ACTION_SENDTO);
                    intent.setType(SET_TYPE);
                    intent.setData(Uri.parse(SET_DATA));
                    intent.putExtra(Intent.EXTRA_EMAIL, email);
                    intent.putExtra(Intent.EXTRA_SUBJECT,purchaseOrder);
                    startActivity(Intent.createChooser(intent,getString(R.string.detail_act_send_email)));
                }
                return true;
            case R.id.detail_menu_sell:
                //  Updates History and Quantity
                if(checkQuantity()){

                    //  Grab quantity
                    int quantity = Integer.parseInt(mQuantity.getText().toString());

                    //  Grabs current date/time
                    String currentDateString = DateFormat.getDateTimeInstance().format(new Date());

                    // Add to string
                    mHistoryString += currentDateString + STRING_SEPARATOR;

                    ContentValues values = new ContentValues();
                    values.put(inventoryEntry.PRODUCT_HISTORY, mHistoryString);
                    values.put(inventoryEntry.PRODUCT_QUANTITY,--quantity);

                    getContentResolver().update(mComputerUri, values, null, null);

                } else {displayMessage(R.string.detail_act_quantity_invalid);}


                return true;
            case R.id.detail_menu_delete:
                //  Provides Dialog before deleting
                showDeleteConfirmationDialog();
                return true;
            case R.id.detail_menu_save:
                //  Saves any changes to values
                saveComputerChanges();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mInventoryHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    /**
     *  LoaderManager
     */
    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // projection of data to grab
        String[] projection = {
                BaseColumns._ID,
                inventoryEntry.PRODUCT_NAME,
                inventoryEntry.PRODUCT_TYPE,
                inventoryEntry.MANUFACTURE_EMAIL,
                inventoryEntry.PRODUCT_QUANTITY,
                inventoryEntry.PRODUCT_PRICE,
                inventoryEntry.PRODUCT_HISTORY};

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
            int history = data.getColumnIndex(inventoryEntry.PRODUCT_HISTORY);

            setImageType(data.getInt(type));

            setHistory(data.getString(history));

            mName.setText(data.getString(name));
            mEmail.setText(data.getString(email));
            mQuantity.setText(""+ data.getInt(quantity));
            mPrice.setText("" + data.getInt(price));

        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
    }

    /**
     *  Dialogs
     */
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.detail_act_delete_text));
        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete.
                deleteComputer();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.detail_act_modifications));
        builder.setPositiveButton(getString(R.string.discard), discardButtonClickListener);
        builder.setNegativeButton(getString(R.string.keep_editing), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     *  Setters for Views
     */
    private void setHistory(String string) {

        mHistoryString = string;

        mHistory.setText("");
        if(!string.equals("")){
            String[] array = string.split(STRING_SEPARATOR);

            for (int x = 0; x < array.length; x++) {
                mHistory.append("1 Item sold: " + array[x] + "\n");
            }
        }
    }

    private void setImageType(int type){
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

    /**
     *  Helpers
     */

    private void displayMessage(int i){
        Toast.makeText(this,getString(i), Toast.LENGTH_SHORT).show();
    }

    private boolean checkQuantity() {
        int oldQuantity = Integer.parseInt(mQuantity.getText().toString());

        if (oldQuantity != 0) {
            return true;
        }
        return false;
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mInventoryHasChanged = true;
            return false;
        }
    };

    /**
     *  DB methods
     */
    private void deleteComputer(){
        int rowsDeleted = getContentResolver().delete(mComputerUri,null, null);

        if(rowsDeleted == 0){
            displayMessage(R.string.delete_failed);
        } else {
            displayMessage(R.string.delete_successful);
        }
        finish();
    }

    private void saveComputerChanges(){
        //  get email
        String email = mEmail.getText().toString().trim();

        //  get quantity
        int quantity = Integer.parseInt(mQuantity.getText().toString());

        //  Get price
        String priceString = mPrice.getText().toString().trim();
        int price;

        //  confirm price is valid
        if(priceString.isEmpty() || priceString.equals("")){
            displayMessage(R.string.add_act_price_msg);
            return;
        } else{
            price = Integer.parseInt(priceString);
        }

        ContentValues values = new ContentValues();
        values.put(inventoryEntry.MANUFACTURE_EMAIL, email);
        values.put(inventoryEntry.PRODUCT_QUANTITY, quantity);
        values.put(inventoryEntry.PRODUCT_PRICE, price);

        int results = getContentResolver().update(mComputerUri, values, null, null);

        if(results != 0){
            displayMessage(R.string.save_successful);
        } else {
            displayMessage(R.string.save_failed);
        }

        finish();
    }
}
