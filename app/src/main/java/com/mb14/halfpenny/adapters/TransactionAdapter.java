package com.mb14.halfpenny.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

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
    ProgressDialog progressDialog;
    DateFormat iso8601DateFormat;
    DateFormat standardDateFormat;

    private static class ViewHolder {
        TextView tvTransactionID;
        TextView tvTransactionDescription;
        TextView tvTransactionTimestamp;
        TextView tvTransactionAmount;
        TextView tvTransactionState;
        View sideBar;
        ImageView actionMore;
    }

    public TransactionAdapter(Context context){
        expenses = new Expenses();
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        iso8601DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        standardDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
        refreshExpenses();
    }
    @Override
    public int getCount() {
        return expenses.getTransactions().size();
    }

    @Override
    public Transaction getItem(int position) {
        return expenses.getTransactions().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
                                expenses.getTransactions().get(position).setState("verified");
                                break;
                            case 1:
                                expenses.getTransactions().get(position).setState("unverified");
                                break;
                            case 2:
                                expenses.getTransactions().get(position).setState("fraudulent");
                                break;
                        }
                        updateExpenses();
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

    private void updateExpenses() {
        Call<Expenses> call = JSONBlob.getService().updateExpenses(expenses);
        progressDialog.setTitle("Updating expenses...");
        progressDialog.show();
        call.enqueue(new Callback<Expenses>() {
            @Override
            public void onResponse(Response<Expenses> response) {
                if(response.isSuccess()) {
                    expenses = response.body();
                    notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void refreshExpenses(){
        Call<Expenses> call = JSONBlob.getService().getExpenses();
        progressDialog.setTitle("Fetching expenses...");
        progressDialog.show();
        call.enqueue(new Callback<Expenses>() {
            @Override
            public void onResponse(Response<Expenses> response) {
                if(response.isSuccess()){
                    expenses = response.body();
                    notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
