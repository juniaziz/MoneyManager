package com.example.android.moneymanager.data.transactions;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.moneymanager.MainActivity;

/*
 * Created by mjfor on 10/29/2017.
 */

public class TransactionsProvider extends ContentProvider {

    MainActivity homeActivity;


    //URI: content://com.example.android.moneymanager/transactions
    //URI: content://com.example.android.moneymanager/transactions/id


    private static final UriMatcher sURiMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private final static int TRANSACTIONS = 100;
    private final static int TRANSACTIONS_ID = 101;

    static {

        sURiMatcher.addURI(TransactionsContract.CONTENT_AUTHORITY, TransactionsContract.PATH_TRANSACTIONS, TRANSACTIONS);

        sURiMatcher.addURI(TransactionsContract.CONTENT_AUTHORITY, TransactionsContract.PATH_TRANSACTIONS + "/#", TRANSACTIONS_ID);

    }


    private TransactionsDbHelper mTransactionsDbHelper;

    @Override
    public boolean onCreate() {
        mTransactionsDbHelper = new TransactionsDbHelper(getContext());
        return true;
    }



    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

       final int match = sURiMatcher.match(uri);

        switch (match) {
            case TRANSACTIONS:
                return insertTransaction(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insert Error. Invalid URI: " + uri);
        }
    }


    private Uri insertTransaction(Uri uri, ContentValues values) {


        SQLiteDatabase database = mTransactionsDbHelper.getWritableDatabase();

        long newTransactionID = database.insert(TransactionsContract.TransactionEntry.TABLE_NAME, null, values);

        if (newTransactionID == -1) {
            System.out.print("Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, newTransactionID);
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = mTransactionsDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sURiMatcher.match(uri);

        switch (match) {
            case TRANSACTIONS:

                cursor = database.query(TransactionsContract.TransactionEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TRANSACTIONS_ID:

                selection = TransactionsContract.TransactionEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(TransactionsContract.TransactionEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot Query. Invalid URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
