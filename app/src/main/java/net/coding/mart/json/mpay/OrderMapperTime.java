package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chenchao on 2017/2/21.
 */

public class OrderMapperTime implements Serializable {

    private static final long serialVersionUID = 3313545285795116739L;

    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("value")
    @Expose
    public String value;
    @SerializedName("rangeDays")
    @Expose
    public int rangeDays;

    public String getRequestParam() {
        return String.valueOf(rangeDays * 24 * 3600 * 1000);
    }

}
