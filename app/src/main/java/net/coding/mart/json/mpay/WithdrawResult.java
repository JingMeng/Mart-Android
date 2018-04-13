package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;

/**
 * Created by chenchao on 16/8/5.
 */
public class WithdrawResult extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = -31682054196902835L;

    @SerializedName("order")
    @Expose
    public Order order;
    @SerializedName("account")
    @Expose
    public WithdrawAccountSimple account;
    @SerializedName("invoice")
    @Expose
    public InvoiceAccountSimple invoice;
}
