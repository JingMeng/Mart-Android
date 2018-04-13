package net.coding.mart.json.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.common.constant.Progress;
import net.coding.mart.common.constant.RewardType;
import net.coding.mart.json.BasicJobInfo;
import net.coding.mart.json.RoleType;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.Generated;

/**
 * Created by chenchao on 16/7/28.
 */
@Generated("org.jsonschema2pojo")
public class SimplePublished implements Serializable, BasicJobInfo {

    private static final long serialVersionUID = 7385929213184571786L;

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("type")
    @Expose
    public int type;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName(value = "format_description", alternate = {"formatDescription"})
    @Expose
    public String formatDescription;
    @SerializedName(value = "first_sample", alternate = {"firstSample"})
    @Expose
    public String firstSample;
    @SerializedName(value = "format_first_sample", alternate = {"formatFirstSample"})
    @Expose
    public String formatFirstSample;
    @SerializedName("cover")
    @Expose
    public String cover;
    @SerializedName(value = "plain_content", alternate = {"plainContent"})
    @Expose
    public String plainContent;
    @SerializedName(value = "high_paid", alternate = {"highPaid"})
    @Expose
    public int highPaid; // 1 表示普通 2 表示高回报项目
    @Expose
    @SerializedName(value = "apply_count", alternate = {"applyCount"})
    public int applyCount;
    @SerializedName("duration")
    @Expose
    public int duration;
    @SerializedName(value = "format_price_no_currency", alternate = {"formatPriceNoCurrency"})
    @Expose
    public String formatPriceNoCurrency;
    @SerializedName(value = "role_types", alternate = {"roleTypes"})
    @Expose
    public java.util.List<RoleType> roleTypes = new ArrayList<RoleType>();

    public String getPriceString() {
        return String.format("￥%s", formatPriceNoCurrency);
    }

    public Progress getStatus() {
        return Progress.id2Enum(status);
    }

    public boolean isHighPaid() {
        return highPaid == 1;
    }

    public boolean canJumpDetail() {
        return getStatus().canJumpDetail();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public String getRewardTypeString() {
        return RewardType.idToName(type);
    }

    @Override
    public String getRoles() {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (RoleType item : roleTypes) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append("，");
            }
            sb.append(item.name);
        }
        return sb.toString();
    }

    @Override
    public String getDurationString() {
        return duration > 0 ? (duration + " 天") : "待商议";
    }

    @Override
    public String getFormatPrice() {
        return getPriceString();
    }

    @Override
    public String getRewardTypeName() {
        return getRewardTypeString();
    }
}