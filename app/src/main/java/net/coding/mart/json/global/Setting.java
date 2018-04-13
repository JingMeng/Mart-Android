package net.coding.mart.json.global;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chenchao on 2017/9/28.
 */

public class Setting implements Serializable {

    private static final long serialVersionUID = 4756485158372078496L;

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("code")
    @Expose
    public String code = "";
    @SerializedName("value")
    @Expose
    public String value = "";
    @SerializedName("description")
    @Expose
    public String description = "";
}
