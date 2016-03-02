package com.mb14.halfpenny.api.jsonblob;
import com.google.gson.annotations.SerializedName;
/**
 * Created by mb-14 on 01/03/16.
 */
public class Transaction {
    public static final int CATEGORY_ALL = 0;
    public static final int CATEGORY_TAXI = 1;
    public static final int CATEGORY_RECHARGE = 2;
    @SerializedName("id")
    private String id;
    @SerializedName("description")
    private String description;
    @SerializedName("amount")
    private int amount;
    @SerializedName("category")
    private String category;
    @SerializedName("state")
    private String state;
    @SerializedName("time")
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean equals(Transaction other){
        if(!id.equals(other.id))
                return false;
        if(!description.equals(other.description))
                return false;
        if(!time.equals(other.time))
                return false;
        if(amount != other.amount)
            return false;
        if(!state.equals(other.state))
            return false;
        if (!category.equals(other.category))
            return false;
        return true;
    }
}
