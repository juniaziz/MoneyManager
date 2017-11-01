package com.example.android.moneymanager;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import java.text.*;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.TestLooperManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moneymanager.data.data.DatabaseContract;
import com.example.android.moneymanager.data.data.DatabaseContract.DatabaseEntry;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    private static String parentAmount;
    private static String parentCurrency;
    private static String parentDate;
    private static String parentCurrentBalance;
    private static Uri parent_ID;
    private static final int EXISTING_PARENT_AMOUNT = 3;

    TextView parentAmountTxtView;
    TextView parentCurrencyTxtView;
    TextView parentDateTxtView;
    TextView parentCurrentBalanceTxtView;

    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(onfabClicked);

        FragmentHomeActivity fragmentHomeActivity = new FragmentHomeActivity();


//        parentAmount = getIntent().getStringExtra("parentAmount");
//        parentCurrency = getIntent().getStringExtra("parentCurrency");
//        parentDate = getIntent().getStringExtra("parentDate");
//        parentCurrentBalance = getIntent().getStringExtra("parentCurrentBalance");
        parent_ID = getIntent().getData();

        TextView parentAmountTxtView = findViewById(R.id.parentAmount);
        TextView parentCurrencyTxtView = findViewById(R.id.parentCurrency);
        TextView parentDateTxtView = findViewById(R.id.parentDate);
        TextView parentCurrentBalanceTxtView = findViewById(R.id.parentCurrentBalance);

        getSupportLoaderManager().initLoader(EXISTING_PARENT_AMOUNT, null, this);

       

        parentAmountTxtView.setText(parentAmount);
        parentCurrencyTxtView.setText(parentCurrency);
        parentDateTxtView.setText(parentDate);
        parentCurrentBalanceTxtView.setText(parentCurrentBalance);

        showFragment(fragmentHomeActivity);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                DatabaseEntry._ID,
                DatabaseEntry.COLUMN_AMOUNT,
                DatabaseEntry.COLUMN_CURRENCY,
                DatabaseEntry.COLUMN_DATE,
                DatabaseEntry.COLUMN_CURRENT_BALANCE};

        return new CursorLoader(this, parent_ID, projection, null, null, DatabaseEntry._ID + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (Cursor)
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    View.OnClickListener onfabClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_trans);
            dialog.setTitle("Add an Expense");
            dialog.setCancelable(true);

            dialog.show();

            final TextView selectedDateTextView = dialog.findViewById(R.id.dateTextView);
            final EditText amountEditText = dialog.findViewById(R.id.amount_textfield);
            final EditText currencyEditText = dialog.findViewById(R.id.currency_textfield);
            final EditText descriptionEditText = dialog.findViewById(R.id.description_textfield);

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

                    final RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
                    final int selectId = radioGroup.getCheckedRadioButtonId();
                    final RadioButton radioButtonID = dialog.findViewById(selectId);

                    String enteredAmount = amountEditText.getText().toString().trim();
                    String currency = parentCurrency;
                    String description = descriptionEditText.getText().toString().trim();
                    String radioButton = radioButtonID.getText().toString();
                    String selectedDate = selectedDateTextView.getText().toString().trim();

                    Double newBalance = Double.parseDouble(parentCurrentBalance) - Double.parseDouble(enteredAmount);

                    ContentValues newBalanceValue = new ContentValues();
                    newBalanceValue.put(DatabaseEntry.COLUMN_CURRENT_BALANCE, Double.toString(newBalance));

                    int rowsAffected = getContentResolver().update(parent_ID, newBalanceValue, null, null);

                    if (rowsAffected == 0) {
                        // If no rows were affected, then there was an error with the update.
                        Toast.makeText(MainActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the update was successful and we can display a toast.
                        Toast.makeText(MainActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                    }

                    ContentValues values = new ContentValues();

                    values.put(DatabaseContract.DatabaseEntry.COLUMN_AMOUNT, enteredAmount);
                    values.put(DatabaseContract.DatabaseEntry.COLUMN_CURRENCY, currency);
                    values.put(DatabaseContract.DatabaseEntry.COLUMN_TYPE, radioButton);
                    values.put(DatabaseContract.DatabaseEntry.COLUMN_DATE, selectedDate);
                    values.put(DatabaseContract.DatabaseEntry.COLUMN_DESCRIPTION, description);
                    values.put(DatabaseContract.DatabaseEntry.COLUMN_PARENT_AMOUNT, String.valueOf(parent_ID));

                    Log.d("Type from values: ", String.valueOf(values.getAsString(DatabaseContract.DatabaseEntry.COLUMN_TYPE)));

                    Uri newUri = getContentResolver().insert(DatabaseEntry.TRANSACTIONS_URI, values);

                    if (newUri == null) {
                        // If the new content URI is null, then there was an error with insertion.
                        Toast.makeText(MainActivity.this, "Insert transaction failed", Toast.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();

                }
            });
        }
    };


    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);

//        if (state) {
//            fragmentTransaction.addToBackStack(fragment.getClass().getName());
//        }
        fragmentTransaction.commit();
    }

    public void resetToHomeScreen(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


}
