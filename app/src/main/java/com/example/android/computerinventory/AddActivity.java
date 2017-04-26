package com.example.android.computerinventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.android.computerinventory.Data.InventoryContract.inventoryEntry;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddActivity extends AppCompatActivity {

    private static final int DESKTOP = 0;

    private static final int LAPTOP = 1;

    private static final int TABLET = 2;

    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;

    @BindView(R.id.image_view)
    ImageView mComputerImage;

    @BindView(R.id.name_edit_text)
    EditText mName;

    @BindView(R.id.email_edit_text)
    EditText mEmail;

    @BindView(R.id.price_edit_text)
    EditText mPrice;

    @OnClick({R.id.radio_button_desktop, R.id.radio_button_laptop, R.id.radio_button_tablet})
    public void updateComputerImage() {
        //  Updates image live
        int checkedButton = mRadioGroup.getCheckedRadioButtonId();

        switch (checkedButton){
            case R.id.radio_button_desktop:
                mComputerImage.setImageResource(R.drawable.ic_desktop);
                break;
            case R.id.radio_button_laptop:
                mComputerImage.setImageResource(R.drawable.ic_laptop);
                break;
            case R.id.radio_button_tablet:
                mComputerImage.setImageResource(R.drawable.ic_tablet);
                break;
        }
    }

    @OnClick(R.id.save_button)
    public void saveButtonClicked(){
        //  Determine which radio button selected
        int checkedButton = mRadioGroup.getCheckedRadioButtonId();
        int computerType;

        //  Confirm selection has been checked
        if(checkedButton == -1){
            displayMessage(R.string.add_act_type_msg);
            return;
        } else {
            //  Convert to value for DB
            computerType = getComputerType(checkedButton);
        }

        //  Get name of product
        String name = mName.getText().toString().trim();

        //  Name is required input
        if(name.equals("") || name.isEmpty()){
            displayMessage(R.string.add_name_msg);
            return;
        }

        //  Get email
        String email = mEmail.getText().toString().trim();

        //  email is required input
        if(email.equals("") || email.isEmpty() || !email.contains("@")){
            displayMessage(R.string.add_email_msg);
            return;
        }

        //  Get price
        String priceString = mPrice.getText().toString().trim();
        int priceInt;

        //  confirm price is valid
        if(priceString.isEmpty() || priceString.equals("")){
            displayMessage(R.string.add_act_price_msg);
            return;
        } else{
            priceInt = Integer.parseInt(priceString);
        }

        //  Create Values to insert
        ContentValues values = new ContentValues();

        values.put(inventoryEntry.PRODUCT_NAME, name);
        values.put(inventoryEntry.PRODUCT_TYPE, computerType);
        values.put(inventoryEntry.MANUFACTURE_EMAIL, email);
        values.put(inventoryEntry.PRODUCT_PRICE, priceInt);

        Uri uri = getContentResolver().insert(inventoryEntry.CONTENT_URI, values);

        if(ContentUris.parseId(uri) != -1){
            displayMessage(R.string.save_successful);
        } else {
            displayMessage(R.string.save_failed);
        }

        finish();
    }

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ButterKnife.bind(this);
    }

    private void displayMessage(int i){
        Toast.makeText(this,getString(i), Toast.LENGTH_SHORT).show();
    }

    public int getComputerType(int checkedButton){

        switch(checkedButton){
            case R.id.radio_button_desktop:
                return DESKTOP;
            case R.id.radio_button_laptop:
                return LAPTOP;
            default:
                return TABLET;
        }
    }
}
