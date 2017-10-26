package com.example.android.moneymanager;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moneymanager.TransactionsContract.TransactionEntry;


public class MainActivity extends AppCompatActivity {


    private TransactionsDbHelper transactionsDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(onfabClicked);

        FragmentHomeActivity fragmentHomeActivity = new FragmentHomeActivity();

        showFragment(fragmentHomeActivity);

        transactionsDbHelper = new TransactionsDbHelper(this);
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

                    EditText amountEditText = dialog.findViewById(R.id.amount_textfield);
                    EditText currencyEditText = dialog.findViewById(R.id.currency_textfield);
                    EditText descriptionEditText = dialog.findViewById(R.id.description_textfield);
                    RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
                    int selectId = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButtonID = dialog.findViewById(selectId);

                    String amount = amountEditText.getText().toString().trim();
                    String currency = currencyEditText.getText().toString().trim();
                    String description = descriptionEditText.getText().toString().trim();
                    String radioButton = radioButtonID.getText().toString();
                    String selectedDate = selectedDateTextView.getText().toString().trim();

                    ContentValues values = new ContentValues();

                    values.put(TransactionEntry.COLUMN_AMOUNT, amount);
                    values.put(TransactionEntry.COLUMN_CURRENCY, currency);
                    values.put(TransactionEntry.COLUMN_TYPE, radioButton);
                    values.put(TransactionEntry.COLUMN_DATE, selectedDate);
                    values.put(TransactionEntry.COLUMN_DESCRIPTION, description);
                    values.put(TransactionEntry.COLUMN_PARENT_AMOUNT, "Ammi K Paisay");

                    SQLiteDatabase transactionsDb = transactionsDbHelper.getWritableDatabase();

                    long newTransactionId = transactionsDb.insert(TransactionEntry.TABLE_NAME, null, values);

                    System.out.print("newTransactionId: " + newTransactionId);

                    dialog.dismiss();

                    Toast.makeText(MainActivity.this, "Saved Successfully Transaction ID: " + newTransactionId, Toast.LENGTH_LONG).show();

                    displayData();

                }
            });
        }
    };


    private void displayData(){


        SQLiteDatabase transactionDb = transactionsDbHelper.getReadableDatabase();

        String[] projection = {
                TransactionEntry._ID,
                TransactionEntry.COLUMN_AMOUNT,
                TransactionEntry.COLUMN_CURRENCY,
                TransactionEntry.COLUMN_TYPE,
                TransactionEntry.COLUMN_DATE,
                TransactionEntry.COLUMN_DESCRIPTION,
                TransactionEntry.COLUMN_PARENT_AMOUNT};

        String selection = TransactionEntry.COLUMN_TYPE + "=?";

        //Hello changes made to commit

        /*
        In case of the integer casting into String
         String[] selectionArgs = new String[] { String.valueOf(TransactionEntry.TYPE_GOING)};
         */

        String[] selectionArgs = new String[] {TransactionEntry.TYPE_GOING};

        Cursor cursor = transactionDb.query(TransactionEntry.TABLE_NAME, projection, null,null,null,null,null);


        try {

            TextView display_textview = findViewById(R.id.database_output);

            display_textview.setText("Number of rows returned is: " + cursor.getCount() + "\n\n");


            int idColumnIndex = cursor.getColumnIndex(TransactionEntry._ID);
            int amountColumnIndex = cursor.getColumnIndex(TransactionEntry.COLUMN_AMOUNT);
            int currencyColumnIndex = cursor.getColumnIndex(TransactionEntry.COLUMN_CURRENCY);
            int typeColumnIndex = cursor.getColumnIndex(TransactionEntry.COLUMN_TYPE);
            int dateColumnIndex = cursor.getColumnIndex(TransactionEntry.COLUMN_DATE);
            int descriptionColumnIndex = cursor.getColumnIndex(TransactionEntry.COLUMN_DESCRIPTION);
            int parentAmountColumnIndex = cursor.getColumnIndex(TransactionEntry.COLUMN_PARENT_AMOUNT);

            Log.d("column index: ", " " + idColumnIndex
                    + amountColumnIndex
                    + currencyColumnIndex
                    + typeColumnIndex
                    + dateColumnIndex
                    + descriptionColumnIndex
                    + parentAmountColumnIndex + " ");

            while (cursor.moveToNext()) {

                int currentID = cursor.getInt(idColumnIndex);
                String currentAmount = cursor.getString(amountColumnIndex);
                String currentCurrency = cursor.getString(currencyColumnIndex);
                String currentType = cursor.getString(typeColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);
                String currentDescription = cursor.getString(descriptionColumnIndex);
                String currentParentAmount = cursor.getString(parentAmountColumnIndex);

                Log.d("Row Index: ", " "
                        + currentID + " "
                        + currentAmount + " "
                        + currentCurrency + " "
                        + currentType + " "
                        + currentDate + " "
                        + currentDescription + " "
                        + currentParentAmount + " ");

                display_textview.append(("\n"
                        + currentID + "-"
                        + currentAmount + "-"
                        + currentCurrency + "-"
                        + currentType + "-"
                        + currentDate + "-"
                        + currentDescription + "-"
                        + currentParentAmount));
            }

        } finally {
            cursor.close();
        }



    }



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
