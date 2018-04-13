
package net.coding.mart.json.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.common.MyData;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Role implements Serializable {

    private static final long serialVersionUID = -3510808969835889336L;

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName(value = "owner_id", alternate = {"ownerId"})
    @Expose
    public int ownerId;
    @SerializedName("name")
    @Expose
    public String name = "";
    @SerializedName(value = "role_name", alternate = {"roleName"})
    @Expose
    public String roleName = "";
    @SerializedName("description")
    @Expose
    public String description = "";
    @SerializedName(value = "total_price", alternate = {"totalPrice"})
    @Expose
    public BigDecimal totalPrice;
    @SerializedName(value = "assistant_name", alternate = {"assistantName"})
    @Expose
    public String assistantName = "";
    @SerializedName(value = "assistant_global_key", alternate = {"assistantGlobalKey"})
    @Expose
    public String assistantGlobalKey = "";
    @SerializedName("stages")
    @Expose
    public List<Stage> stages = new ArrayList<>();
    @SerializedName("roleSkills")
    @Expose
    public List<String> roleSkills = new ArrayList<>();
    @SerializedName("specialSkill")
    @Expose
    public String specialSkill;

    public boolean isMe() {
        return MyData.getInstance().getData().getId() == ownerId;
    }

}
