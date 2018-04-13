package net.coding.mart.json.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.mart2.user.MartUser;

import java.io.Serializable;

public class RewardDynamic implements Serializable {

    private static final long serialVersionUID = -2686967112909375644L;
    @SerializedName("activity")
    @Expose
    public RewardDynamicData activity;
    @SerializedName("action")
    @Expose
    public int action;
    @SerializedName("user")
    @Expose
    public MartUser user;
    @SerializedName(value = "action_msg", alternate = {"actionMsg"})
    @Expose
    public String actionMsg = "";
    @SerializedName("remark")
    @Expose
    public String remark = "";

}