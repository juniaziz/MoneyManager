package w.moneymanager;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;

import java.text.SimpleDateFormat;

import w.moneymanager.data.data.DatabaseContract.DatabaseEntry;
import w.moneymanager.data.Adapters.AddAmountSwipeAdapter;


public class AddAmountActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks <Cursor>{

    private static final int AMOUNTS_LOADER = 2;

    ListView amountListView;
    private AddAmountSwipeAdapter swipeAdapter;
    SwipeLayout swipeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_amount);

        amountListView = findViewById(R.id.add_amount_listview);
        View emptyView = findViewById(R.id.empty_view_2);
        amountListView.setEmptyView(emptyView);

        getSupportLoaderManager().initLoader(AMOUNTS_LOADER, null, this);

        swipeAdapter = new AddAmountSwipeAdapter(this, null);
        swipeAdapter.setMode(Attributes.Mode.Single);
        amountListView.setAdapter(swipeAdapter);
        amountListView.setOnItemClickListener(onItemSelected);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_add_amount);
        floatingActionButton.setOnClickListener(onfabClicked);

    }

    @Override
    protected void onResume() {
        super.onResume();

//        amountListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

//                Toast.makeText(AddAmountActivity.this, "OnItemLongClickListener", Toast.LENGTH_SHORT).show();
//
//            return true;
//            }
//        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                DatabaseEntry._ID,
                DatabaseEntry.COLUMN_AMOUNT,
                DatabaseEntry.COLUMN_CURRENCY,
                DatabaseEntry.COLUMN_TYPE,
                DatabaseEntry.COLUMN_DATE,
                DatabaseEntry.COLUMN_DESCRIPTION,
                DatabaseEntry.COLUMN_CURRENT_BALANCE};

        String selection = DatabaseEntry.COLUMN_TYPE + "=?";

        /*
        In case of the integer casting into String
         String[] selectionArgs = new String[] { String.valueOf(DatabaseEntry.TYPE_GOING)};
         */

        String[] selectionArgs = new String[] {DatabaseEntry.TYPE_GOING};

        return new CursorLoader(this, DatabaseEntry.AMOUNTS_URI, projection, null, null, DatabaseEntry._ID + " DESC");

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       swipeAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        swipeAdapter.swapCursor(null);
    }

    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            Log.e("ListView", "onScrollStateChanged");
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


        }
    };

    AdapterView.OnItemClickListener onItemSelected = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            swipeLayout = (SwipeLayout) view;

            if(swipeLayout.getOpenStatus() == SwipeLayout.Status.Close) {

                Intent intent = new Intent(AddAmountActivity.this, MainActivity.class);

                Uri currentAmountUri = ContentUris.withAppendedId(DatabaseEntry.AMOUNTS_URI, id);
                intent.setData(currentAmountUri);
                intent.putExtra("CURRENT ID", Long.toString(id));
                startActivity(intent);

                Toast.makeText(AddAmountActivity.this, swipeAdapter.getCursor()
                                .getString(swipeAdapter.getCursor().getColumnIndexOrThrow(DatabaseEntry.COLUMN_DESCRIPTION)),
                        Toast.LENGTH_LONG).show();
            }
        }
    };


    View.OnClickListener onfabClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Dialog dialog = new Dialog(AddAmountActivity.this);
            dialog.setContentView(R.layout.dialog_trans);
            dialog.setTitle("Add an Amount");
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
                    String selectedDate = selectedDateTextView.getText().toString().trim();
                    String radioButton = radioButtonID.getText().toString();

                    if (description.isEmpty()){
                        Toast.makeText(AddAmountActivity.this, "Description cannot be empty", Toast.LENGTH_LONG).show();
                    }
                    else if (currency.isEmpty()) {
                        Toast.makeText(AddAmountActivity.this, "currency cannot be empty", Toast.LENGTH_LONG).show();
                    }
                    else if (amount.isEmpty() || description.isEmpty() || currency.isEmpty()) {
                        Toast.makeText(AddAmountActivity.this, "Amount cannot be empty", Toast.LENGTH_LONG).show();
                    }
                    else {
                        ContentValues values = new ContentValues();
                        values.put(DatabaseEntry.COLUMN_AMOUNT, amount);
                        values.put(DatabaseEntry.COLUMN_CURRENCY, currency);
                        values.put(DatabaseEntry.COLUMN_TYPE, radioButton);
                        values.put(DatabaseEntry.COLUMN_DATE, selectedDate);
                        values.put(DatabaseEntry.COLUMN_DESCRIPTION, description);
                        values.put(DatabaseEntry.COLUMN_CURRENT_BALANCE, amount);

                        Log.d("Type from values: ", String.valueOf(values.getAsString(DatabaseEntry.COLUMN_TYPE)));
                        Uri newUri = getContentResolver().insert(DatabaseEntry.AMOUNTS_URI, values);
                        if (newUri == null) {
                            // If the new content URI is null, then there was an error with insertion.
                            Toast.makeText(AddAmountActivity.this, "Insert transaction failed", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                }
            });
        }
    };
}
