package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpPagerResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderPage extends BaseHttpPagerResult implements Serializable {

    private static final long serialVersionUID = 8673520272281926515L;

    @SerializedName("order")
    @Expose
    public List<Order> orderList = new ArrayList<>();
}
