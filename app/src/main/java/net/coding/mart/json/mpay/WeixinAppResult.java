package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chenchao on 16/8/12.
 */
public class WeixinAppResult implements Serializable {

    private static final long serialVersionUID = -8965569695283948638L;

    @SerializedName("appId")
    @Expose
    public String appId;
    @SerializedName("partnerId")
    @Expose
    public String partnerId;
    @SerializedName("prepayId")
    @Expose
    public String prepayId;
    @SerializedName("nonceStr")
    @Expose
    public String nonceStr;
    @SerializedName("timestamp")
    @Expose
    public String timestamp;
    @SerializedName("package")
    @Expose
    public String _package;
    @SerializedName("sign")
    @Expose
    public String sign;
}
