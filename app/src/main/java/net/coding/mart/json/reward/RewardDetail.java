
package net.coding.mart.json.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Deprecated
public class RewardDetail implements Serializable {

    private static final long serialVersionUID = 3706520922569892264L;
    @SerializedName("basicInfo")
    @Expose
    public BasicInfo basicInfo;
    @SerializedName("metro")
    @Expose
    public Metro metro;
    @SerializedName("apply")
    @Expose
    public Apply apply;
    @SerializedName("prd")
    @Expose
    public Prd prd;
    @SerializedName("stagePay")
    @Expose
    public StagePay stagePay;
}
