package net.coding.mart.json.developer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 16/6/21.
 */
public class FunctionResult implements Serializable {
    private static final long serialVersionUID = -5172480288358144043L;
    @SerializedName("items")
    @Expose
    public List<Item>  items;
    @SerializedName("quote")
    @Expose
    public Quote quote;
}
