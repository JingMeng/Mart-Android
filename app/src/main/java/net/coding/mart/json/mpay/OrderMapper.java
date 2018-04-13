package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by chenchao on 2017/2/21.
 * 余额宝的类型动态获取
 */

public class OrderMapper extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = 5337239237274776828L;

    @SerializedName("orderType")
    @Expose
    public Map<String, String> orderType;
    @SerializedName("productType")
    @Expose
    public Map<String, String> productType;
    @SerializedName("tradeType")
    @Expose
    public Map<String, String> tradeType;
    @SerializedName("symbol")
    @Expose
    public Map<String, String> symbol;
    @SerializedName("tradeOptions")
    @Expose
    public List<OrderMapperTrade> tradeOptions;
    @SerializedName("timeOptions")
    @Expose
    public List<OrderMapperTime> timeOptions;
    @SerializedName("status")
    @Expose
    public Map<String, String> status;
}
