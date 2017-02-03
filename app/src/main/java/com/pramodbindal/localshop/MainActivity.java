package com.pramodbindal.localshop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pramodbindal.localshop.domain.Customer;
import com.pramodbindal.localshop.domain.Transaction;
import com.pramodbindal.localshop.domain.TransactionType;
import com.pramodbindal.localshop.util.CommonUtils;
import com.pramodbindal.localshop.util.ViewUtils;
import com.pramodbindal.localshop.validator.ValidationHandler;

import java.util.Date;
import java.util.Map;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView dateField = (TextView) findViewById(R.id.date);
        dateField.setText(CommonUtils.getCurrentDate());
        final AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.name);
        CustomerAdapter adapter = new CustomerAdapter(this, dbHelper.getAllCustomers());
        textView.setAdapter(adapter);
        textView.setThreshold(1);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setTag(parent.getItemAtPosition(position));
                findViewById(R.id.amount).requestFocus();
            }
        });
        setTitle(R.string.new_transaction);

    }

    public void save(View v) {
        TextView customerBox = (TextView) findViewById(R.id.name);
        Customer customer = (Customer) customerBox.getTag();
        EditText amountBox = (EditText) findViewById(R.id.amount);
        EditText commentBox = (EditText) findViewById(R.id.comments);
        RadioGroup txnTypBox = (RadioGroup) findViewById(R.id.txn_type);

        String s = amountBox.getText().toString().trim();
        final double amount;
        if (s.isEmpty()) {
            amount = 0;
        } else {
            amount = Double.valueOf(s);
        }
        int checkedRadioButtonId = txnTypBox.getCheckedRadioButtonId();

        TransactionType txnType = checkedRadioButtonId == R.id.radio_credit ? TransactionType.CREDIT : TransactionType.DEBIT;

        Transaction transaction = new Transaction(customer, amount, txnType, new Date());
        transaction.setComment(commentBox.getText().toString());

        Map<String, String> errors = ValidationHandler.validate(transaction);
        if (errors.isEmpty()) {
            dbHelper.saveTransaction(transaction);
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setMessage(getString(R.string.record_saved)).show();
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (String error : errors.values()) {
                stringBuilder.append(error).append("\n");
            }
            ViewUtils.showErrorDialog(this, stringBuilder.toString());
        }

    }


}
