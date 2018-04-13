package net.coding.mart.json.user.identity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;

/**
 * Created by chenchao on 2017/6/8.
 */

public class IdentityOrder extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = -7998257572214008115L;

    @SerializedName("orderId")
    @Expose
    public String orderId;
    @SerializedName("orderType")
    @Expose
    public String orderType;
    @SerializedName("productId")
    @Expose
    public int productId;
    @SerializedName("productType")
    @Expose
    public String productType;
    @SerializedName("totalFee")
    @Expose
    public String totalFee;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("createdAt")
    @Expose
    public String createdAt;
    @SerializedName("totalFeeOrigin")
    @Expose
    public String totalFeeOrigin;

}
