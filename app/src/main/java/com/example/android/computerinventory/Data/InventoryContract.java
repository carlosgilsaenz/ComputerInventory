package com.example.android.computerinventory.Data;

import android.provider.BaseColumns;

/**
 * Created by csaenz on 4/13/2017.
 */

public class InventoryContract {

    //  An empty private constructor ensures the class will not be initialised.
    private InventoryContract(){}

    public static final class inventoryEntry implements BaseColumns{
        //  Table name
        public static final String TABLE_NAME = "inventory";

        // Product name
        public static final String PRODUCT_NAME = "name";

        //  Product type
        public static final String PRODUCT_TYPE = "type";

        //  Product quantity
        public static final String PRODUCT_QUANTITY = "quantity";

        //  Product price
        public static final String PRODUCT_PRICE = "price";
    }
}
