package net.coding.mart.json.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;
import net.coding.mart.json.mpay.Order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 16/11/29.
 */

public class MulStageOrder extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = -6769961248788113037L;

    @SerializedName("order")
    @Expose
    public List<Order> order = new ArrayList<>();
    @SerializedName("orderAmount")
    @Expose
    public String orderAmount;
    @SerializedName("stageAmount")
    @Expose
    public String stageAmount;
    @SerializedName("serviceFee")
    @Expose
    public String serviceFee;
}
