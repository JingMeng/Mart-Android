package net.coding.mart.json.v2.phase;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Approval implements Serializable {

    private static final long serialVersionUID = 8011703008892903222L;

    @SerializedName("creatorId")
    @Expose
    public int creatorId;
    @SerializedName("approverId")
    @Expose
    public int approverId;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("submittedAt")
    @Expose
    public long submittedAt;

}
