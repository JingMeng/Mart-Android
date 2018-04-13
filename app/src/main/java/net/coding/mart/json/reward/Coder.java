
package net.coding.mart.json.reward;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.R;
import net.coding.mart.common.constant.DeveloperType;
import net.coding.mart.common.constant.IdentityStatus;
import net.coding.mart.common.event.CallPhoneEvent;
import net.coding.mart.json.reward.user.ApplyContact;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

public class Coder implements Serializable {

    private static final long serialVersionUID = 3754583015987638300L;

    public enum Status {
        defaultStatus(1),
        accept(2),
        refuse(3);

        public int value;

        public static Status idToType(int id) {
            for (Status item : Status.values()) {
                if (item.value == id) {
                    return item;
                }
            }

            return defaultStatus;
        }

        Status(int value) {
            this.value = value;
        }
    }

    @SerializedName(value = "global_key", alternate = {"globalKey"})
    @Expose
    public String globalKey;
    @SerializedName(value = "user_name", alternate = {"userName"})
    @Expose
    public String userName;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("avatar")
    @Expose
    public String avatar;
    @SerializedName(value = "role_type", alternate = {"roleType"})
    @Expose
    public String roleType;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName(value = "user_mobile", alternate = {"userMobile"})
    @Expose
    public String userMobile;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("qq")
    @Expose
    public String qq;
    @SerializedName("skills")
    @Expose
    public String skills;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName(value = "reward_role", alternate = {"rewardRole"})
    @Expose
    public int rewardRole;  // 个人开发者 1, 团队 2
    @SerializedName(value = "good_at", alternate = {"goodAt"})
    @Expose
    public String goodAt;
    @SerializedName(value = "role_type_id", alternate = {"roleTypeId"})
    @Expose
    public int roleTypeId;
    @SerializedName(value = "user_id", alternate = {"userId"})
    @Expose
    public int userId;
    @SerializedName(value = "apply_id", alternate = {"applyId"})
    @Expose
    public int applyId;
    @SerializedName("status")
    @Expose
    public int status; // 2 选中, 3 拒绝, 1 默认
    @SerializedName("createdAt")
    @Expose
    public String createdAt;
    @SerializedName("identityStatus")
    @Expose
    public int identityStatus;
    @SerializedName("excellent")
    @Expose
    public int excellent;

    public boolean stagePayed = false; // 需求方是否已经付过阶段款， 退款了也算

    public String getCreatedAtFormat() {
        return String.format("报名时间：%s", createdAt);
    }

    public void onClickMobile(View v) {
        if (!TextUtils.isEmpty(mobile)) {
            EventBus.getDefault().post(new CallPhoneEvent(mobile));
        }
    }

    public boolean isRefuse() {
        return status == Status.refuse.value;
    }

    public void setStatus(Status s) {
        status = s.value;
    }

    public void onClickQQ(View v) {
        if (!TextUtils.isEmpty(qq)) {
            EventBus.getDefault().post(new CallPhoneEvent(qq, CallPhoneEvent.Type.QQ));
        }
    }

    public Status getStatus() {
        return Status.idToType(status);
    }

    public boolean isStatus(Status s) {
        return s.value == status;
    }

    public void updateContact(ApplyContact contact) {
        email = contact.email;
        mobile = contact.phone;
        qq = contact.qq;
    }

    public boolean isPassIdentity() {
        return identityStatus == IdentityStatus.CHECKED.id;
    }

    public boolean isExcellent() {
        return excellent == 1;
    }

    public Drawable getCardDrawable(Context context) {
        Drawable rightIcon = null;
        if (isExcellent()) {
            rightIcon = context.getResources().getDrawable(R.mipmap.identity_id_card_excellent);
        } else if (isPassIdentity()) {
            rightIcon = context.getResources().getDrawable(R.mipmap.identity_id_card);
        }

        return rightIcon;
    }

    public boolean isTeam() {
        return rewardRole == DeveloperType.TEAM.id;
    }

    public DeveloperType getRewardRole() {
        return DeveloperType.id2Enum(rewardRole);
    }
}
