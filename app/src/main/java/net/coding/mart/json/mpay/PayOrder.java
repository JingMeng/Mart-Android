package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by chenchao on 2017/6/5.
 * 生成订单
 */

public class PayOrder extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = -6693941933845775738L;

    @SerializedName("account")
    @Expose
    public Account account;
    @SerializedName("affordable")
    @Expose
    public boolean affordable;
    @SerializedName("orders")
    @Expose
    public ArrayList<Order> orders = null;
}
