package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by chenchao on 16/8/4.
 */
public class Account implements Serializable {

    private static final long serialVersionUID = 6903425236710674805L;

    @SerializedName("userId")
    @Expose
    public int userId;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("balance")
    @Expose
    public String balance;
    @SerializedName("balanceValue")
    @Expose
    public BigDecimal balanceValue = new BigDecimal(0);
    @SerializedName("isSafe")
    @Expose
    public boolean isSafe;
    @SerializedName("freeze")
    @Expose
    public String freeze;
    @SerializedName("freezeValue")
    @Expose
    public BigDecimal freezeValue = new BigDecimal(0);
}
