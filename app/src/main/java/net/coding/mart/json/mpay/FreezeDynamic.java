package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chenchao on 16/9/1.
 */
public class FreezeDynamic implements Serializable {

    private static final long serialVersionUID = -6197270171397300081L;

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("user")
    @Expose
    public FreezeUser user;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("source")
    @Expose
    public String source;
    @SerializedName("createdAt")
    @Expose
    public String createdAt;
    @SerializedName("updatedAt")
    @Expose
    public String updatedAt;

}