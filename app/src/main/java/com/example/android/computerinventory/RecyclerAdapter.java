package com.example.android.computerinventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.computerinventory.Data.InventoryContract.inventoryEntry;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by csaenz on 4/17/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CustomHolder>{

    private static final int RESOURCE_DESKTOP = 0;
    private static final int RESOURCE_LAPTOP = 1;
    private static final int RESOURCE_TABLET = 2;
    private static final String STRING_SEPARATOR = "_,_";

    private Context mContext;
    private Cursor mCursor;

    public RecyclerAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    @Override
    public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        // Inflate custom Layout
        View view = inflater.inflate(R.layout.cardview_item, parent, false);

        // Return a new holder instance
        CustomHolder viewHolder = new CustomHolder(view);
        return viewHolder;    }

    @Override
    public void onBindViewHolder(CustomHolder holder, int position) {
        mCursor.moveToPosition(position);

        //  Verify contents by referencing index
        int titleIndex = mCursor.getColumnIndex(inventoryEntry.PRODUCT_NAME);
        String titleString = mCursor.getString(titleIndex);

        int quantityIndex = mCursor.getColumnIndex(inventoryEntry.PRODUCT_QUANTITY);
        int quantityInt = mCursor.getInt(quantityIndex);
        String quantityString = Integer.toString(quantityInt);


        int priceIndex = mCursor.getColumnIndex(inventoryEntry.PRODUCT_PRICE);
        int priceInt = mCursor.getInt(priceIndex);

        int imageIndex = mCursor.getColumnIndex(inventoryEntry.PRODUCT_TYPE);
        int imageType = mCursor.getInt(imageIndex);

        int historyIndex = mCursor.getColumnIndex(inventoryEntry.PRODUCT_HISTORY);
        String historyString = mCursor.getString(historyIndex);

        long id = mCursor.getLong(mCursor.getColumnIndex(BaseColumns._ID));

        //  Set contents on CardView
        TextView titleView = holder.titleView;
        titleView.setText(titleString);

        TextView quantityView = holder.quantityView;
        quantityView.setText(quantityString);

        TextView priceView = holder.priceView;
        priceView.setText("$" + priceInt);

        ImageView imageView = holder.imageView;
        setImageView(imageType, imageView);

        //  Set Listener on entire Card
        holder.cardView.setOnClickListener(new CardViewClicked(id));

        //  Set Listener on Sale Button
        holder.saleButton.setOnClickListener(new SaleButtonClicked(quantityInt,id, historyString));
    }

    public class SaleButtonClicked implements View.OnClickListener{

        int mQuantity;

        long mID;

        String mHistoryString;

        public SaleButtonClicked(int quantity, long id, String history) {
            mQuantity = quantity;
            mID = id;
            mHistoryString = history;
        }

        @Override
        public void onClick(View v) {

            //  Updates History and Quantity
            if(mQuantity != 0){

                //  Grabs current date/time
                String currentDateString = DateFormat.getDateTimeInstance().format(new Date());

                // Add to string
                mHistoryString += currentDateString + STRING_SEPARATOR;

                ContentValues values = new ContentValues();
                values.put(inventoryEntry.PRODUCT_HISTORY, mHistoryString);
                values.put(inventoryEntry.PRODUCT_QUANTITY,--mQuantity);

                // Append ID to URI
                Uri uri = ContentUris.withAppendedId(inventoryEntry.CONTENT_URI, mID);

                mContext.getContentResolver().update(uri, values, null, null);

            } else {
                Toast.makeText(mContext,R.string.detail_act_quantity_invalid, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class CardViewClicked implements View.OnClickListener {

        Long mID;

        public CardViewClicked(Long id) {
            mID = id;
        }

        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(mContext, DetailActivity.class);
            Uri uri = ContentUris.withAppendedId(inventoryEntry.CONTENT_URI, mID);
            intent.setData(uri);
            mContext.startActivity(intent);
        }
    }


    @Override
    public int getItemCount() {
        if(mCursor == null){
            return 0;
        } else {
        return mCursor.getCount();
        }
    }

    // Custom View Holder
    public class CustomHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.text_title)
        TextView titleView;
        @BindView(R.id.text_quantity_amount)
        TextView quantityView;
        @BindView(R.id.text_price_amount)
        TextView priceView;
        @BindView(R.id.image_computer_type)
        ImageView imageView;
        @BindView(R.id.card_sale_button)
        Button saleButton;


        public CustomHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void setImageView(int number, ImageView view){
        switch (number){
            case RESOURCE_DESKTOP:
                view.setImageResource(R.drawable.ic_desktop);
                break;
            case RESOURCE_LAPTOP:
                view.setImageResource(R.drawable.ic_laptop);
                break;
            case RESOURCE_TABLET:
                view.setImageResource(R.drawable.ic_tablet);
                break;
        }
    }

    public void swapCursor(Cursor cursor){
        if(mCursor == cursor){
             return;
        }
        mCursor = cursor;
        this.notifyDataSetChanged();
    }
}
