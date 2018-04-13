package net.coding.mart.json.developer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liu on 16/6/6.
 */
public class CalculateResult implements Serializable{
    private static final long serialVersionUID = -6889506706155179832L;
    @SerializedName("fromPrice")
    @Expose
    public String fromPrice = "";
    @SerializedName("toPrice")
    @Expose
    public String toPrice = "";
    @SerializedName("fromTerm")
    @Expose
    public int fromTerm;
    @SerializedName("toTerm")
    @Expose
    public int toTerm;
    @SerializedName("platformCount")
    @Expose
    public int platformCount;
    @SerializedName("webPageCount")
    @Expose
    public int webPageCount;
    @SerializedName("moduleCount")
    @Expose
    public int moduleCount;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("id")
    @Expose
    public int id;
}
