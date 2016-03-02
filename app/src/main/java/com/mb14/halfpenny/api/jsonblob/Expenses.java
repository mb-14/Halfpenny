package com.mb14.halfpenny.api.jsonblob;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Expenses {
    private transient List<Transaction> taxiTransactions;
    private transient List<Transaction> rechargeTransactions;

    @SerializedName("expenses")
    private List<Transaction> transactions;

    public List<Transaction> getTransactions(int category) {
        if(category == Transaction.CATEGORY_ALL)
            return transactions;
        else if(category == Transaction.CATEGORY_TAXI)
            return taxiTransactions;
        else if(category == Transaction.CATEGORY_RECHARGE)
            return rechargeTransactions;
        return transactions;
    }

    public Expenses(){
        transactions = new ArrayList<Transaction>();
        taxiTransactions = new ArrayList<Transaction>();
        rechargeTransactions = new ArrayList<Transaction>();
    }



    public void generateCategories() {
        rechargeTransactions = new ArrayList<Transaction>();
        taxiTransactions = new ArrayList<Transaction>();
        for(Transaction transaction : transactions){
            if(transaction.getCategory().equals("Taxi"))
                taxiTransactions.add(transaction);
            else if (transaction.getCategory().equals("Recharge"))
                rechargeTransactions.add(transaction);
        }
    }



    public Transaction getTransaction(String transactionId) {
        for(Transaction transaction : transactions)
            if(transaction.getId().equals(transactionId))
                return transaction;
        return null;
    }


    public boolean equals(Expenses other){
        if (other == null) return false;
        if(transactions.size() != other.transactions.size())
            return false;
        for(int i = 0;i<transactions.size();i++){
            if(!transactions.get(i).equals(other.transactions.get(i)))
                return false;
        }
        return true;
    }

}

