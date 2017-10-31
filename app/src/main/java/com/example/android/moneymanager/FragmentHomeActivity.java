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
import com.example.android.moneymanager.data.transactions.TransactionsContract.TransactionEntry;
import com.example.android.moneymanager.data.transactions.TransactionsCursorAdapter;


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


//        transactionsDbHelper = new TransactionsDbHelper(getContext());

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
                TransactionEntry._ID,
                TransactionEntry.COLUMN_AMOUNT,
                TransactionEntry.COLUMN_CURRENCY,
                TransactionEntry.COLUMN_TYPE,
                TransactionEntry.COLUMN_DATE,
                TransactionEntry.COLUMN_DESCRIPTION,
                TransactionEntry.COLUMN_PARENT_AMOUNT};

        String selection = TransactionEntry.COLUMN_TYPE + "=?";



        /*
        In case of the integer casting into String
         String[] selectionArgs = new String[] { String.valueOf(TransactionEntry.TYPE_GOING)};
         */

        String[] selectionArgs = new String[] {TransactionEntry.TYPE_GOING};

        return new CursorLoader(getContext(), TransactionEntry.CONTENT_URI, projection, null, null, TransactionEntry._ID + " DESC");
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


