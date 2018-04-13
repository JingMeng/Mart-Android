package net.coding.mart.json.developer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liu on 16/5/26.
 */
public class WeixinPayVO implements Serializable{

    private static final long serialVersionUID = -2077487776389700487L;

    @SerializedName("package")
    @Expose
    public String _package;
    @SerializedName(value = "charge_id", alternate = {"chargeId"})
    @Expose
    public String chargeId;
    @SerializedName("appid")
    @Expose
    public String appid;
    @SerializedName("sign")
    @Expose
    public String sign;
    @SerializedName("partnerid")
    @Expose
    public String partnerid;
    @SerializedName("prepayid")
    @Expose
    public String prepayid;
    @SerializedName("noncestr")
    @Expose
    public String noncestr;
    @SerializedName("timestamp")
    @Expose
    public String timestamp;
}
