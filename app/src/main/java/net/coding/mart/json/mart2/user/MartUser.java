package net.coding.mart.json.mart2.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.common.constant.AccountType;
import net.coding.mart.common.constant.DemandType;
import net.coding.mart.common.constant.DeveloperType;
import net.coding.mart.common.constant.IdentityStatus;
import net.coding.mart.common.constant.VipType;
import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;
import java.util.List;

public class MartUser extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = 6356766120971740179L;
    @SerializedName("phone")
    @Expose
    public String phone = "";
    @SerializedName("isoCode")
    @Expose
    public String isoCode = "";
    @SerializedName("countryCode")
    @Expose
    public String countryCode = "";
    @SerializedName("email")
    @Expose
    public String email = "";
    @SerializedName("excellent")
    @Expose
    public boolean excellent;
    @SerializedName("qq")
    @Expose
    public String qq = "";
    @SerializedName("description")
    @Expose
    public String description = "";
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("avatar")
    @Expose
    public String avatar;
    @SerializedName("identity")
    @Expose
    public String identity;
    @SerializedName("phoneValidation")
    @Expose
    public boolean phoneValidation;
    @SerializedName("emailValidation")
    @Expose
    public boolean emailValidation;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("registerAt")
    @Expose
    public String registerAt;
    @SerializedName("registerIp")
    @Expose
    public String registerIp;
    @SerializedName("loginAt")
    @Expose
    public String loginAt;
    @SerializedName("loginIp")
    @Expose
    public String loginIp;
    @SerializedName("lastLoginAt")
    @Expose
    public String lastLoginAt;
    @SerializedName("lastLoginIp")
    @Expose
    public String lastLoginIp;
    @SerializedName("lastActiveAt")
    @Expose
    public String lastActiveAt;
    @SerializedName("lastActiveIp")
    @Expose
    public String lastActiveIp;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("evaluation") // 开发者综合评分
    @Expose
    public String evaluation = "0";
    @SerializedName("infoComplete")
    @Expose
    public boolean infoComplete;
    @SerializedName("skillComplete")
    @Expose
    public boolean skillComplete;
    @SerializedName("surveyComplete")
    @Expose
    public boolean surveyComplete;
    @SerializedName("identityPassed")
    @Expose
    public boolean identityPassed;
    @SerializedName("developerTypeComplete")
    @Expose
    public boolean developerTypeComplete;
    @SerializedName("contractComplete")
    @Expose
    public boolean contractComplete;
    @SerializedName("teamDeveloper")
    @Expose
    public boolean teamDeveloper;
    @SerializedName("isDeveloper")
    @Expose
    public boolean isDeveloper;
    @SerializedName("deposit")
    @Expose
    public boolean deposit;
    @SerializedName("vipType")
    @Expose
    public VipType vipType = VipType.NOT_VIP;
    @SerializedName("vipFrom")
    @Expose
    public String vipFrom;
    @SerializedName("vipTo")
    @Expose
    public String vipTo;
    @SerializedName("nextVip")
    @Expose
    public List<String> nextVip = null;
    @SerializedName("vipDays")
    @Expose
    public int vipDays;
    @SerializedName("vipName")
    @Expose
    public String vipName;
    @SerializedName("deliverability")
    @Expose
    public double deliverability;
    @SerializedName("communication")
    @Expose
    public double communication;
    @SerializedName("responsibility")
    @Expose
    public double responsibility;
    @SerializedName("goodAts")
    @Expose
    public String goodAts;
    @SerializedName("vipContactCount")
    @Expose
    public double vipContactCount;
    @SerializedName("acceptNewRewardAllNotification")
    @Expose
    public boolean acceptNewRewardAllNotification;
    @SerializedName("vipServiceFee")
    @Expose
    public double vipServiceFee;
    @SerializedName("developerManager")
    @Expose
    public MartUser developerManager;
    @SerializedName("accountType")
    @Expose
    public AccountType accountType;
    @SerializedName("demandType")
    @Expose
    public DemandType demandType = DemandType.PERSONAL;
    @SerializedName("counter")
    @Expose
    public UserCounter counter = new UserCounter();
    @SerializedName("identityStatus")
    @Expose
    public IdentityStatus identityStatus = IdentityStatus.UNCHECKED;
    @SerializedName("developerType")
    @Expose
    public DeveloperType developerType = DeveloperType.SOLO;
    @SerializedName("freeTime")
    @Expose
    public FreeTime freeTime = FreeTime.LESS;
    @SerializedName("city")
    @Expose
    public City city = new City();
    @SerializedName("province")
    @Expose
    public City province = new City();
    @SerializedName("district")
    @Expose
    public City district = new City();

    public boolean isEnterpriseAccount() {
        return accountType == AccountType.DEMAND && demandType == DemandType.ENTERPRISE;
    }

    public boolean isDeveloperTeam() {
        return accountType == AccountType.DEVELOPER && developerType == DeveloperType.TEAM;
    }

    public boolean isCheking() {
        return identityStatus == IdentityStatus.CHECKING;
    }

    public boolean isPassed() {
        return identityPassed;
    }

    public boolean isNew() {
        return identityStatus == IdentityStatus.UNCHECKED
                || identityStatus == IdentityStatus.REJECTED;
    }

    public enum FreeTime {
        LESS("较少时间兼职"),
        MORE("较多时间兼职"),
        SOHO("全职 SOHO");

        public String alics;

        FreeTime(String s) {
            alics = s;
        }
    }

    public String getLocalString() {
        return String.format("%s %s %s", province.alias, city.alias, district.alias);
    }

//    enum Status {
//        NORMAL,
//        NOT_ACTIVATED,
//        ACTIVATED,
//        BLOCKED
//    }

}

