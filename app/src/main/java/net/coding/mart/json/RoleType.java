package net.coding.mart.json;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;

public class RoleType implements Serializable {

    private static final long serialVersionUID = -1227889303511600726L;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName(value = "show_order", alternate = {"showOrder"})
    @Expose
    public int showOrder;
    @SerializedName(value = "created_at", alternate = {"createdAt"})
    @Expose
    public String createdAt;
    @SerializedName(value = "updated_at", alternate = {"updatedAt"})
    @Expose
    public String updatedAt;
    @SerializedName("completed")
    @Expose
    public boolean completed = false;

    public RoleType() {
        id = 0;
        name = "";
        description = "";
        createdAt = "";
        updatedAt = "";
    }

    public RoleType(JSONObject json) {
        id = json.optInt("id");
        name = json.optString("name", "");
        description = json.optString("description", "");
        createdAt = json.optString("createdAt", "");
        updatedAt = json.optString("updatedAt", "");
    }

    /**
     *
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

}

