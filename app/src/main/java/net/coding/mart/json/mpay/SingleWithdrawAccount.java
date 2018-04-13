package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;

/**
 * Created by chenchao on 16/8/18.
 */
public class SingleWithdrawAccount extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = -2850747218337121361L;

    @SerializedName("account")
    @Expose
    public WithdrawAccount account;
}
