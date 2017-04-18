package com.example.android.computerinventory;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.computerinventory.Data.InventoryContract.inventoryEntry;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by csaenz on 4/17/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CustomHolder>{

    private static final int RESOURCE_DESKTOP = 0;
    private static final int RESOURCE_LAPTOP = 1;
    private static final int RESOURCE_TABLET = 2;

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
        String quantityString = Integer.toString(mCursor.getInt(quantityIndex));

        int priceIndex = mCursor.getColumnIndex(inventoryEntry.PRODUCT_PRICE);
        String priceString = Integer.toString(mCursor.getInt(priceIndex));

        int imageIndex = mCursor.getColumnIndex(inventoryEntry.PRODUCT_TYPE);
        int imageType = mCursor.getInt(imageIndex);

        //  Set contents on CardView
        TextView titleView = holder.titleView;
        titleView.setText(titleString);

        TextView quantityView = holder.quantityView;
        quantityView.setText(quantityString);

        TextView priceView = holder.priceView;
        priceView.setText(priceString);

        ImageView imageView = holder.imageView;
        setImageView(imageType, imageView);
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
