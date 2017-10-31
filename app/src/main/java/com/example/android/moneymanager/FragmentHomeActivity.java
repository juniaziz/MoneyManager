package com.example.android.moneymanager;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.moneymanager.data.data.DatabaseContract;
import com.example.android.moneymanager.data.Adapters.TransactionsCursorAdapter;


/*
 * Created by mjfor on 10/22/2017.
 */

public class FragmentHomeActivity extends Fragment implements LoaderManager.LoaderCallbacks <Cursor> {


    View rootView;
    MainActivity homeActivity;

    private static final int TRANSACTION_LOADER = 1;

    TransactionsCursorAdapter mTransactionCursorAdapter;

    //Every fragment needs some methods to be used

    //FIRST ONE

    public FragmentHomeActivity(){

    }

    //SECOND ONE


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        transactionsDbHelper = new DatabaseDbHelper(getContext());

        rootView = inflater.inflate(R.layout.fragment_home_activity, container, false);

        mTransactionCursorAdapter = new TransactionsCursorAdapter(getContext(), null);

        getLoaderManager().initLoader(TRANSACTION_LOADER, null, this);

        //   displayData();

        ListView listView = rootView.findViewById(R.id.recycler_view_home_activity);
        View emptyView = rootView.findViewById(R.id.empty_view);

        listView.setEmptyView(emptyView);

        listView.setAdapter(mTransactionCursorAdapter);



        //Set any Text or images or like set up the whole fragment here.

        return rootView;
    }

    //The above two methods are essential


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                DatabaseContract.DatabaseEntry._ID,
                DatabaseContract.DatabaseEntry.COLUMN_AMOUNT,
                DatabaseContract.DatabaseEntry.COLUMN_CURRENCY,
                DatabaseContract.DatabaseEntry.COLUMN_TYPE,
                DatabaseContract.DatabaseEntry.COLUMN_DATE,
                DatabaseContract.DatabaseEntry.COLUMN_DESCRIPTION,
                DatabaseContract.DatabaseEntry.COLUMN_PARENT_AMOUNT};

        String selection = DatabaseContract.DatabaseEntry.COLUMN_TYPE + "=?";



        /*
        In case of the integer casting into String
         String[] selectionArgs = new String[] { String.valueOf(DatabaseEntry.TYPE_GOING)};
         */

        String[] selectionArgs = new String[] {DatabaseContract.DatabaseEntry.TYPE_GOING};

        return new CursorLoader(getContext(), DatabaseContract.DatabaseEntry.TRANSACTIONS_URI, projection, null, null, DatabaseContract.DatabaseEntry._ID + " DESC");
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


