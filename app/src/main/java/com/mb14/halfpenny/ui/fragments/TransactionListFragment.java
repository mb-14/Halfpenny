package com.mb14.halfpenny.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mb14.halfpenny.R;
import com.mb14.halfpenny.adapters.TransactionAdapter;



/**
 * Created by mb-14 on 29/02/16.
 */
public class TransactionListFragment extends Fragment {
    private ListView listView;
    int category;
    public static final int CATEGORY_ALL = 0;
    public static final int CATEGORY_TAXI = 1;
    public static final int CATEGORY_RECHARGE = 2;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_list,container,false);
        listView = (ListView)view.findViewById(R.id.list_transactions);
        listView.setAdapter(new TransactionAdapter(getActivity()));
        return view;
    }

}

