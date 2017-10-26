package com.example.android.moneymanager;


import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;






/*
 * Created by mjfor on 10/22/2017.
 */

public class FragmentHomeActivity extends Fragment{


    //Every fragment needs some methods to be used

    //FIRST ONE

    public FragmentHomeActivity(){

    }


    private TransactionsDbHelper transactionsDbHelper;

    //SECOND ONE


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


//        transactionsDbHelper = new TransactionsDbHelper(getContext());

        View rootView = inflater.inflate(R.layout.transaction_item, container, false);



       //Set any Text or images or like set up the whole fragment here.

        return rootView;
    }


    //The above two methods are essential


}


