package w.moneymanager.data.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


/*
 * Created by mjfor on 10/29/2017.
 */

public class DatabaseContentProvider extends ContentProvider {

    //URI: content://com.example.android.moneymanager/transactions
    //URI: content://com.example.android.moneymanager/transactions/id

    private static final UriMatcher sURiMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //Amounts
    private static final int AMOUNTS = 100;
    private static final int AMOUNT_ID = 101;
    private static final int AMOUNT_CURRENCY = 102;

    //Transactions
    private final static int TRANSACTIONS = 200;
    private final static int TRANSACTIONS_ID = 201;

    static {
        //Amounts
        sURiMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_AMOUNTS, AMOUNTS);
        sURiMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_AMOUNTS + "/#", AMOUNT_ID);

        //Transactions
        sURiMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_TRANSACTIONS, TRANSACTIONS);
        sURiMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_TRANSACTIONS + "/#", TRANSACTIONS_ID);
    }

    private DatabaseDbHelper mDatabaseDbHelper;

    @Override
    public boolean onCreate() {
        mDatabaseDbHelper = new DatabaseDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
       final int match = sURiMatcher.match(uri);
        switch (match) {
            case AMOUNTS:
                return insertAmount(uri, contentValues);
            case TRANSACTIONS:
                return insertTransaction(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insert Error. Invalid URI: " + uri);
        }
    }

    private Uri insertAmount(Uri uri, ContentValues values){
        SQLiteDatabase database = mDatabaseDbHelper.getWritableDatabase();
        long newAmountID = database.insert(DatabaseContract.DatabaseEntry.TABLE_NAME_AMOUNTS, null, values);
        if (newAmountID == -1) {
            System.out.print("Failed to insert new row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, newAmountID);
    }

    private Uri insertTransaction(Uri uri, ContentValues values) {
        SQLiteDatabase database = mDatabaseDbHelper.getWritableDatabase();
        long newTransactionID = database.insert(DatabaseContract.DatabaseEntry.TABLE_NAME_TRANSACTIONS, null, values);
        if (newTransactionID == -1) {
            System.out.print("Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, newTransactionID);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDatabaseDbHelper.getWritableDatabase();
        int match = sURiMatcher.match(uri);
        switch (match){
            case AMOUNT_ID:
                selection = DatabaseContract.DatabaseEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                getContext().getContentResolver().notifyChange(uri, null);
                return database.update(DatabaseContract.DatabaseEntry.TABLE_NAME_AMOUNTS, contentValues, selection, selectionArgs);
            default:
                System.out.print("Update Failed. Invalid Uri");
                return 0;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDatabaseDbHelper.getWritableDatabase();
        int match = sURiMatcher.match(uri);
        switch (match){
            case AMOUNT_ID:
                selection = DatabaseContract.DatabaseEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                getContext().getContentResolver().notifyChange(uri, null);
                return database.delete(DatabaseContract.DatabaseEntry.TABLE_NAME_AMOUNTS, selection, selectionArgs);
            case TRANSACTIONS:
                getContext().getContentResolver().notifyChange(uri, null);
                return database.delete(DatabaseContract.DatabaseEntry.TABLE_NAME_TRANSACTIONS, selection, selectionArgs);
            case TRANSACTIONS_ID:
                Log.d("Delete Transaction", " " + uri);
                selection = DatabaseContract.DatabaseEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                getContext().getContentResolver().notifyChange(uri, null);
                return database.delete(DatabaseContract.DatabaseEntry.TABLE_NAME_TRANSACTIONS, selection, selectionArgs);
            default:
                System.out.print("Delete Failed");
                return 0;
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDatabaseDbHelper.getWritableDatabase();
        Cursor cursor;
        int match = sURiMatcher.match(uri);
        switch (match) {
            //Transactions
            case TRANSACTIONS:
                cursor = database.query(DatabaseContract.DatabaseEntry.TABLE_NAME_TRANSACTIONS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TRANSACTIONS_ID:
                selection = DatabaseContract.DatabaseEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(DatabaseContract.DatabaseEntry.TABLE_NAME_TRANSACTIONS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            //Amounts
            case AMOUNTS:
                cursor = database.query(DatabaseContract.DatabaseEntry.TABLE_NAME_AMOUNTS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case AMOUNT_ID:                selection = DatabaseContract.DatabaseEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(DatabaseContract.DatabaseEntry.TABLE_NAME_AMOUNTS, projection, selection, selectionArgs, null, null, sortOrder);
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


//    https://stackoverflow.com/questions/31367270/exporting-sqlite-database-to-csv-file-in-android
//
//    private void exportDB() {
//
//        File dbFile=getDatabasePath("MyDBName.db");
//        DBHelper dbhelper = new DBHelper(getApplicationContext());
//        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
//        if (!exportDir.exists())
//        {
//            exportDir.mkdirs();
//        }
//
//        File file = new File(exportDir, "csvname.csv");
//        try
//        {
//            file.createNewFile();
//            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
//            SQLiteDatabase db = dbhelper.getReadableDatabase();
//            Cursor curCSV = db.rawQuery("SELECT * FROM contacts",null);
//            csvWrite.writeNext(curCSV.getColumnNames());
//            while(curCSV.moveToNext())
//            {
//                //Which column you want to exprort
//                String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2)};
//                csvWrite.writeNext(arrStr);
//            }
//            csvWrite.close();
//            curCSV.close();
//        }
//        catch(Exception sqlEx)
//        {
//            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
//        }


}
