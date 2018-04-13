package net.coding.mart.json.reward.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;

/**
 * Created by chenchao on 16/10/17.
 */

public class ApplyContact extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = -6170821425189571072L;

    @SerializedName("rewardId")
    @Expose
    public int rewardId;
    @SerializedName("userId")
    @Expose
    public int userId;
    @SerializedName("applyId")
    @Expose
    public int applyId;
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("qq")
    @Expose
    public String qq;
}
