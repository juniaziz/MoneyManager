package w.moneymanager.data.Adapters;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import w.moneymanager.R;
import w.moneymanager.data.data.DatabaseContract;

import static android.R.attr.id;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


/*
 * Created by mjfor on 10/29/2017.
 */

public class TransactionsCursorAdapter extends CursorAdapter {



    public TransactionsCursorAdapter(Context context, Cursor c){
        super(context,c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.swipe_transaction, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

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
        String currency = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_CURRENCY));
        String amount = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_AMOUNT));

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


        deleteBtn.setTag(ID);


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = (String) view.getTag();
                long ID = Long.parseLong(id);

                Uri deleteTransactionUri = ContentUris.withAppendedId(DatabaseContract.DatabaseEntry.TRANSACTIONS_URI, ID);
                Log.d("Delete Trans:", " " + deleteTransactionUri);

                int deletedTransactions = context.getContentResolver().delete(deleteTransactionUri, null, null);

                if (deletedTransactions == 0) {
                    Toast.makeText(context, "No Transactions Deleted", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(context, deletedTransactions + " " + "Transactions Deleted", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
