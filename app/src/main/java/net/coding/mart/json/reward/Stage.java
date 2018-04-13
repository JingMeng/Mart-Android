
package net.coding.mart.json.reward;

import android.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.common.constant.RewardStageConsts;
import net.coding.mart.common.constant.StageStatus;

import java.io.Serializable;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Stage extends BaseObservable implements Serializable {

    private static final long serialVersionUID = -115041920395764081L;

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName(value = "stage_no", alternate = {"stageNo"})
    @Expose
    public String stageNo = "";
    @SerializedName(value = "stage_task", alternate = {"stageTask"})
    @Expose
    public String stageTask = "";
    @SerializedName("deadline")
    @Expose
    public String deadline = "";
    @SerializedName(value = "deadline_timestamp", alternate = {"deadlineTimestamp"})
    @Expose
    public long deadlineTimestamp;
    @SerializedName(value = "deadline_check_timestamp", alternate = {"deadlineCheckTimestamp"})
    @Expose
    public long deadlineCheckTimestamp;
    @SerializedName("price")
    @Expose
    public String price = "";
    @SerializedName(value = "format_price", alternate = {"formatPrice"})
    @Expose
    public String formatPrice = "";
//    @SerializedName("status")
//    @Expose
//    public int status;
    @SerializedName(value = "status_desc", alternate = {"statusDesc"})
    @Expose
    public String statusDesc = "";
    @SerializedName(value = "stage_file", alternate = {"stageFile"})
    @Expose
    public String stageFile = "";
    @SerializedName(value = "stage_file_desc", alternate = {"stageFileDesc"})
    @Expose
    public String stageFileDesc = "";
    @SerializedName(value = "modify_file", alternate = {"modifyFile"})
    @Expose
    public String modifyFile = "";
    @SerializedName("planDays")
    @Expose
    public int planDays;
    @SerializedName("payed")
    @Expose
    public int payed;
    @SerializedName(value = "finish_time", alternate = {"finishTime"})
    @Expose
    public String finishTime = "";
    @SerializedName("planStartAtFormat")
    @Expose
    public String planStartAtFormat = "";
    @SerializedName("planFinishAtFormat")
    @Expose
    public String planFinishAtFormat = "";
    @SerializedName("factStartAtFormat")
    @Expose
    public String factStartAtFormat = "";
    @SerializedName("factFinishAtFormat")
    @Expose
    public String factFinishAtFormat = "";
    @SerializedName("statusText")
    @Expose
    public String statusText = "";
    @SerializedName("payedText")
    @Expose
    public String payedText = "";
    @SerializedName("statusOrigin")
    @Expose
    public int statusOrigin;

    public String getStatusString() {
        return StageStatus.id2Name(statusOrigin);
    }

    public int getStatusColor() {
//        return RewardStageConsts.getColor(statusOrigin);
        return StageStatus.id2Enum(statusOrigin).color;
    }

    public boolean showPayButton() {
        return payed == RewardStageConsts.PAYED_PENDING
                && getStatusEnum() == StageStatus.notStart;
    }

    public StageStatus getStatusEnum() {
        return StageStatus.id2Enum(statusOrigin);
    }

    public String getPlanTimeRange() {
        return String.format("%s - %s", planStartAtFormat, planFinishAtFormat);
    }

    public String getPlanTimeRangeOld() {
        return String.format("%s", planFinishAtFormat);
    }


    public String getFactTimeRange() {
        return String.format("%s - %s", factStartAtFormat, factFinishAtFormat);
    }

//    public String getDeveloperRange() {
//        return String.format()
//    }

    public String getPlanDays() {
        return String.valueOf(planDays);
    }

}
