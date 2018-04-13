package net.coding.mart.json.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.common.Global;
import net.coding.mart.common.constant.JoinStatus;
import net.coding.mart.common.constant.Progress;
import net.coding.mart.common.constant.RewardType;
import net.coding.mart.json.RoleType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 15/11/12.
 */
public class JoinJob implements Serializable {

    private static final long serialVersionUID = 8965985046730566532L;

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName(value = "reward_status", alternate = {"rewardStatus"})
    @Expose
    public int rewardStatus;
    @SerializedName("type")
    @Expose
    public int type;
    @SerializedName("price")
    @Expose
    public BigDecimal price = new BigDecimal(0);
    @SerializedName(value = "format_price", alternate = {"formatPrice"})
    @Expose
    public String formatPrice;
    @SerializedName(value = "apply_at", alternate = {"applyAt"})
    @Expose
    public String applyAt;
    @SerializedName(value = "format_apply_at", alternate = {"formatApplyAt"})
    @Expose
    public String formatApplyAt;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName(value = "apply_status", alternate = {"applyStatus"})
    @Expose
    public int applyStatus;
    @SerializedName(value = "role_type_id", alternate = {"roleTypeId"})
    @Expose
    public int roleTypeId;
    @SerializedName("duration")
    @Expose
    public int duration;
    @SerializedName("cover")
    @Expose
    public String cover;
    @SerializedName(value = "apply_count", alternate = {"applyCount"})
    @Expose
    public int applyCount;
    @SerializedName("roleTypes")
    @Expose
    public List<RoleType> roleTypes = new ArrayList<>();
    @SerializedName("phaseType")
    @Expose
    public Published.PhaseType phaseType = Published.PhaseType.STAGE;

    public boolean isNewPhase() {
        return phaseType == Published.PhaseType.PHASE;
    }


    public boolean canJumpDetail() {
        return getRewardStatus().canJumpDetail();
    }

    public String getUrl() {
        return Global.generateRewardLink(id);
    }

    public Progress getRewardStatus() {
        return Progress.id2Enum(rewardStatus);
    }

    public String getTypeString() {
        return RewardType.idToName(type);
    }

    public double getPrice() {
        return price.doubleValue();
    }

    public String getMessage() {
        return message;
    }

    public JoinStatus getApplyStatus() {
        return JoinStatus.id2Enum(applyStatus);
    }

    public void setApplyStatus(JoinStatus joinStatus) {
        this.applyStatus = joinStatus.id;
    }

}
