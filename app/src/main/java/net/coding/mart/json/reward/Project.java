package net.coding.mart.json.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.common.constant.ProjectStatus;
import net.coding.mart.json.BaseHttpResult;
import net.coding.mart.json.RoleType;
import net.coding.mart.json.user.Owner;

import java.io.Serializable;

/**
 * Created by chenchao on 2017/9/28.
 */

public class Project extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = 5339388171598856964L;

    @SerializedName("ownerId")
    @Expose
    public int ownerId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("cover")
    @Expose
    public String cover;
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("roles")
    @Expose
    public String roles;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("status")
    @Expose
    public ProjectStatus status;
    @SerializedName("duration")
    @Expose
    public int duration;
    @SerializedName("contactName")
    @Expose
    public String contactName;
    @SerializedName("contactEmail")
    @Expose
    public String contactEmail;
    @SerializedName("contactMobile")
    @Expose
    public String contactMobile;
    @SerializedName("serviceFee")
    @Expose
    public String serviceFee;
    @SerializedName("rewardDemand")
    @Expose
    public String rewardDemand;
    @SerializedName("phoneCountryCode")
    @Expose
    public String phoneCountryCode;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("owner")
    @Expose
    public Owner owner;
    @SerializedName("createdAt")
    @Expose
    public String createdAt;
    @SerializedName("roleType")
    @Expose
    public RoleType roleType;
    @SerializedName("developerType")
    @Expose
    public String developerType;
    @SerializedName("developerRole")
    @Expose
    public int developerRole;
    @SerializedName("industry")
    @Expose
    public String industry;
    @SerializedName("industryName")
    @Expose
    public String industryName;
    @SerializedName("statusText")
    @Expose
    public String statusText;
    @SerializedName("typeText")
    @Expose
    public String typeText;

}
