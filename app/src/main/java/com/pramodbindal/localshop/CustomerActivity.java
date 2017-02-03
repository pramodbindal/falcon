package com.pramodbindal.localshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pramodbindal.localshop.domain.Customer;

public class CustomerActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new CustomerAdapter(this, dbHelper.getAllCustomers()));
        listView.setOnItemClickListener(this);

        setTitle(R.string.customers);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ReportActivity.class);
        intent.putExtra("CUSTOMER", (Customer) parent.getAdapter().getItem(position));
        startActivity(intent);
    }
}
