package com.pramodbindal.localshop;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.pramodbindal.localshop.domain.Customer;

import java.util.Locale;

public class DashboardActivity extends BaseActivity {

    private static final int PICK_CONTACT = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Locale.setDefault(new Locale("hi", "IN"));
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

//        Intent intent = new Intent(this, DataSyncService.class);
//        intent.setAction(DataSyncService.INTENT_ACTION);
//        startService(intent);

        scheduleAlarm();
    }


    public void scheduleAlarm() {

        MyAlarmReceiver.restartPeriodicTaskHeartBeat(this);

//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//        intent.setAction(MyAlarmReceiver.INTENT_ACTION);
//        sendBroadcast(intent);
//
//
//        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
//        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        long firstMillis = System.currentTimeMillis(); // alarm is set right away
//        AlarmManager alarm = (AlarmManager) this.getSystemService(ALARM_SERVICE);
//        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, 5000, pIntent);
    }


    public void viewReport(View view) {
        Intent intent = new Intent(this, ReportActivity.class);
        startActivity(intent);
    }

    public void addNew(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void addUser(View view) {
        Intent intent = new Intent(this, NewCustomerActivity.class);
        startActivity(intent);

    }

    public void readContact(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    public void customers(View v) {
        startActivity(new Intent(this, CustomerActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor phones = managedQuery(contactData, null, null, null, null);
                    if (phones.moveToFirst()) {
                        String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        int columnIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String phoneNumber = phones.getString(columnIndex);
                        Customer customer = new Customer();
                        customer.setName(name);
                        customer.setMobile(phoneNumber);
                        dbHelper.saveCustomer(customer);
                    }
                }
                break;
        }
    }
}

