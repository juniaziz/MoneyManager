package com.example.android.moneymanager;

import android.app.Dialog;
import android.content.ContentValues;
import java.text.*;
import android.net.Uri;
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
import com.example.android.moneymanager.data.transactions.TransactionsContract.TransactionEntry;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(onfabClicked);

        FragmentHomeActivity fragmentHomeActivity = new FragmentHomeActivity();

        showFragment(fragmentHomeActivity);

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

                    Log.d("Type from values: ", String.valueOf(values.getAsString(TransactionEntry.COLUMN_TYPE)));

                    Uri newUri = getContentResolver().insert(TransactionEntry.CONTENT_URI, values);

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
