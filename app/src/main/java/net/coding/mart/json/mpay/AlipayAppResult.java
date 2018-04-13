package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chenchao on 16/8/12.
 */
public class AlipayAppResult implements Serializable {

    private static final long serialVersionUID = 6762944557628825286L;

    @SerializedName("order")
    @Expose
    public String order;

}
