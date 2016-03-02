package com.mb14.halfpenny.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.mb14.halfpenny.api.jsonblob.Expenses;
import com.mb14.halfpenny.api.jsonblob.JSONBlob;
import com.mb14.halfpenny.api.jsonblob.Transaction;
import com.mb14.halfpenny.R;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TransactionAdapter extends BaseAdapter{
    Context context;
    Expenses expenses;
    int category;
    DateFormat iso8601DateFormat;
    DateFormat standardDateFormat;
    OnTransactionUpdateListener onTransactionUpdateListener;

    public Expenses getExpenses() {
        return expenses;
    }


    private static class ViewHolder {
        TextView tvTransactionID;
        TextView tvTransactionDescription;
        TextView tvTransactionTimestamp;
        TextView tvTransactionAmount;
        TextView tvTransactionState;
        View sideBar;
        ImageView actionMore;
    }
    public interface OnTransactionUpdateListener{
         void onTransactionUpdate(Expenses expenses);
    }
    public TransactionAdapter(Context context, int category ,OnTransactionUpdateListener onUpdateListener){
        expenses = new Expenses();
        this.context = context;
        this.category = category;
        this.onTransactionUpdateListener = onUpdateListener;
        iso8601DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        standardDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
    }
    @Override
    public int getCount() {
        return expenses.getTransactions(category).size();
    }

    @Override
    public Transaction getItem(int position) {
        return expenses.getTransactions(category).get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Transaction transaction = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_transaction, parent, false);
            viewHolder.tvTransactionID = (TextView) convertView.findViewById(R.id.transactionID);
            viewHolder.tvTransactionDescription = (TextView) convertView.findViewById(R.id.transactionDescription);
            viewHolder.tvTransactionTimestamp = (TextView) convertView.findViewById(R.id.transactionTimestamp);
            viewHolder.tvTransactionState = (TextView) convertView.findViewById(R.id.transactionState);
            viewHolder.tvTransactionAmount = (TextView) convertView.findViewById(R.id.transactionAmount);
            viewHolder.sideBar = convertView.findViewById(R.id.sidebar);
            viewHolder.actionMore = (ImageView) convertView.findViewById(R.id.actionMore);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTransactionID.setText("Transaction " + transaction.getId());
        viewHolder.tvTransactionDescription.setText(transaction.getDescription());
        viewHolder.tvTransactionState.setText(transaction.getState().toUpperCase());
        viewHolder.tvTransactionAmount.setText("Rs. " + transaction.getAmount());
        try {
            Date timeStamp = iso8601DateFormat.parse(transaction.getTime());
            viewHolder.tvTransactionTimestamp.setText(standardDateFormat.format(timeStamp));
        } catch (ParseException e) {
            Log.e("com.mb14.halfpenny", e.getMessage());
            viewHolder.tvTransactionTimestamp.setText(transaction.getTime());
        }

        viewHolder.sideBar.setBackgroundResource(getStateColor(transaction.getState()));
        viewHolder.actionMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenu().add(Menu.NONE, 0, Menu.NONE, "Mark as verified");
                popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "Mark as unverified");
                popupMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, "Mark as fraudulent");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case 0:
                                expenses.getTransaction(transaction.getId()).setState("verified");
                                break;
                            case 1:
                                expenses.getTransaction(transaction.getId()).setState("unverified");
                                break;
                            case 2:
                                expenses.getTransaction(transaction.getId()).setState("fraudulent");
                                break;
                        }
                        onTransactionUpdateListener.onTransactionUpdate(expenses);
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        // Return the completed view to render on screen

        return convertView;

    }

    private int getStateColor(String state) {
        switch (state){
            case "verified":
                return R.color.green;
            case "unverified":
                return R.color.yellow;
            case "fraudulent":
                return R.color.red;
        }
        return R.color.yellow;
    }


    public void updateExpenses(Expenses result) {
        expenses = result;
        expenses.generateCategories();

        notifyDataSetChanged();
        Toast.makeText(context,"Expenses updated",Toast.LENGTH_SHORT).show();
    }

}
