package com.mb14.halfpenny.api.jsonblob;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Expenses {
    @SerializedName("expenses")
    private List<Transaction> transactions;

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Expenses(){
        transactions = new ArrayList<Transaction>();
    }

}

