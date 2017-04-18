package com.example.android.computerinventory;

import android.content.Context;
import android.database.Cursor;
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

public class RecylcerAdapter extends RecyclerView.Adapter<RecylcerAdapter.CustomHolder>{

    private Context mContext;
    private Cursor mCursor;

    public RecylcerAdapter(Context context, Cursor cursor){
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

        //  Set contents on CardView
        TextView titleView = holder.titleView;
        titleView.setText(titleString);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
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
}
