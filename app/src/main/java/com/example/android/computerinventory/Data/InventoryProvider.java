package com.example.android.computerinventory.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.computerinventory.Data.InventoryContract.inventoryEntry;

/**
 * Created by csaenz on 4/14/2017.
 */

public class InventoryProvider extends ContentProvider{

    private static final int COMPUTERS = 100;

    private static final int COMPUTER_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(inventoryEntry.CONTENT_AUTHORITY, inventoryEntry.PATH_INVENTORY, COMPUTERS);
        sUriMatcher.addURI(inventoryEntry.CONTENT_AUTHORITY, inventoryEntry.PATH_INVENTORY + "/#", COMPUTER_ID);

    }

    private InventoryDBHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new InventoryDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get readable database
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match){
            case COMPUTERS:
                cursor = db.query(inventoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case COMPUTER_ID:

                selection = inventoryEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = db.query(inventoryEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        //  set listener for changes to DB
        cursor.setNotificationUri(getContext().getContentResolver(),inventoryEntry.CONTENT_URI);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
//        final int match = sUriMatcher.match(uri);
//        switch (match){
//            case COMPUTERS:
//                return inventoryEntry.CONTENT_LIST_TYPE;
//            default:
//                throw new IllegalArgumentException("Unknown URI " + uri + "with match " + match);
//        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        //  Confirm Name is valid
        String name = values.getAsString(inventoryEntry.PRODUCT_NAME);

        if(name.isEmpty() || name.equals("")){
            return null;
        }

        //  Confirm email is not valid
        String email = values.getAsString(inventoryEntry.MANUFACTURE_EMAIL);

        if(email.isEmpty() || email.equals("") || !email.contains("@")){
            return null;
        }

        //  Confirm computer type
        int type = values.getAsInteger(inventoryEntry.PRODUCT_TYPE);

        if(type < 0 || type > 2){
            return null;
        }

        // Get writable database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long id = db.insert(InventoryContract.inventoryEntry.TABLE_NAME, null, values);

        db.close();

        //notify listener of change
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        //  Get writable database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match){
            case COMPUTERS:
                rowsDeleted = db.delete(inventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case COMPUTER_ID:

                selection = inventoryEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                rowsDeleted = db.delete(inventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                rowsDeleted = 0;
        }

        if(rowsDeleted > 0){
            //notify listener of change
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int match = sUriMatcher.match(uri);
        switch (match){
            case COMPUTERS:
                return updateInventory(uri, values, selection, selectionArgs);
            case COMPUTER_ID:

                selection = inventoryEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                return updateInventory(uri, values, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
    }

    private int updateInventory(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        //  Confirm email is not valid
        String email = values.getAsString(inventoryEntry.MANUFACTURE_EMAIL);

        if(email.isEmpty() || email.equals("") || !email.contains("@")){
            return 0;
        }

        if(values.size() == 0){
            return 0;
        }

        //  Get writable database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //  Update DB and save results
        int count = db.update(inventoryEntry.TABLE_NAME, values, selection, selectionArgs);

        //notify listener of change
        getContext().getContentResolver().notifyChange(uri, null);

        db.close();
        return count;
    }
}
