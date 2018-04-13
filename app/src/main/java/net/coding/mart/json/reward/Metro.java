
package net.coding.mart.json.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class Metro implements Serializable {

    private static final long serialVersionUID = -316097837220456425L;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("hangStatus")
    @Expose
    public List<Integer> hangStatus = new ArrayList<>();
    @SerializedName("allStatus")
    @Expose
    public AllStatus allStatus;
    @SerializedName("roles")
    @Expose
    public List<Role> roles = new ArrayList<>();
    @SerializedName("allStages")
    @Expose
    public AllStages allStages;
    @SerializedName("stageColors")
    @Expose
    public List<String> stageColors = new ArrayList<>();

}
