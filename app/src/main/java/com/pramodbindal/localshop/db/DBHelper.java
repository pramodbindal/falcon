package com.pramodbindal.localshop.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.LongSparseArray;

import com.pramodbindal.localshop.R;
import com.pramodbindal.localshop.domain.Customer;
import com.pramodbindal.localshop.domain.Transaction;
import com.pramodbindal.localshop.domain.TransactionType;
import com.pramodbindal.localshop.util.CommonUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pramodbindal.localshop.util.CommonConstants.TAG;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MBSG";
    private final Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 9);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating Database");
        executeSQLFromFile(db, R.raw.full_build);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Updating Database");
        if (oldVersion < 6) {
            executeSQLFromFile(db, R.raw.version_6);
        }
        if (oldVersion < 9) {
            executeSQLFromFile(db, R.raw.version_7);
        }

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private void executeSQLFromFile(SQLiteDatabase db, int resId) {
        String sqlString = CommonUtils.readRawTextFile(context, resId);

        for (String sql : sqlString.split(";")) {
            try {
                Log.d("MBSG", "Executing : " + sql);
                db.execSQL(sql);
            } catch (Exception e) {
                Log.e(TAG, "Error while executing " + sql, e);
            }
        }

    }


    public void saveCustomer(Customer customer) {
        Log.d("SAVE_CUSTOMER", "Saving Customer");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", customer.getName());
        contentValues.put("MOBILE", customer.getMobile());
        contentValues.put("IS_DELETED", 0);
        db.insert("CUSTOMER", null, contentValues);

        Log.d("SAVE_TRANSACTION", "Saved transaction");

        db.close();
    }


    public void saveTransaction(Transaction transaction) {
        Log.d("SAVE_TRANSACTION", "Saving transaction");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CUSTOMER_ID", transaction.getCustomer().getId());
        contentValues.put("AMOUNT", transaction.getAmount());
        contentValues.put("COMMENT", transaction.getComment());
        contentValues.put("TXN_DATE", transaction.getDate().getTime());
        contentValues.put("IS_DELETED", 0);

        contentValues.put("TRANSACTION_TYPE", transaction.getTransactionType() == null ? TransactionType.DEBIT.value() : transaction.getTransactionType().value());
        db.insert("TXN", null, contentValues);
        Log.d("SAVE_TRANSACTION", "Saved transaction");
        db.close();
    }


    public void deleteTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("IS_DELETED", 1);
        db.update("TXN", contentValues, "ID=?", new String[]{String.valueOf(transaction.getId())});
    }


    public List<Customer> getAllCustomers() {
        List<Customer> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from CUSTOMER  WHERE IS_DELETED=0 ORDER BY NAME", null);
        while (res.moveToNext()) {
            Long custId = res.getLong(res.getColumnIndex("ID"));
            String mobile = res.getString(res.getColumnIndex("MOBILE"));
            String customerName = res.getString(res.getColumnIndex("NAME"));
            Customer customer = new Customer();
            customer.setId(custId);
            customer.setName(customerName);
            customer.setMobile(mobile);
            array_list.add(customer);
        }
        return array_list;
    }


    public List<Transaction> getTransactionsForCustomer(Customer customer) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT T.*, C.NAME , C.MOBILE  FROM TXN T, CUSTOMER C WHERE T.CUSTOMER_ID=C.ID  AND T.IS_DELETED=0 AND C.ID = ? ", new String[]{String.valueOf(customer.getId())});
        return getTransactions(res);
    }

    public List<Transaction> getAllTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT T.*, C.NAME , C.MOBILE  FROM TXN T, CUSTOMER C WHERE T.CUSTOMER_ID=C.ID AND T.IS_DELETED=0", null);
        return getTransactions(res);
    }


    public String readAttribute(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT VALUE FROM ATTRIBUTE WHERE NAME = ?", new String[]{name});

        String value;
        if (res.moveToNext()) {
            value = res.getString(0);
        } else {
            value = null;
        }
        res.close();
        return value;
    }

    public void saveAttribute(String name, String value) {
        //ATTRIBUTE
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("VALUE", value);

        db.insertWithOnConflict("ATTRIBUTE", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private List<Transaction> getTransactions(Cursor res) {
        List<Transaction> array_list = new ArrayList<>();
        LongSparseArray<Customer> customerMap = new LongSparseArray<>();
        while (res.moveToNext()) {
            long txnId = res.getLong(res.getColumnIndex("ID"));
            Long custId = res.getLong(res.getColumnIndex("CUSTOMER_ID"));
            Long txnDate = res.getLong(res.getColumnIndex("TXN_DATE"));
            String comment = res.getString(res.getColumnIndex("COMMENT"));
            double amount = res.getDouble(res.getColumnIndex("AMOUNT"));

            String transactionType = res.getString(res.getColumnIndex("TRANSACTION_TYPE"));
            String mobile = res.getString(res.getColumnIndex("MOBILE"));
            String customerName = res.getString(res.getColumnIndex("NAME"));


            if (customerMap.get(custId) == null) {
                Customer customer = new Customer();
                customer.setId(custId);
                customer.setName(customerName);
                customer.setMobile(mobile);
                customerMap.put(custId, customer);
            }
            TransactionType transactionType2 = TransactionType.fromValue(transactionType);
            Transaction transaction = new Transaction(customerMap.get(custId), amount, transactionType2, new Date(txnDate));
            transaction.setId(txnId);
            transaction.setComment(comment);
            array_list.add(transaction);
        }
        System.out.println("array_list = " + array_list);
        return array_list;

    }
}