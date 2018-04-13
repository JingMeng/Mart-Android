package net.coding.mart.json.developer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liu on 16/5/26.
 */
public class AliPayVO implements Serializable{

    private static final long serialVersionUID = -4209743934843155494L;
    @SerializedName(value = "charge_id", alternate = {"chargeId"})
    @Expose
    public String chargeId;
    @SerializedName("order")
    @Expose
    public String order;
}
