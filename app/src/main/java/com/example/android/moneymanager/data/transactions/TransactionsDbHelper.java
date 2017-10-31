package com.example.android.moneymanager.data.transactions;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * Created by mjfor on 10/25/2017.
 */

public class TransactionsDbHelper extends SQLiteOpenHelper {



    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Financial Records";

    public final static String TYPE_AMOUNT = "TEXT";
    public final static String TYPE_CURRENCY = "TEXT";
    public final static String TYPE_TYPE = "TEXT";
    public final static String TYPE_DATE = "TEXT";
    public final static String TYPE_DESCRIPTION = "TEXT";
    public final static String TYPE_PARENt_AMOUNT = "TEXT";
    public final static String COMMA_SEP = ",";


    public static final String SQL_CREATE_TABLES = "CREATE TABLE " + TransactionsContract.TransactionEntry.TABLE_NAME + " ("
            + TransactionsContract.TransactionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT " + COMMA_SEP
            + TransactionsContract.TransactionEntry.COLUMN_AMOUNT + " " + TYPE_AMOUNT + COMMA_SEP + " "
            + TransactionsContract.TransactionEntry.COLUMN_CURRENCY + " " + TYPE_CURRENCY + COMMA_SEP + " "
            + TransactionsContract.TransactionEntry.COLUMN_TYPE + " " + TYPE_TYPE + COMMA_SEP + " "
            + TransactionsContract.TransactionEntry.COLUMN_DATE + " " + TYPE_DATE + COMMA_SEP + " "
            + TransactionsContract.TransactionEntry.COLUMN_DESCRIPTION + " " + TYPE_DESCRIPTION + COMMA_SEP + " "
            + TransactionsContract.TransactionEntry.COLUMN_PARENT_AMOUNT + " " + TYPE_PARENt_AMOUNT + " "
            +")";



    public TransactionsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }





    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
