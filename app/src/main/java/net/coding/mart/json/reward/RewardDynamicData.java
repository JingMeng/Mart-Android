package net.coding.mart.json.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RewardDynamicData implements Serializable {

    private static final long serialVersionUID = 6493434869011045780L;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName(value = "reward_id", alternate = {"rewardId"})
    @Expose
    public int rewardId;
    @SerializedName(value = "user_id", alternate = {"userId"})
    @Expose
    public int userId;
    @SerializedName(value = "target_type", alternate = {"targetType"})
    @Expose
    public int targetType;
    @SerializedName(value = "target_id", alternate = {"targetId"})
    @Expose
    public int targetId;
    @SerializedName("action")
    @Expose
    public int action;
    @SerializedName("content")
    @Expose
    public String content = "";
    @SerializedName(value = "created_at", alternate = {"createdAt"})
    @Expose
    public String createdAt = "";
    @SerializedName(value = "format_created_at", alternate = {"formatCreatedAt"})
    @Expose
    public String formatCreatedAt = "";

}