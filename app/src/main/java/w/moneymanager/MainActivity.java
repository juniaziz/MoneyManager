package w.moneymanager;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import java.math.RoundingMode;
import w.moneymanager.data.data.DatabaseContract.DatabaseEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    FragmentHomeActivity fragmentHomeActivity;

    private static String parentAmount;
    private static String parentCurrency;
    private static String parentDate;
    private static String parentCurrentBalance;
    private static String parent_ID;
    private static Uri parent_ID_URL;
    private static final int EXISTING_PARENT_AMOUNT = 3;
    private static final double NEW_BALANCE_INITIALIZER = 10000000000000000.0;

    TextView parentAmountTxtView;
    TextView parentCurrencyTxtView;
    TextView parentDateTxtView;
    TextView parentCurrentBalanceTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parent_ID_URL = getIntent().getData();
        parent_ID = getIntent().getStringExtra("CURRENT ID");



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(onfabClicked);

        fragmentHomeActivity = new FragmentHomeActivity();
        Bundle bundle = new Bundle();
        bundle.putString("CURRENT AMOUNT ID", parent_ID);
        fragmentHomeActivity.setArguments(bundle);

        Log.d("Bundle URL sent", bundle.toString());


//        parentAmount = getIntent().getStringExtra("parentAmount");
//        parentCurrency = getIntent().getStringExtra("parentCurrency");
//        parentDate = getIntent().getStringExtra("parentDate");
//        parentCurrentBalance = getIntent().getStringExtra("parentCurrentBalance");


        parentAmountTxtView = findViewById(R.id.parentAmount);
        parentCurrencyTxtView = findViewById(R.id.parentCurrency);
        parentDateTxtView = findViewById(R.id.parentDate);
        parentCurrentBalanceTxtView = findViewById(R.id.parentCurrentBalance);

        getSupportLoaderManager().initLoader(EXISTING_PARENT_AMOUNT, null, this);


        showFragment(fragmentHomeActivity);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (parent_ID_URL == null) {
            return null;
        }

        String[] projection = {
                DatabaseEntry._ID,
                DatabaseEntry.COLUMN_AMOUNT,
                DatabaseEntry.COLUMN_CURRENCY,
                DatabaseEntry.COLUMN_DATE,
                DatabaseEntry.COLUMN_CURRENT_BALANCE,
                DatabaseEntry.COLUMN_DESCRIPTION};

        return new CursorLoader(this, parent_ID_URL, projection, null, null, DatabaseEntry._ID + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1){
            return;
        }
        if (cursor.moveToFirst()){

            Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
            parentAmount = (cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_DESCRIPTION)));
            parentCurrency = (cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_CURRENCY)));
            parentDate = (cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_DATE)));
            parentCurrentBalance = (cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_CURRENT_BALANCE)));

            double currentBalance = Double.parseDouble(parentCurrentBalance);
            currentBalance = round(currentBalance, 2);

            parentAmountTxtView.setText(parentAmount);
            parentCurrencyTxtView.setText(parentCurrency);
            parentDateTxtView.setText(parentDate);
            parentCurrentBalanceTxtView.setText(Double.toString(currentBalance));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        parentAmountTxtView.setText(" ");
        parentCurrencyTxtView.setText(" ");
        parentDateTxtView.setText(" ");
        parentCurrentBalanceTxtView.setText(" ");
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


            currencyEditText.setEnabled(false);
            currencyEditText.setText(parentCurrency);

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

                    if (description.isEmpty()){
                        Toast.makeText(MainActivity.this, "Description cannot be empty", Toast.LENGTH_LONG).show();
                    }
                    else if (currency.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Currency cannot be empty", Toast.LENGTH_LONG).show();
                    }
                    else if (enteredAmount.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Amount cannot be empty", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Double newBalance = NEW_BALANCE_INITIALIZER;

                        if (radioButton.equalsIgnoreCase("Going")) {
                            newBalance = Double.parseDouble(parentCurrentBalance) - Double.parseDouble(enteredAmount);
                        } else if (radioButton.equalsIgnoreCase("Coming")) {
                            newBalance = Double.parseDouble(parentCurrentBalance) + Double.parseDouble(enteredAmount);
                        }

                        if (newBalance != NEW_BALANCE_INITIALIZER) {
                            ContentValues newBalanceValue = new ContentValues();
                            newBalanceValue.put(DatabaseEntry.COLUMN_CURRENT_BALANCE, Double.toString(newBalance));

                            int rowsAffected = getContentResolver().update(parent_ID_URL, newBalanceValue, null, null);
                            if (rowsAffected == 0) {
                                // If no rows were affected, then there was an error with the update.
                                Toast.makeText(MainActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                            } else {
                                // Otherwise, the update was successful and we can display a toast.
                                Toast.makeText(MainActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                            }

                            ContentValues values = new ContentValues();

                            values.put(DatabaseEntry.COLUMN_AMOUNT, enteredAmount);
                            values.put(DatabaseEntry.COLUMN_CURRENCY, currency);
                            values.put(DatabaseEntry.COLUMN_TYPE, radioButton);
                            values.put(DatabaseEntry.COLUMN_DATE, selectedDate);
                            values.put(DatabaseEntry.COLUMN_DESCRIPTION, description);
                            values.put(DatabaseEntry.COLUMN_PARENT_AMOUNT, parent_ID);

                            Log.d("Type from values: ", String.valueOf(values.getAsString(DatabaseEntry.COLUMN_TYPE)));
                            Uri newUri = getContentResolver().insert(DatabaseEntry.TRANSACTIONS_URI, values);
                            if (newUri == null) {
                                // If the new content URI is null, then there was an error with insertion.
                                Toast.makeText(MainActivity.this, "Insert transaction failed", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this, "Calculation error", Toast.LENGTH_LONG).show();
                        }
                    }
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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void resetToHomeScreen(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


}
