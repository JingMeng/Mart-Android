package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;

/**
 * Created by chenchao on 16/8/12.
 */
public class WeixinRecharge extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = 8894479701967442109L;

    @SerializedName("orderId")
    @Expose
    public String orderId;
    @SerializedName("weixinAppResult")
    @Expose
    public WeixinAppResult weixinAppResult;
}
