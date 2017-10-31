package com.example.android.moneymanager.data.transactions;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.moneymanager.R;

/*
 * Created by mjfor on 10/29/2017.
 */

public class TransactionsCursorAdapter extends CursorAdapter {



    public TransactionsCursorAdapter(Context context, Cursor c){
        super(context,c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(R.layout.transaction_item, viewGroup, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView amountDescription = view.findViewById(R.id.amount_description);
        TextView amountDate = view.findViewById(R.id.amount_date);
        TextView itemCurrency = view.findViewById(R.id.item_currency);
        TextView itemAmount = view.findViewById(R.id.item_amount);
        View typeIndicator = view.findViewById(R.id.type_indicator);


        String description = cursor.getString(cursor.getColumnIndexOrThrow(TransactionsContract.TransactionEntry.COLUMN_DESCRIPTION));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(TransactionsContract.TransactionEntry.COLUMN_TYPE));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(TransactionsContract.TransactionEntry.COLUMN_DATE));
        String currency = cursor.getString(cursor.getColumnIndexOrThrow(TransactionsContract.TransactionEntry.COLUMN_CURRENCY));
        String amount = cursor.getString(cursor.getColumnIndexOrThrow(TransactionsContract.TransactionEntry.COLUMN_AMOUNT));

        Log.d("Type : ", type);

        if (type.equals("Going")) {
            typeIndicator.setBackgroundColor(Color.RED);
        } else if (type.equals("Coming")){
            typeIndicator.setBackgroundColor(Color.GREEN);
        } else {
            typeIndicator.setBackgroundColor(Color.LTGRAY);
        }

        amountDescription.setText(description);
        amountDate.setText(date);
        itemCurrency.setText(currency);
        itemAmount.setText(amount);

    }
}
