package w.moneymanager.data.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import w.moneymanager.data.data.DatabaseContract.DatabaseEntry;

/*
 * Created by mjfor on 10/25/2017.
 */

public class DatabaseDbHelper extends SQLiteOpenHelper {



    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FinancialRecords.db";



    //Transaction Table
    public final static String TYPE_AMOUNT = "TEXT";
    public final static String TYPE_CURRENCY = "TEXT";
    public final static String TYPE_TYPE = "TEXT";
    public final static String TYPE_DATE = "TEXT";
    public final static String TYPE_DESCRIPTION = "TEXT";
    public final static String TYPE_PARENT_AMOUNT = "TEXT";
    public final static String TYPE_CURRENT_BALANCE = "TEXT";

    public final static String COMMA_SEP = ",";

    public static final String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE "
            + DatabaseEntry.TABLE_NAME_TRANSACTIONS + " ("
            + DatabaseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT " + COMMA_SEP
            + DatabaseEntry.COLUMN_AMOUNT + " " + TYPE_AMOUNT + COMMA_SEP + " "
            + DatabaseEntry.COLUMN_CURRENCY + " " + TYPE_CURRENCY + COMMA_SEP + " "
            + DatabaseEntry.COLUMN_TYPE + " " + TYPE_TYPE + COMMA_SEP + " "
            + DatabaseEntry.COLUMN_DATE + " " + TYPE_DATE + COMMA_SEP + " "
            + DatabaseEntry.COLUMN_DESCRIPTION + " " + TYPE_DESCRIPTION + COMMA_SEP + " "
            + DatabaseEntry.COLUMN_PARENT_AMOUNT + " " + TYPE_PARENT_AMOUNT + " )";

    public static final String CREATE_AMOUNTS_TABLE = "CREATE TABLE "
            + DatabaseEntry.TABLE_NAME_AMOUNTS + " ("
            + DatabaseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP
            + DatabaseEntry.COLUMN_AMOUNT + " " + TYPE_AMOUNT + COMMA_SEP + " "
            + DatabaseEntry.COLUMN_CURRENCY + " " + TYPE_CURRENCY + COMMA_SEP + " "
            + DatabaseEntry.COLUMN_TYPE + " " + TYPE_TYPE + COMMA_SEP + " "
            + DatabaseEntry.COLUMN_DATE + " " + TYPE_DATE + COMMA_SEP + " "
            + DatabaseEntry.COLUMN_DESCRIPTION + " " + TYPE_DESCRIPTION + COMMA_SEP + " "
            + DatabaseEntry.COLUMN_CURRENT_BALANCE + " " + TYPE_CURRENT_BALANCE + " )";


    public DatabaseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TRANSACTIONS_TABLE);
        sqLiteDatabase.execSQL(CREATE_AMOUNTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseEntry.TABLE_NAME_TRANSACTIONS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseEntry.TABLE_NAME_AMOUNTS);
    }
}
