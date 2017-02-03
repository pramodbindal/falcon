package com.pramodbindal.localshop;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.pramodbindal.localshop.domain.Customer;
import com.pramodbindal.localshop.util.ViewUtils;
import com.pramodbindal.localshop.validator.ValidationHandler;

import java.util.Collection;

public class NewCustomerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        findViewById(R.id.name).requestFocus();
        setTitle(R.string.new_customer);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    public void save(View view) {
        Customer customer = new Customer();
        EditText nameBox = (EditText) findViewById(R.id.name);
        EditText mobileBox = (EditText) findViewById(R.id.mobile);
        customer.setName(nameBox.getText().toString());
        customer.setMobile(mobileBox.getText().toString());
        Collection<String> errors = ValidationHandler.validate(customer).values();
        if (errors.isEmpty()) {
            dbHelper.saveCustomer(customer);
            finish();
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (String error : errors) {
                stringBuilder.append(error).append("\n");
            }
            ViewUtils.showErrorDialog(this, stringBuilder.toString());
        }
    }
}
