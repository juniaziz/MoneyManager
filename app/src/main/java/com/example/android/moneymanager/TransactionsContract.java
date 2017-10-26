package com.example.android.moneymanager;

/*
 * Created by mjfor on 10/25/2017.
 */

import android.database.Cursor;
import android.net.wifi.aware.PublishConfig;
import android.provider.BaseColumns;

public class TransactionsContract {


    /**
     * amount: 10,000
     * currency: PKR
     * timeStamp: 17 November, 2017 14:30:12
     * description: Bajo Qarz
     * parentAmount: Ammi-k-Paisay
     */


    private TransactionsContract(){

    }


    public static final class TransactionEntry implements BaseColumns {


        public final static String TABLE_NAME = "Transactions";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_AMOUNT = "Amount";
        public final static String COLUMN_CURRENCY = "Currency";
        public final static String COLUMN_TYPE = "Type";
        public final static String COLUMN_DATE = "Date";
        public final static String COLUMN_DESCRIPTION = "Description";
        public final static String COLUMN_PARENT_AMOUNT = "Parent_Amount";



        public final static String TYPE_GOING = "Going";
        public final static String TYPE_COMING = "Coming";


        String[] projection = {TransactionEntry.COLUMN_AMOUNT,
                TransactionEntry.COLUMN_CURRENCY,
                TransactionEntry.COLUMN_TYPE,
                TransactionEntry.COLUMN_DATE,
                TransactionEntry.COLUMN_DESCRIPTION,
                TransactionEntry.COLUMN_PARENT_AMOUNT};

        String selection = TransactionEntry.COLUMN_TYPE + "=?";

        /*
        In case of the integer casting into String
         String[] selectionArgs = new String[] { String.valueOf(TransactionEntry.TYPE_GOING)};
         */

        String[] selectionArgs = new String[] {TransactionEntry.TYPE_GOING};




    }







}
