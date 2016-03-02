package com.mb14.halfpenny.ui.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mb14.halfpenny.R;
import com.mb14.halfpenny.adapters.TransactionAdapter;
import com.mb14.halfpenny.api.jsonblob.Expenses;
import com.mb14.halfpenny.api.jsonblob.JSONBlob;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by mb-14 on 29/02/16.
 */
public class TransactionListFragment extends Fragment implements TransactionAdapter.OnTransactionUpdateListener {
    private ListView listView;
    int category;
    TransactionAdapter adapter;
    ScheduledThreadPoolExecutor threadpool;
    ProgressDialog progressDialog;

    public static TransactionListFragment newInstance(int category){
        TransactionListFragment fragment = new TransactionListFragment();
        Bundle args = new Bundle();
        args.putInt("category",category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = getArguments().getInt("category");
        threadpool = new ScheduledThreadPoolExecutor(1);
        initProgressDialog();
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Updating expenses");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_list, container, false);
        listView = (ListView)view.findViewById(R.id.list_transactions);
        adapter = new TransactionAdapter(getActivity(),category,this);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onTransactionUpdate(Expenses expenses) {
        //Send PUT request to server
        Call<Expenses> call = JSONBlob.getService().updateExpenses(expenses);
        progressDialog.show();
        call.enqueue(new Callback<Expenses>() {
            @Override
            public void onResponse(Response<Expenses> response) {
                if (response.isSuccess()) {
                    progressDialog.dismiss();
                    adapter.updateExpenses(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    class FetchExpensesTask implements Runnable {

        @Override
        public void run() {
            Call<Expenses> call = JSONBlob.getService().getExpenses();
            try {
                final Response<Expenses> response = call.execute();
                if(response.isSuccess()){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(response.isSuccess())
                                if(!response.body().equals(adapter.getExpenses()))
                                adapter.updateExpenses(response.body());
                        }
                    });

                }
            } catch (IOException e) {

            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        threadpool.shutdownNow();

    }

    @Override
    public void onResume() {
        super.onResume();
        threadpool = new ScheduledThreadPoolExecutor(1);
        //Poll after every 10 seconds
        threadpool.scheduleWithFixedDelay(new FetchExpensesTask(), 0, 10, TimeUnit.SECONDS);
    }

}


