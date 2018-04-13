package net.coding.mart.json.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserExtraRole implements Serializable {
    private static final long serialVersionUID = 3360596878605604614L;
    @SerializedName("skills")
    @Expose
    public List<Skill> skills = new ArrayList<Skill>();
    @SerializedName(value = "user_role", alternate = {"userRole"})
    @Expose
    public UserRole userRole;
    @SerializedName("role")
    @Expose
    public Role role;

    public boolean isSoftEngineer() {
        return role != null && role.id == Role.SOFT_ENGINEER_ID;
    }
}
