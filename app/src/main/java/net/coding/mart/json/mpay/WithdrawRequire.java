package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 16/8/4.
 */
public class WithdrawRequire extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = 2892596386076603716L;

    @SerializedName("passIdentity")
    @Expose
    public boolean passIdentity;
    @SerializedName("hasPassword")
    @Expose
    public boolean hasPassword;
    @SerializedName("accounts")
    @Expose
    public List<WithdrawAccount> accounts = new ArrayList<>(1);
}
