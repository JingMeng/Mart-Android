package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chenchao on 16/8/5.
 */
public class WithdrawAccountSimple implements Serializable {

    private static final long serialVersionUID = -2449789268098775732L;

    @SerializedName("account")
    @Expose
    public String account = "";
    @SerializedName("accountName")
    @Expose
    public String accountName = "";
}
