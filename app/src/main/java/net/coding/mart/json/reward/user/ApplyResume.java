package net.coding.mart.json.reward.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 16/10/13.
 */

public class ApplyResume extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = 2168202475690144820L;

    @SerializedName("userId")
    @Expose
    public int userId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("devType")
    @Expose
    public String devType;
    @SerializedName("devLocation")
    @Expose
    public String devLocation;
    @SerializedName("reason")
    @Expose
    public String reason;
    @SerializedName("roles")
    @Expose
    public List<Role> roles = new ArrayList<>();
    @SerializedName("projects")
    @Expose
    public List<Project> projects = new ArrayList<Project>();
    @SerializedName("auth")
    @Expose
    public boolean auth;
    @SerializedName("excellent")
    @Expose
    public boolean excellent;
}
