package net.coding.mart.json.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by chenchao on 16/8/11.
 */
public class StagePay implements Serializable {

    private static final long serialVersionUID = 3422714850261243152L;

    @SerializedName("totalPayedPrice")
    @Expose
    public BigDecimal totalPayedPrice;
    @SerializedName("totalPayedPriceFormat")
    @Expose
    public String totalPayedPriceFormat;
    @SerializedName("totalPayedStageCount")
    @Expose
    public int totalPayedStageCount;
    @SerializedName("totalDevelopingPrice")
    @Expose
    public BigDecimal totalDevelopingPrice;
    @SerializedName("totalDevelopingPriceFormat")
    @Expose
    public String totalDevelopingPriceFormat;
    @SerializedName("totalDevelopingStageCount")
    @Expose
    public int totalDevelopingStageCount;
    @SerializedName("totalPendingPrice")
    @Expose
    public BigDecimal totalPendingPrice;
    @SerializedName("totalPendingPriceFormat")
    @Expose
    public String totalPendingPriceFormat;
    @SerializedName("totalPendingStageCount")
    @Expose
    public int totalPendingStageCount;

    public String getPayed() {
        return totalPayedPriceFormat;
    }

    public String getDeveloping() {
        return totalDevelopingPriceFormat;
    }

    public String getPending() {
        return totalPendingPriceFormat;
    }

    public String getTotalDevelopingCount() {
        return String.format("%s 个阶段开发中", totalDevelopingStageCount);
    }

    public String getPedingStageCount() {
        return String.format("%s 个阶段待启动", totalPendingStageCount);
    }

}
