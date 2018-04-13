package net.coding.mart.json.reward.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;

/**
 * Created by chenchao on 16/10/19.
 */

public class ApplyContactResult extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = 2219379584518669732L;

    @SerializedName("freeTotal")
    @Expose
    public int freeTotal;
    @SerializedName("freeRemain")
    @Expose
    public int freeRemain;
    @SerializedName("fee")
    @Expose
    public String fee;
}
