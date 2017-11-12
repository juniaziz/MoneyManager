package w.moneymanager.data.Adapters;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.adapters.CursorSwipeAdapter;

import w.moneymanager.R;
import w.moneymanager.data.data.DatabaseContract;

/**
 * Created by junaidaziz on 11/12/17.
 */

public class TransactionsSwipeAdapter extends CursorSwipeAdapter {

    Cursor gCursor;
    Context gContext;
    long parent_ID;
    String id;

    public TransactionsSwipeAdapter(Context context, Cursor c){
        super(context, c, 0);
        gCursor = c;
        gContext = context;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.swipe_transaction, parent, false);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_transaction;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        TextView amountDescription = view.findViewById(R.id.amount_description);
        TextView amountDate = view.findViewById(R.id.amount_date);
        TextView itemCurrency = view.findViewById(R.id.item_currency);
        TextView itemAmount = view.findViewById(R.id.item_amount);
        View typeIndicator = view.findViewById(R.id.type_indicator);
        ImageView deleteBtn = view.findViewById(R.id.swipe_delete);

        String ID = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry._ID));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_DESCRIPTION));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_TYPE));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_DATE));
        final String currency = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_CURRENCY));
        final String amount = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_AMOUNT));

        Log.d("Type : ", type);

        if (type.equals("Going")) {
            typeIndicator.setBackgroundColor(context.getResources().getColor(R.color.going));
        } else if (type.equals("Coming")) {
            typeIndicator.setBackgroundColor(context.getResources().getColor(R.color.coming));
        } else {
            typeIndicator.setBackgroundColor(context.getResources().getColor(R.color.accent));
        }

        amountDescription.setText(description);
        amountDate.setText(date);
        itemCurrency.setText(currency);
        itemAmount.setText(amount);


        deleteBtn.setTag(ID);


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amountStr = "";
                double amount = 0.0;
                String currentBalanceStr = "";
                double currentBalance = 0.0;
                double newBalance = 0.0;
                String type = "";

                String id = (String) view.getTag();
                long ID = Long.parseLong(id);

                Uri deleteTransactionUri = ContentUris.withAppendedId(DatabaseContract.DatabaseEntry.TRANSACTIONS_URI, ID);
                Log.d("DELETE Trans Uri:", " " + deleteTransactionUri);

                String[] projection = {
                        DatabaseContract.DatabaseEntry._ID,
                        DatabaseContract.DatabaseEntry.COLUMN_AMOUNT,
                        DatabaseContract.DatabaseEntry.COLUMN_TYPE,
                        DatabaseContract.DatabaseEntry.COLUMN_PARENT_AMOUNT};

                Cursor cursorTrans = gContext.getContentResolver().query(deleteTransactionUri, projection, null, null, null);

                Log.d("DELETE Trans Cursor:", " " + DatabaseUtils.dumpCursorToString(cursorTrans));

                if (cursorTrans.moveToFirst()) {
                    amountStr = cursorTrans.getString(cursorTrans.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_AMOUNT));
                    amount = Double.parseDouble(amountStr);
                    id = cursorTrans.getString(cursorTrans.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_PARENT_AMOUNT));
                    type = cursorTrans.getString(cursorTrans.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_TYPE));
                    parent_ID = Long.parseLong(id);
                }

                Uri parentAmountUri = ContentUris.withAppendedId(DatabaseContract.DatabaseEntry.AMOUNTS_URI, parent_ID);
                Log.d("DELETE Amount Uri:", " " + parentAmountUri);

                String[] projectionAmount = {
                        DatabaseContract.DatabaseEntry._ID,
                        DatabaseContract.DatabaseEntry.COLUMN_CURRENT_BALANCE};

                Cursor cursorAmount = gContext.getContentResolver().query(parentAmountUri, projectionAmount, null, null, null);

                if (cursorAmount.moveToFirst()) {
                    currentBalanceStr = cursorAmount.getString(cursorAmount.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_CURRENT_BALANCE));
                    currentBalance = Double.parseDouble(currentBalanceStr);
                }

                if (currentBalance == 0.0 || amount == 0.0){
                    Log.d("DELETE null err:", " " + currentBalance + " " + amount);
                } else {

                    if (type.equalsIgnoreCase("Going")){
                        newBalance = currentBalance + amount;
                    } else if (type.equalsIgnoreCase("Coming")){
                        newBalance = currentBalance - amount;
                    }

                }

                if (newBalance >= 0.0) {
                    ContentValues valuesParent = new ContentValues();
                    valuesParent.put(DatabaseContract.DatabaseEntry.COLUMN_CURRENT_BALANCE, newBalance);

                    int updateParentAmount = gContext.getContentResolver().update(parentAmountUri, valuesParent, null,null);
                    Log.d("DELETE update:", " " + updateParentAmount);

                    int deletedTransactions = gContext.getContentResolver().delete(deleteTransactionUri, null, null);

                    if (deletedTransactions == 0) {
                        Toast.makeText(gContext, "No Transactions Deleted", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(gContext, deletedTransactions + " " + "Transactions Deleted", Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

    }

    @Override
    public void closeAllItems() {

    }
}
