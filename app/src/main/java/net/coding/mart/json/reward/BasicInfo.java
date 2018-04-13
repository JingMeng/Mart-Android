
package net.coding.mart.json.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.text.TextUtils;

import net.coding.mart.common.constant.Progress;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Deprecated
@Generated("org.jsonschema2pojo")
public class BasicInfo implements Serializable {

    private static final long serialVersionUID = 4468106187067624530L;

    @SerializedName("cover")
    @Expose
    public String cover = "";
    @SerializedName("name")
    @Expose
    public String name = "";
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("allowEdit")
    @Expose
    public boolean allowEdit;
    @SerializedName("type")
    @Expose
    public int type;
    @SerializedName("typeName")
    @Expose
    public String typeName = "";
    @SerializedName("roleTypes")
    @Expose
    public List<String> roleTypes = new ArrayList<String>();
    @SerializedName("price")
    @Expose
    public double price;
    @SerializedName("formatPrice")
    @Expose
    public String formatPrice = "";
    @SerializedName("balance")
    @Expose
    public double balance;
    @SerializedName("formatBalance")
    @Expose
    public String formatBalance = "";
    @SerializedName("priceWithFee")
    @Expose
    public int priceWithFee;
    @SerializedName("showBalance")
    @Expose
    public boolean showBalance;
    @SerializedName("submitWay")
    @Expose
    public int submitWay;
    @SerializedName("ownerName")
    @Expose
    public String ownerName = "";
    @SerializedName("duration")
    @Expose
    public int duration;
    @SerializedName("allowPublishedAccess")
    @Expose
    public boolean allowPublishedAccess;
    @SerializedName("managerName")
    @Expose
    public String managerNames = "";

    public String getSectionString() {
        return String.format("阶段列表");
    }

    public String getIdString() {
        return "NO." + id;
    }
    public String getRolesString() {
        return TextUtils.join("，", roleTypes);
    }

    public Progress getProgress() {
        return Progress.id2Enum(status);
    }

    public int getDuration() {
        return duration;
    }

    public String getDurationString() {
        return duration > 0 ? String.format("%s 天", duration) : "待商议";
    }

    public String getDurationStringPrefix() {
        return duration > 0 ? String.format("%s 天", duration) : "周期待商议";
    }

    public String getSpaceName() {
        int spaceLen = getIdString().length() + 8;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < spaceLen; ++i) {
            sb.append(" ");
        }
        sb.append(name);

        return sb.toString();
    }

    public int getId() {
        return id;
    }

    public String getRoles() {
        return getRolesString();
    }

    public String getFormatPrice() {
        return formatPrice;
    }

    public String getNoCurrencySymbolFormatPrice() {
        return formatPrice.replace("￥", "");
    }

    public String getRewardTypeName() {
        return typeName;
    }

}
