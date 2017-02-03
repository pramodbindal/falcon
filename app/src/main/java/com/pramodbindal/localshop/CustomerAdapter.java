package com.pramodbindal.localshop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.pramodbindal.localshop.domain.Customer;

import java.util.ArrayList;
import java.util.List;


class CustomerAdapter extends ArrayAdapter<Customer> {
    private final List<Customer> allCustomers;
    private final List<Customer> mCustomers;

    private final Context mContext;

    public CustomerAdapter(Context mContext, List<Customer> customers) {
        super(mContext, R.layout.cust_list_item, customers);
        this.allCustomers = new ArrayList<>(customers);
        this.mCustomers = new ArrayList<>(customers);
        this.mContext = mContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Customer customer = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.cust_list_item, parent, false);
            viewHolder.customerName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.mobile = (TextView) convertView.findViewById(R.id.mobile);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.customerName.setText(customer.getName());
        viewHolder.mobile.setText(customer.getMobile());

        return convertView;
    }

    @Override
    public int getCount() {
        return mCustomers.size();
    }

    @Nullable
    @Override
    public Customer getItem(int position) {
        return mCustomers.get(position);
    }

    @NonNull
    public Filter getFilter() {
        return new Filter() {

            @Override
            public String convertResultToString(Object resultValue) {
                return ((Customer) resultValue).getName();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    final List<Customer> suggestions = new ArrayList<>();
                    for (Customer customer : allCustomers) {
                        if (customer.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            suggestions.add(customer);
                        }
                    }
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mCustomers.clear();
                if (results != null && results.count > 0) {
                    List<?> result = (List<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof Customer) {
                            mCustomers.add((Customer) object);
                        }
                    }
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    mCustomers.addAll(allCustomers);
                }
                notifyDataSetChanged();
            }
        };
    }

    // View lookup cache
    private static class ViewHolder {
        TextView customerName;
        TextView mobile;
    }
}
