package net.coding.mart.json.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Created by chenchao on 16/8/23.
 */
public class JoinState {

    @SerializedName(value = "owner_id", alternate = {"ownerId"})
    @Expose
    public int ownerId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("province")
    @Expose
    public int province;
    @SerializedName("city")
    @Expose
    public int city;
    @SerializedName("district")
    @Expose
    public int district;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName(value = "first_link", alternate = {"firstLink"})
    @Expose
    public String firstLink;
    @SerializedName(value = "second_link", alternate = {"secondLink"})
    @Expose
    public String secondLink;
    @SerializedName(value = "third_link", alternate = {"thirdLink"})
    @Expose
    public String thirdLink;
    @SerializedName("skill")
    @Expose
    public String skill;
    @SerializedName("specialty")
    @Expose
    public String specialty;
    @SerializedName("alipay")
    @Expose
    public String alipay;
    @SerializedName(value = "created_at", alternate = {"createdAt"})
    @Expose
    public String createdAt;
    @SerializedName(value = "updated_at", alternate = {"updatedAt"})
    @Expose
    public String updatedAt;
    @SerializedName(value = "phone_validation", alternate = {"phoneValidation"})
    @Expose
    public int phoneValidation;
    @SerializedName(value = "email_validation", alternate = {"emailValidation"})
    @Expose
    public int emailValidation;
    @SerializedName("wechat")
    @Expose
    public String wechat;
    @SerializedName("qq")
    @Expose
    public String qq;
    @SerializedName(value = "work_type", alternate = {"workType"})
    @Expose
    public String workType;
    @SerializedName(value = "current_job", alternate = {"currentJob"})
    @Expose
    public int currentJob;
    @SerializedName(value = "career_years", alternate = {"careerYears"})
    @Expose
    public int careerYears;
    @SerializedName("identity")
    @Expose
    public String identity;
    @SerializedName(value = "identity_img_front", alternate = {"identityImgFront"})
    @Expose
    public String identityImgFront;
    @SerializedName(value = "identity_img_back", alternate = {"identityImgBack"})
    @Expose
    public String identityImgBack;
    @SerializedName(value = "identity_img_auth", alternate = {"identityImgAuth"})
    @Expose
    public String identityImgAuth;
    @SerializedName(value = "total_fee", alternate = {"totalFee"})
    @Expose
    public BigDecimal totalFee;
    @SerializedName(value = "left_fee", alternate = {"leftFee"})
    @Expose
    public BigDecimal leftFee;
    @SerializedName(value = "work_exp", alternate = {"workExp"})
    @Expose
    public String workExp;
    @SerializedName(value = "project_exp", alternate = {"projectExp"})
    @Expose
    public String projectExp;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName(value = "reject_reason", alternate = {"rejectReason"})
    @Expose
    public int rejectReason;
    @SerializedName(value = "free_time", alternate = {"freeTime"})
    @Expose
    public int freeTime;
    @SerializedName(value = "reward_role", alternate = {"rewardRole"})
    @Expose
    public int rewardRole;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName(value = "phone_country_code", alternate = {"phoneCountryCode"})
    @Expose
    public String phoneCountryCode;
    @SerializedName(value = "info_complete", alternate = {"infoComplete"})
    @Expose
    public int infoComplete;
    @SerializedName(value = "skills_complete", alternate = {"skillsComplete"})
    @Expose
    public int skillsComplete;
    @SerializedName(value = "survey_complete", alternate = {"surveyComplete"})
    @Expose
    public int surveyComplete;
    @SerializedName("id")
    @Expose
    public int id;
}
