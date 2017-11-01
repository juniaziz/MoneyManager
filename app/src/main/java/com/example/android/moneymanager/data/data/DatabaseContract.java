package com.example.android.moneymanager.data.data;

/*
 * Created by mjfor on 10/25/2017.
 */

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {


    /**
     * amount: 10,000
     * currency: PKR
     * timeStamp: 17 November, 2017 14:30:12
     * description: Bajo Qarz
     * parentAmount: Ammi-k-Paisay
     */

    public final static String CONTENT_AUTHORITY = "com.example.android.moneymanager";
    //public final static String TEST_URI = "testPATH";
    public final static String PATH_TRANSACTIONS = "transations";
    public final static String PATH_AMOUNTS = "Amounts";

    public final static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);



    private DatabaseContract(){

    }


    public static final class DatabaseEntry implements BaseColumns {


        public final static Uri TRANSACTIONS_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TRANSACTIONS);
        public final static Uri AMOUNTS_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_AMOUNTS);
        public final static Uri AMOUNTS_ID_URI = Uri.withAppendedPath(AMOUNTS_URI, "/#");

        public final static String TABLE_NAME_TRANSACTIONS = "Transactions";
        public final static String TABLE_NAME_AMOUNTS = "Amounts";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_AMOUNT = "Amount";
        public final static String COLUMN_CURRENCY = "Currency";
        public final static String COLUMN_TYPE = "Type";
        public final static String COLUMN_DATE = "Date";
        public final static String COLUMN_DESCRIPTION = "Description";
        public final static String COLUMN_PARENT_AMOUNT = "Parent_Amount";
        public final static String COLUMN_CURRENT_BALANCE = "Current_Balance";

        public final static String TYPE_GOING = "Going";
        public final static String TYPE_COMING = "Coming";


        /*
        String[] projection = {DatabaseEntry.COLUMN_AMOUNT,
                DatabaseEntry.COLUMN_CURRENCY,
                DatabaseEntry.COLUMN_TYPE,
                DatabaseEntry.COLUMN_DATE,
                DatabaseEntry.COLUMN_DESCRIPTION,
                DatabaseEntry.COLUMN_PARENT_AMOUNT};

        String selection = DatabaseEntry.COLUMN_TYPE + "=?";


        In case of the integer casting into String
         String[] selectionArgs = new String[] { String.valueOf(DatabaseEntry.TYPE_GOING)};


        String[] selectionArgs = new String[] {DatabaseEntry.TYPE_GOING};
        */



    }







}
