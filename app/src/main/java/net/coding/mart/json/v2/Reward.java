package net.coding.mart.json.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.RoleType;
import net.coding.mart.json.user.Owner;

import java.io.Serializable;

/**
 * Created by chenchao on 2017/10/19.
 */

public class Reward implements Serializable {

    private static final long serialVersionUID = 9212976582682593791L;

    @SerializedName("ownerId")
    @Expose
    public int ownerId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("cover")
    @Expose
    public String cover;
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("roles")
    @Expose
    public String roles;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("duration")
    @Expose
    public int duration;
    @SerializedName("applyCount")
    @Expose
    public int applyCount;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("owner")
    @Expose
    public Owner owner;
    @SerializedName("createdAt")
    @Expose
    public String createdAt;
    @SerializedName("roleType")
    @Expose
    public RoleType roleType;
    @SerializedName("developerRole")
    @Expose
    public int developerRole;
    @SerializedName("statusText")
    @Expose
    public String statusText;
    @SerializedName("typeText")
    @Expose
    public String typeText;
    @SerializedName("visitCount")
    @Expose
    public int visitCount;
}
