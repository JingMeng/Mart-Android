package net.coding.mart.json.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chenchao on 16/10/11.
 */
public class ApplyResumeList implements Serializable {

    private static final long serialVersionUID = -1222485279366714302L;

    @SerializedName("userId")
    @Expose
    public int userId;
    @SerializedName("rewardId")
    @Expose
    public int rewardId;
    @SerializedName("applyId")
    @Expose
    public int applyId;
    @SerializedName("targetId")
    @Expose
    public int targetId;
    @SerializedName("targetType")
    @Expose
    public int targetType;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("createdAt")
    @Expose
    public String createdAt;
    @SerializedName("updatedAt")
    @Expose
    public String updatedAt;
    @SerializedName("deletedAt")
    @Expose
    public String deletedAt;

    public boolean isProject() {
        return targetType == 1;
    }

    public boolean isRole() {
        return targetType == 0;
    }
}
