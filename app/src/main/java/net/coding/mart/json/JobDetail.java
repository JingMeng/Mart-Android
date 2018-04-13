package net.coding.mart.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.common.constant.JoinStatus;
import net.coding.mart.json.reward.Published;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 15/11/6.
 */
public class JobDetail implements Serializable {

    private static final long serialVersionUID = -4986929353880307899L;

    public JoinStatus getJoinStatus() {
        return JoinStatus.id2Enum(joinTypeId);
    }

    @SerializedName("reward")
    @Expose
    public Published reward;
    @SerializedName("roleTypes")
    @Expose
    public List<RoleType> roleTypes = new ArrayList<>();
    @SerializedName("joinStatus")
    @Expose
    public int joinTypeId = JoinStatus.noJoin.id;
    @SerializedName("isOwner")
    @Expose
    public boolean isOwner;
}
