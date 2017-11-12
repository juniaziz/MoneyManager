package w.moneymanager;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import w.moneymanager.data.Adapters.TransactionsSwipeAdapter;
import w.moneymanager.data.data.DatabaseContract.DatabaseEntry;


/*
 * Created by mjfor on 10/22/2017.
 */

public class FragmentHomeActivity extends Fragment implements LoaderManager.LoaderCallbacks <Cursor> {


    View rootView;

    private static final int TRANSACTION_LOADER = 1;

    private static String PARENT_AMOUNT_ID;

    TransactionsSwipeAdapter mTransactionCursorAdapter;

    //Every fragment needs some methods to be used

    //FIRST ONE

    public FragmentHomeActivity(){

    }



    //SECOND ONE


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            PARENT_AMOUNT_ID = bundle.getString("CURRENT AMOUNT ID", "Invalid");
        }


        Log.d("Bundle URL onCreate: ", PARENT_AMOUNT_ID);

//        transactionsDbHelper = new DatabaseDbHelper(getContext());

        rootView = inflater.inflate(R.layout.fragment_home_activity, container, false);

        mTransactionCursorAdapter = new TransactionsSwipeAdapter(getContext(), null);

        getLoaderManager().initLoader(TRANSACTION_LOADER, null, this);



        ListView listView = rootView.findViewById(R.id.recycler_view_home_activity);
        View emptyView = rootView.findViewById(R.id.empty_view);

        listView.setEmptyView(emptyView);

        listView.setAdapter(mTransactionCursorAdapter);



        //Set any Text or images or like set up the whole fragment here.

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String onResumeURL = bundle.getString("CURRENT AMOUNT ID", "Invalid");
            Log.d("Bundle URL onResume: ", onResumeURL);
        }

    }

    //The above two methods are essential


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                DatabaseEntry._ID,
                DatabaseEntry.COLUMN_AMOUNT,
                DatabaseEntry.COLUMN_CURRENCY,
                DatabaseEntry.COLUMN_TYPE,
                DatabaseEntry.COLUMN_DATE,
                DatabaseEntry.COLUMN_DESCRIPTION,
                DatabaseEntry.COLUMN_PARENT_AMOUNT};

        String selection = DatabaseEntry.COLUMN_PARENT_AMOUNT + "=?";

        /*
        In case of the integer casting into String
         String[] selectionArgs = new String[] { String.valueOf(DatabaseEntry.TYPE_GOING)};
         */

        String[] selectionArgs = new String[] {PARENT_AMOUNT_ID};

        return new CursorLoader(getContext(), DatabaseEntry.TRANSACTIONS_URI, projection, selection, selectionArgs, DatabaseEntry._ID + " DESC");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTransactionCursorAdapter.swapCursor(null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mTransactionCursorAdapter.swapCursor(data);
    }


}


