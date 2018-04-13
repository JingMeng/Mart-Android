package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chenchao on 16/8/5.
 */
public class WithdrawAccount implements Serializable {

    private static final long serialVersionUID = -2449789268098775732L;

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("userId")
    @Expose
    public int userId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("account")
    @Expose
    public String account;
    @SerializedName("accountType")
    @Expose
    public String accountType;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("adminId")
    @Expose
    public int adminId;
    @SerializedName("createdAt")
    @Expose
    public String createdAt;
    @SerializedName("updatedAt")
    @Expose
    public String updatedAt;
    @SerializedName("deletedAt")
    @Expose
    public String deletedAt;

}
