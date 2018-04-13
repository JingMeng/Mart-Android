package net.coding.mart.json.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 16/10/11.
 */

public class RewardApply implements Serializable {

    private static final long serialVersionUID = 2615281897328275130L;

    @SerializedName("userId")
    @Expose
    public int userId;
    @SerializedName("rewardId")
    @Expose
    public int rewardId;
    @SerializedName("roleTypeId")
    @Expose
    public int roleTypeId;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("secret")
    @Expose
    public int secret;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("applyResumeList")
    @Expose
    public List<ApplyResumeList> applyResumeList = new ArrayList<>();
}
