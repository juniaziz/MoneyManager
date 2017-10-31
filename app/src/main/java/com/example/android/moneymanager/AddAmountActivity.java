package com.example.android.moneymanager;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moneymanager.data.Adapters.AmountsCursorAdapter;
import com.example.android.moneymanager.data.data.DatabaseContract;
import com.example.android.moneymanager.data.data.DatabaseContract.DatabaseEntry;
import java.text.SimpleDateFormat;

import static com.example.android.moneymanager.R.id.radioGroup;

public class AddAmountActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks <Cursor>{

    private static final int AMOUNTS_LOADER = 2;

    AmountsCursorAdapter mAmountsCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_amount);

        mAmountsCursorAdapter = new AmountsCursorAdapter(this, null);
        getSupportLoaderManager().initLoader(AMOUNTS_LOADER, null, this);

        ListView amountListView = findViewById(R.id.add_amount_listview);
        View emptyView = findViewById(R.id.empty_view_2);
        amountListView.setEmptyView(emptyView);
        amountListView.setAdapter(mAmountsCursorAdapter);



        FloatingActionButton floatingActionButton = findViewById(R.id.fab_add_amount);
        floatingActionButton.setOnClickListener(onfabClicked);


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                DatabaseContract.DatabaseEntry._ID,
                DatabaseContract.DatabaseEntry.COLUMN_AMOUNT,
                DatabaseContract.DatabaseEntry.COLUMN_CURRENCY,
                DatabaseContract.DatabaseEntry.COLUMN_TYPE,
                DatabaseContract.DatabaseEntry.COLUMN_DATE,
                DatabaseContract.DatabaseEntry.COLUMN_DESCRIPTION,};

        String selection = DatabaseContract.DatabaseEntry.COLUMN_TYPE + "=?";



        /*
        In case of the integer casting into String
         String[] selectionArgs = new String[] { String.valueOf(DatabaseEntry.TYPE_GOING)};
         */

        String[] selectionArgs = new String[] {DatabaseContract.DatabaseEntry.TYPE_GOING};

        return new CursorLoader(this, DatabaseEntry.AMOUNTS_URI, projection, null, null, DatabaseContract.DatabaseEntry._ID + " DESC");

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       mAmountsCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAmountsCursorAdapter.swapCursor(null);
    }


    View.OnClickListener onfabClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Dialog dialog = new Dialog(AddAmountActivity.this);
            dialog.setContentView(R.layout.dialog_trans);
            dialog.setTitle("Add an Expense");
            dialog.setCancelable(true);

            dialog.show();

            final TextView selectedDateTextView = dialog.findViewById(R.id.dateTextView);
            final EditText amountEditText = dialog.findViewById(R.id.amount_textfield);
            final EditText currencyEditText = dialog.findViewById(R.id.currency_textfield);
            final EditText descriptionEditText = dialog.findViewById(R.id.description_textfield);
            final RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);

            radioGroup.check(R.id.radioBtnComing);
            currencyEditText.setText("AED");
            descriptionEditText.setText("Ting Tong");


            long date = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = sdf.format(date);
            selectedDateTextView.setText(dateString);

            selectedDateTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    DialogFragment newFragment = new DatePickerFragment(selectedDateTextView);
                    newFragment.show(getSupportFragmentManager(), "datePicker");

                }

            });

            final Button addBtnDialog = dialog.findViewById(R.id.addBtn_dialog);

            addBtnDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final int selectId = radioGroup.getCheckedRadioButtonId();
                    final RadioButton radioButtonID = dialog.findViewById(selectId);

                    String amount = amountEditText.getText().toString().trim();
                    String currency = currencyEditText.getText().toString().trim();
                    String description = descriptionEditText.getText().toString().trim();
                    String radioButton = radioButtonID.getText().toString();
                    String selectedDate = selectedDateTextView.getText().toString().trim();

                    ContentValues values = new ContentValues();

                    values.put(DatabaseContract.DatabaseEntry.COLUMN_AMOUNT, amount);
                    values.put(DatabaseContract.DatabaseEntry.COLUMN_CURRENCY, currency);
                    values.put(DatabaseContract.DatabaseEntry.COLUMN_TYPE, radioButton);
                    values.put(DatabaseContract.DatabaseEntry.COLUMN_DATE, selectedDate);
                    values.put(DatabaseContract.DatabaseEntry.COLUMN_DESCRIPTION, description);

                    Log.d("Type from values: ", String.valueOf(values.getAsString(DatabaseEntry.COLUMN_TYPE)));

                    Uri newUri = getContentResolver().insert(DatabaseEntry.AMOUNTS_URI, values);
                    Uri newUri2 = getContentResolver().insert(DatabaseEntry.TRANSACTIONS_URI, values);

                    if (newUri == null) {
                        // If the new content URI is null, then there was an error with insertion.
                        Toast.makeText(AddAmountActivity.this, "Insert transaction failed", Toast.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();

                }
            });
        }
    };


}
