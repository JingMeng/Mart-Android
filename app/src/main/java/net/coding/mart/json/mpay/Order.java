package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.common.Global;
import net.coding.mart.common.constant.OrderStatus;
import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;

public class Order extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = 486878794558940338L;

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
    @SerializedName("updatedAt")
    @Expose
    public String updatedAt;
    @SerializedName("creatorId")
    @Expose
    public int creatorId;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("userId")
    @Expose
    public int userId;
    @SerializedName("reward")
    @Expose
    public OrderReward reward;
    @SerializedName("tradeType")
    @Expose
    public String tradeType;
    @SerializedName("symbol")
    @Expose
    public String symbol;

    public String getTime() {
        return Global.timeToString(createdAt);
    }

    public String getMoney() {
        return String.format("%s%s", symbol, totalFee);
    }

    public String getStatusString() {
        return OrderStatus.getAlics(status);
    }

    public OrderStatus getStatus() {
        return OrderStatus.name2enum(status);
    }

    public String getTypeString() {
        return tradeType;
    }

    public boolean isRewardPrepaymentPending() {
        return "RewardPrepayment".equalsIgnoreCase(orderType) && "Pending".equalsIgnoreCase(status);
    }

    public boolean isWithDrawNoSystem() { // 非系统提现
        return "WithDraw".equalsIgnoreCase(orderType) && !"SystemFinance".equalsIgnoreCase(productType);
    }

    public String getTitle() {
        return title;
    }

}
