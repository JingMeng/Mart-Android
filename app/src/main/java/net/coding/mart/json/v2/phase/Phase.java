package net.coding.mart.json.v2.phase;

import android.text.TextUtils;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.common.Global;

import java.io.Serializable;

public class Phase implements Serializable {

    private static final long serialVersionUID = -413207618943559695L;

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("projectId")
    @Expose
    public int projectId;
    @SerializedName("developerId")
    @Expose
    public int developerId;
    @SerializedName("creatorId")
    @Expose
    public int creatorId;
    @SerializedName("phaseNo")
    @Expose
    public String phaseNo;
    @SerializedName("status")
    @Expose
    public Status status;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("actualPrice")
    @Expose
    public String actualPrice = "";
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("deliveryNote")
    @Expose
    public String deliveryNote;
    @SerializedName("actualDeliveryAt")
    @Expose
    public long actualDeliveryAt;
    @SerializedName("planDeliveryAt")
    @Expose
    public long planDeliveryAt;
    @SerializedName("approval")
    @Expose
    public Approval approval;
    @SerializedName("evaluation")
    @Expose
    public Evaluation evaluation;

    // 有实际日期就显示实际交付日期，没有就显示计划交付日期
    public String getDeliveryString() {
        if (actualDeliveryAt > 0) {
            return String.format("交付日期：%s", Global.formatDay(actualDeliveryAt));
        }

        return String.format("交付日期：%s", Global.formatDay(planDeliveryAt));
    }

    public String getPriceString() {
        if (!TextUtils.isEmpty(actualPrice)) {
            return String.format("交付金额：¥ %s", actualPrice);
        }
        return String.format("交付金额：¥ %s", price);
    }

    public String getPlanString1() {
        return String.format("%s", Global.formatDay(planDeliveryAt));
    }

    public String getActualPlanString1() {
        return String.format("%s", Global.formatDay(actualDeliveryAt));
    }

    public String getActualPriceString1() {
        return String.format("¥ %s", actualPrice);
    }

    public String getPriceString1() {
        return String.format("¥ %s", price);
    }

    public int showActual() {
        return (status == Status.FINISHED || status == Status.TERMINATED) ? View.VISIBLE : View.GONE;
    }

    public int showActualDate() {
        return (showActual() == View.VISIBLE && actualDeliveryAt > 0) ? View.VISIBLE : View.GONE;
    }

    public String getPString1() {
        return phaseNo + " 阶段";
    }

    public enum Status {
        UNKNOWN_STATUS(0, "未知", 0xFFDDE3EB),
        CREATE(1, "阶段划分", 0xFFDDE3EB),
        UNPAID(2, "未启动", 0xFFDDE3EB),
        DEVELOPING(3, "开发中", 0xFF4289DB),
        TERMINATED(4, "已中止", 0xFF979FA8),
        CHECKING(5, "待验收", 0xFFE3935D),
        FINISHED(6, "已验收", 0xFF61C279),
        EDIT_ADD(7, "未启动", 0xFFDDE3EB),
        CHECK_FAILED(8, "验收未通过", 0xFFE72511),
        DELETED(11, "已删除", 0xFFDDE3EB);  // NOT IN DATABASE

        public int id;
        public String alics;
        public int color;

        Status(int id, String alics, int color) {
            this.id = id;
            this.alics = alics;
            this.color = color;
        }

        public int getColor() {
            return color;
        }
    }
}