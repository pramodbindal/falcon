package com.pramodbindal.localshop;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pramodbindal.localshop.domain.Transaction;
import com.pramodbindal.localshop.domain.TransactionType;
import com.pramodbindal.localshop.util.CommonUtils;

import java.util.List;


class TransactionAdapter extends ArrayAdapter<Transaction> {

    private List<Transaction> data;

    public TransactionAdapter(Context mContext, List<Transaction> data) {
        super(mContext, R.layout.txn_list_item, data);
        this.data = data;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        Transaction item = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.txn_list_item, parent, false);
            viewHolder.customerName = (TextView) convertView.findViewById(R.id.cust_name);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.amount);
            viewHolder.transactionDate = (TextView) convertView.findViewById(R.id.txn_dt);
            viewHolder.txnTyp = (TextView) convertView.findViewById(R.id.txn_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (item != null) {
            TransactionType type = item.getTransactionType();
            viewHolder.customerName.setText(item.getCustomer().getName() + "##" + item.getComment());
            viewHolder.amount.setText(CommonUtils.formatAmount(item.getAmount()));
            viewHolder.transactionDate.setText(CommonUtils.formatDate(item.getDate()));
            viewHolder.txnTyp.setText(type.value());
            if (type == TransactionType.DEBIT) {
                viewHolder.txnTyp.setTextColor(Color.RED);
            } else if (type == TransactionType.CREDIT) {
                viewHolder.txnTyp.setTextColor(Color.GREEN);
            } else {
                viewHolder.txnTyp.setTextColor(Color.DKGRAY);
            }
        }
        return convertView;
    }

    public double getTotal() {
        double total = 0;
        for (int i = 0; i < getCount(); i++) {
            Transaction transaction = getItem(i);
            if (transaction.getTransactionType() == TransactionType.CREDIT) {
                total += transaction.getAmount();
            } else if (transaction.getTransactionType() == TransactionType.DEBIT) {
                total -= transaction.getAmount();
            }
        }
        return total;
    }

    @Override
    public void remove(Transaction object) {
        super.remove(object);
        notifyDataSetChanged();
    }

    // View lookup cache
    private static class ViewHolder {
        TextView transactionDate;
        TextView customerName;
        TextView amount;
        TextView txnTyp;

    }
}

