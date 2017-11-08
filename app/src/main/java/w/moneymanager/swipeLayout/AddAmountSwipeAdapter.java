package w.moneymanager.swipeLayout;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.swipe.adapters.CursorSwipeAdapter;
import w.moneymanager.R;
import w.moneymanager.data.data.DatabaseContract;

import static android.os.Build.ID;

/*
 * Created by nikunjkumar on 11/6/17.
 */

public class AddAmountSwipeAdapter extends CursorSwipeAdapter  {

    Context gContext;


    int mPosition;
    String _ID;
    int id;

    public AddAmountSwipeAdapter(Context context, Cursor c){
        super(context, c, 0);
        this.gContext = context;

    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        mPosition = position;
        Log.d("SWIPE id:", " " + position);
        return R.id.swipe_transaction;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.swipe_transaction, parent, false);
    }

    @Override
    public void closeAllItems() {
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {



        TextView amountDescription = view.findViewById(R.id.amount_description);
        TextView amountDate = view.findViewById(R.id.amount_date);
        TextView itemCurrency = view.findViewById(R.id.item_currency);
        TextView itemAmount = view.findViewById(R.id.item_amount);
        View typeIndicator = view.findViewById(R.id.type_indicator);
        ImageView deleteImg = view.findViewById(R.id.swipe_delete);
        ImageView editImg = view.findViewById(R.id.swipe_Edit);

        _ID = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry._ID));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_DESCRIPTION));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_TYPE));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_DATE));
        String currency = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_CURRENCY));
        String amount = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_AMOUNT));

        Log.d("Type : ", type);

        if (type.equals("Going")) {
            typeIndicator.setBackgroundColor(Color.RED);
        } else if (type.equals("Coming")) {
            typeIndicator.setBackgroundColor(Color.GREEN);
        } else {
            typeIndicator.setBackgroundColor(Color.LTGRAY);
        }

        amountDescription.setText(description);
        amountDate.setText(date);
        itemCurrency.setText(currency);
        itemAmount.setText(amount);

        id = Integer.parseInt(_ID);

        Log.d("mPostion Genesis:", " " + id);

        deleteImg.setTag(id);
        editImg.setTag(id);


        deleteImg.setOnClickListener(onDeleteClicked);

        editImg.setOnClickListener(onEditItemClicked);



    }

    private View.OnClickListener onEditItemClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Cursor gCursor = null;
            id = (Integer) v.getTag();

            Log.d("mPosition", " " + id);

            final Dialog dialog = new Dialog(gContext);
            dialog.setContentView(R.layout.edit_dialog);
            dialog.setTitle("Delete");
            dialog.setCancelable(true);
            dialog.show();

            final TextView selectedDateTextView = dialog.findViewById(R.id.dateTextView);
            final EditText amountEditText = dialog.findViewById(R.id.amount_textfield);
            final EditText currencyEditText = dialog.findViewById(R.id.currency_textfield);
            final EditText descriptionEditText = dialog.findViewById(R.id.description_textfield);
            final RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
            final Button saveChanges = dialog.findViewById(R.id.btn_save_changes);




            String[] projection = {
                    DatabaseContract.DatabaseEntry._ID,
                    DatabaseContract.DatabaseEntry.COLUMN_AMOUNT,
                    DatabaseContract.DatabaseEntry.COLUMN_CURRENCY,
                    DatabaseContract.DatabaseEntry.COLUMN_TYPE,
                    DatabaseContract.DatabaseEntry.COLUMN_DATE,
                    DatabaseContract.DatabaseEntry.COLUMN_DESCRIPTION,
                    DatabaseContract.DatabaseEntry.COLUMN_CURRENT_BALANCE};



            final Uri currentAmountUri = ContentUris.withAppendedId(DatabaseContract.DatabaseEntry.AMOUNTS_URI, id);

            try {
                gCursor = gContext.getContentResolver().query(currentAmountUri, projection, null, null, null);

                if (gCursor.moveToFirst()) {
                    String ID = gCursor.getString(getCursor().getColumnIndexOrThrow(DatabaseContract.DatabaseEntry._ID));
                    String description = gCursor.getString(gCursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_DESCRIPTION));
                    String type = gCursor.getString(gCursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_TYPE));
                    String date = gCursor.getString(gCursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_DATE));
                    String currency = gCursor.getString(gCursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_CURRENCY));
                    String amount = gCursor.getString(gCursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_AMOUNT));
                    String currentBalance = gCursor.getString(gCursor.getColumnIndexOrThrow(DatabaseContract.DatabaseEntry.COLUMN_CURRENT_BALANCE));



                    Log.d("Type : ", type);

                    if (type.equals("Going")) {
                        radioGroup.check(R.id.radioBtnGoing);
                    } else if (type.equals("Coming")) {
                        radioGroup.check(R.id.radioBtnComing);
                    } else {
                        radioGroup.clearCheck();
                    }

                    descriptionEditText.setText(description);
                    selectedDateTextView.setText(date);

                    selectedDateTextView.setEnabled(false);
                    selectedDateTextView.setAlpha(.5f);

                    currencyEditText.setText(currency);
                    amountEditText.setText(amount);

                    if (!(amount.equalsIgnoreCase(currentBalance))){
                        amountEditText.setFocusable(false);

                        amountEditText.setAlpha(.5f);

                        View.OnClickListener disableToast = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(gContext, "Cannot change in amounts with transactions", Toast.LENGTH_LONG).show();
                            }
                        };

                        selectedDateTextView.setOnClickListener(disableToast);
                        amountEditText.setOnClickListener(disableToast);

                    }


                    saveChanges.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final int selectId = radioGroup.getCheckedRadioButtonId();
                            final RadioButton radioButtonID = dialog.findViewById(selectId);

                            String amount = amountEditText.getText().toString().trim();
                            String currency = currencyEditText.getText().toString().trim();
                            String description = descriptionEditText.getText().toString().trim();
                            String selectedDate = selectedDateTextView.getText().toString().trim();
                            String radioButton = radioButtonID.getText().toString();


                            ContentValues values = new ContentValues();

                            values.put(DatabaseContract.DatabaseEntry.COLUMN_AMOUNT, amount);
                            values.put(DatabaseContract.DatabaseEntry.COLUMN_CURRENCY, currency);
                            values.put(DatabaseContract.DatabaseEntry.COLUMN_TYPE, radioButton);
                            values.put(DatabaseContract.DatabaseEntry.COLUMN_DATE, selectedDate);
                            values.put(DatabaseContract.DatabaseEntry.COLUMN_DESCRIPTION, description);

                            Log.d("Type from values: ", String.valueOf(values.getAsString(DatabaseContract.DatabaseEntry.COLUMN_TYPE)));

                            try {
                                int rowsAffected = gContext.getContentResolver().update(currentAmountUri, values, null, null);

                                if (rowsAffected == 0) {
                                    Toast.makeText(gContext, "Save Failed", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(gContext, "Saved Successfully", Toast.LENGTH_SHORT).show();
                                }
                            } finally {
                                System.out.print("Unexpected error");
                                dialog.dismiss();
                            }

                            dialog.dismiss();


                        }
                    });


                }
            }
            catch (CursorIndexOutOfBoundsException e){
                Log.d("mPosition Cursor error:", " " + gCursor.getCount() + " " + DatabaseUtils.dumpCursorToString(gCursor));
            }



        }
    };


    private View.OnClickListener onDeleteClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Dialog dialog = new Dialog(gContext);
            dialog.setContentView(R.layout.alert_dialog);
            dialog.setTitle("Delete");
            dialog.setCancelable(true);
            dialog.show();

            id = (Integer) v.getTag();

            //TODO Delete all children transactions as well


            Log.d("mPosition", " " + id);

            Button deleteBtn = dialog.findViewById(R.id.Btn_delete);
            Button cancelBtn = dialog.findViewById(R.id.Btn_cancel);

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Uri transDeleteURi = ContentUris.withAppendedId(DatabaseContract.DatabaseEntry.TRANSACTIONS_URI, id);
                    Log.d("Delete:", " " + transDeleteURi);

                    int deletedTransactions = gContext.getContentResolver().delete(transDeleteURi, null, null);

                    if (deletedTransactions == 0) {
                        Toast.makeText(gContext, "No Transactions Deleted", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(gContext, deletedTransactions + " " + "Transactions Deleted", Toast.LENGTH_LONG).show();
                    }

                    Uri currentAmountUri = ContentUris.withAppendedId(DatabaseContract.DatabaseEntry.AMOUNTS_URI, id);

                    Log.d("currentAmountURi:", " " + currentAmountUri);
                    int affectedRows = gContext.getContentResolver().delete(currentAmountUri, null, null);


                    if (affectedRows == 0) {
                        // If the new content URI is null, then there was an error with insertion.
                        Toast.makeText(gContext, "Delete Amount failed", Toast.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();

                }
            });
        }
    };




}
