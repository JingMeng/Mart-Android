package net.coding.mart.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class RewardsPreview implements Serializable {

    private static final long serialVersionUID = 6649908984851274383L;

    @SerializedName("rewardCount")
    @Expose
    public int rewardCount;
    @SerializedName("rewardUserCount")
    @Expose
    public int rewardUserCount;
    @SerializedName("validMoneyCount")
    @Expose
    public String validMoneyCount = "";

}