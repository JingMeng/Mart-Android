package net.coding.mart.json.v2.phase;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Evaluation implements Serializable {

    private static final long serialVersionUID = 3215620783326490489L;

    @SerializedName("rewardId")
    @Expose
    public int rewardId;
    @SerializedName("stageId")
    @Expose
    public int stageId;
    @SerializedName("appraiseeId")
    @Expose
    public int appraiseeId;
    @SerializedName("appraiserId")
    @Expose
    public int appraiserId;
    @SerializedName("appraiserType")
    @Expose
    public String appraiserType = "";
    @SerializedName("deliverabilityRate")
    @Expose
    public String deliverabilityRate = "";
    @SerializedName("communicationRate")
    @Expose
    public String communicationRate = "";
    @SerializedName("responsibilityRate")
    @Expose
    public String responsibilityRate = "";
    @SerializedName("averageRate")
    @Expose
    public String averageRate = "0";
    @SerializedName("content")
    @Expose
    public String content = "";
    @SerializedName("createdAt")
    @Expose
    public String createdAt = "";
    @SerializedName("id")
    @Expose
    public int id;

    public boolean rateIsEmpty() {
        return TextUtils.isEmpty(averageRate) || averageRate.equals("0");
    }

    public String getContentString() {
        if (TextUtils.isEmpty(content)) {
            return "未填写";
        }
        return content;
    }

    public String getDeliverabilityRateString() {
        if (TextUtils.isEmpty(deliverabilityRate)) {
            return "未评价";
        }
        return deliverabilityRate;
    }

    public String getCommunicationRateString() {
        if (TextUtils.isEmpty(communicationRate)) {
            return "未评价";
        }
        return communicationRate;
    }

    public String getResponsibilityRateString() {
        if (TextUtils.isEmpty(responsibilityRate)) {
            return "未评价";
        }
        return responsibilityRate;
    }
}