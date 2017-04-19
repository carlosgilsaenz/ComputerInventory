package com.example.android.computerinventory.Data;

import android.content.ContentResolver;
import android.net.Uri;
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

        //  Manufacturers email
        public static final String MANUFACTURE_EMAIL = "email";

        //  URI creation
        public static final String CONTENT_AUTHORITY = "com.example.android.computerinventory";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_INVENTORY = "inventory";

        //  Create URI
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        //  MIME type for list of inventory
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;
    }
}
