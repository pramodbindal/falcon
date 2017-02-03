package com.pramodbindal.localshop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pramodbindal.localshop.domain.Customer;
import com.pramodbindal.localshop.domain.Transaction;
import com.pramodbindal.localshop.util.CommonUtils;

import java.util.List;

public class ReportActivity extends BaseActivity implements AdapterView.OnItemLongClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Customer customer = (Customer) getIntent().getSerializableExtra("CUSTOMER");
        final AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.cust_name);
        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemLongClickListener(this);

        CustomerAdapter adapter = new CustomerAdapter(this, dbHelper.getAllCustomers());
        textView.setAdapter(adapter);
        textView.setThreshold(1);
        textView.requestFocus();
        listView.setVisibility(View.GONE);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Customer customer = (Customer) parent.getItemAtPosition(position);
                showTransactionsForCustomer(customer, listView);
            }
        });

        if (customer != null) {
            textView.setText(customer.getName());
            showTransactionsForCustomer(customer, listView);
            listView.requestFocus();
        }

        setTitle(R.string.report);
    }

    private void showTransactionsForCustomer(Customer customer, ListView listView) {
        List<Transaction> transactionsForCustomer = dbHelper.getTransactionsForCustomer(customer);
        final TransactionAdapter adapter = new TransactionAdapter(ReportActivity.this, transactionsForCustomer);
        listView.setAdapter(adapter);
        listView.setVisibility(View.VISIBLE);
        final View footerView = ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.report_txn_footer, null, false);
        listView.addFooterView(footerView);

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                ((TextView) footerView.findViewById(R.id.amount)).setText(CommonUtils.formatAmount(adapter.getTotal()));
            }
        });
        ((TextView) footerView.findViewById(R.id.amount)).setText(CommonUtils.formatAmount(adapter.getTotal()));
    }


    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Transaction transaction = (Transaction) parent.getItemAtPosition(position);
        builder
                .setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.confirm_delete))
                .setIcon(android.R.drawable.ic_input_delete)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Object adapter = parent.getAdapter();
                        TransactionAdapter wrappedAdapter = (TransactionAdapter) ((HeaderViewListAdapter) adapter).getWrappedAdapter();
                        wrappedAdapter.remove(transaction);
                        dbHelper.deleteTransaction(transaction);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
        return false;
    }
}
