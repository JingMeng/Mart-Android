package net.coding.mart.json.developer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

/**
 * Created by liu on 16/6/21.
 */
@Generated("org.jsonschema2pojo")
public class Datum {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("rewardId")
    @Expose
    public Integer rewardId;
    @SerializedName("fromPrice")
    @Expose
    public String fromPrice;
    @SerializedName("toPrice")
    @Expose
    public String toPrice;
    @SerializedName("fromTerm")
    @Expose
    public Integer fromTerm;
    @SerializedName("toTerm")
    @Expose
    public Integer toTerm;
    @SerializedName("platformCount")
    @Expose
    public Integer platformCount;
    @SerializedName("webPageCount")
    @Expose
    public Integer webPageCount;
    @SerializedName("moduleCount")
    @Expose
    public Integer moduleCount;
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("platforms")
    @Expose
    public List<String> platforms = new ArrayList<String>();

}