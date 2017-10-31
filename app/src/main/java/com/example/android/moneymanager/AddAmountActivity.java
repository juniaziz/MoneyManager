package com.example.android.moneymanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class AddAmountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_amount);

        ListView amountListView = findViewById(R.id.add_amount_listview);
        View emptyView = findViewById(R.id.empty_view_2);

        amountListView.setEmptyView(emptyView);

    }
}
