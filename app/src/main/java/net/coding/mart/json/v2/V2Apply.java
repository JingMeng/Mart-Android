package net.coding.mart.json.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import net.coding.mart.common.Global;
import net.coding.mart.common.ImageLoadTool;
import net.coding.mart.common.constant.ApplyStatus;
import net.coding.mart.json.mart2.user.MartUser;

import java.io.Serializable;

/**
 * Created by chenchao on 2017/10/19.
 */

public class V2Apply implements Serializable {

    private static final long serialVersionUID = 8987038351428434653L;

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("userId")
    @Expose
    public int userId;
    @SerializedName("rewardId")
    @Expose
    public int rewardId;
    @SerializedName("status")
    @Expose
    public ApplyStatus status = ApplyStatus.UNKNOWN_STATUS;
    @SerializedName("secret")
    @Expose
    public String secret;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("createdAt")
    @Expose
    public String createdAt;
    @SerializedName("updatedAt")
    @Expose
    public long updatedAt;
    @SerializedName("goodAt")
    @Expose
    public String goodAt;
    @SerializedName("remark")
    @Expose
    public String remark = "";
    @SerializedName("user")
    @Expose
    public MartUser user;
    @SerializedName("reward")
    @Expose
    public Reward reward;
    @SerializedName("marked")
    @Expose
    public boolean marked;
    @SerializedName("roleTypeName")
    @Expose
    public String roleTypeName;

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, V2Apply apply) {
        ImageLoadTool.loadImage(imageView, apply.user.avatar);
    }

    public String getLine2String() {
        return String.format("%s | %s", user.developerType.alics, user.freeTime.alics);
    }

    public String getTimeString() {
        return Global.getTimeDetail(updatedAt);
    }

    public String getStatusString() {
        if (status == ApplyStatus.UNKNOWN_STATUS) {
            return "";
        }
        return status.alias;
    }

    public String getRemarkString() {
        return !TextUtils.isEmpty(remark) ? remark : "暂无备注信息。";
    }

    public int getStatusColor() {
        return status.color;
    }

    public int visableCandidate() {
        return marked ? View.VISIBLE : View.INVISIBLE;
    }

    public boolean hasContact() {
        return user != null && !TextUtils.isEmpty(user.phone);
    }
}
