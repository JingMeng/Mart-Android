package net.coding.mart.json.reward.project;

import android.text.Spanned;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.common.Global;
import net.coding.mart.common.constant.ProjectStatus;
import net.coding.mart.json.BaseHttpResult;
import net.coding.mart.json.File;
import net.coding.mart.json.RoleType;
import net.coding.mart.json.body.NewReward;
import net.coding.mart.json.reward.IndustryName;
import net.coding.mart.json.user.Owner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 2017/9/29.
 * 新的 Reward 类型
 */
public class ProjectPublish extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = -406060610362768019L;

    @SerializedName("ownerId")
    @Expose
    public int ownerId;
    @SerializedName("name")
    @Expose
    public String name = "";
    @SerializedName("description")
    @Expose
    public String description = "";
    @SerializedName("cover")
    @Expose
    public String cover = "";
    @SerializedName("price")
    @Expose
    public String price = "";
    @SerializedName("roles")
    @Expose
    public String roles = "";
    @SerializedName("type")
    @Expose
    public String type = "";
    @SerializedName("files")
    @Expose
    public List<File> files = new ArrayList<>(0);
    @SerializedName("status")
    @Expose
    public ProjectStatus status;
    @SerializedName(value = "firstSample")
    @Expose
    public String firstSample = "";
    @SerializedName(value = "secondSample")
    @Expose
    public String secondSample = "";
    @SerializedName("duration")
    @Expose
    public int duration;
    @SerializedName("contactName")
    @Expose
    public String contactName = "";
    @SerializedName("contactEmail")
    @Expose
    public String contactEmail = "";
    @SerializedName("contactMobile")
    @Expose
    public String contactMobile = "";
    @SerializedName("serviceFee")
    @Expose
    public String serviceFee = "";
    @SerializedName("rewardDemand")
    @Expose
    public String rewardDemand = "";
    @SerializedName("phoneCountryCode")
    @Expose
    public String phoneCountryCode = "";
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("owner")
    @Expose
    public Owner owner;
    @SerializedName("createdAt")
    @Expose
    public long createdAt;
    @SerializedName("roleType")
    @Expose
    public RoleType roleType;
    @SerializedName("developerType")
    @Expose
    public NewReward.DeveloperType developerType;
    @SerializedName("developerRole")
    @Expose
    public int developerRole;
    @SerializedName("industry")
    @Expose
    public String industry = "";
    @SerializedName("industryName")
    @Expose
    public String industryName = "";
    @SerializedName("statusText")
    @Expose
    public String statusText = "";
    @SerializedName("typeText")
    @Expose
    public String typeText = "";
    @SerializedName("bargain")
    @Expose
    public boolean bargain;

    private ArrayList<IndustryName> industrys = null;

    public ArrayList<IndustryName> getIndustrys() {
        if (industrys == null) {
            industrys = new ArrayList<>();
            String[] ids = industry.split(",");
            String[] names = industryName.split(",");

            for (int i = 0; i < ids.length; ++i) {
                try {
                    int id = Integer.valueOf(ids[i]);
                    if (i < names.length) {
                        String name = names[i];
                        IndustryName industryName = new IndustryName();
                        industryName.setData(id, name);
                        industrys.add(industryName);
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    break;
                }
            }
        }

        return industrys;
    }

    public String getIdAndTitle() {
        return String.format("NO.%s %s", id, name);
    }

    public void updateIndustry(ArrayList<IndustryName> industryNames) {
        if (industrys == null) {
            industrys = new ArrayList<>();
        } else {
            industrys.clear();
        }

        industrys.addAll(industryNames);
    }

    public String getTypeText() {
        return String.format("类型：%s", typeText);
    }

    public String getRoles() {
        return String.format("招募：%s", roles);
    }

    public String getDurationStringPrefix() {
        return duration > 0 ? String.format("周期：%s 天", duration) : "周期待商议";
    }

    public String getIndustrysString() {
        ArrayList<IndustryName> industryNames = getIndustrys();
        return IndustryName.createName(industryNames);
    }

    public Spanned getNoCurrencySymbolFormatPrice() {
        return Global.createColorHtml("金额：", "￥" + price, "", "#F6A823");
    }

    public String getIndustrysIdString() {
        ArrayList<IndustryName> list = getIndustrys();
        return IndustryName.createIdString(list);
    }

    public String getIdString() {
        return String.valueOf(id);
    }
    public String getDurationString() {
        return String.format("%s 天", duration);
    }

    public String getSampleLinks() {
        String link = "";
        if (!TextUtils.isEmpty(firstSample)) {
            link = firstSample;
        }

        if (!TextUtils.isEmpty(secondSample)) {
            link += "/n" + secondSample;
        }

        return link;
    }
}
