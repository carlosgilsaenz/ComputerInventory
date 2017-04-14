package com.example.android.computerinventory.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.android.computerinventory.Data.InventoryContract.inventoryEntry;

/**
 * Created by csaenz on 4/13/2017.
 */

public class InventoryDBHelper extends SQLiteOpenHelper{

    //  Create table String
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + inventoryEntry.TABLE_NAME + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY," +
                    inventoryEntry.PRODUCT_NAME + " TEXT NOT NULL," +
                    inventoryEntry.PRODUCT_TYPE + " INTEGER NOT NULL," +
                    inventoryEntry.PRODUCT_QUANTITY + " INTEGER CHECK(" +
                    inventoryEntry.PRODUCT_QUANTITY +" >= 0)," +
                    inventoryEntry.PRODUCT_PRICE + " INTEGER CHECK(" +
                    inventoryEntry.PRODUCT_PRICE + " > 0));";

    //  Delete Table
    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXIST " + inventoryEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "computer_inventory.db";

    public InventoryDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
}
