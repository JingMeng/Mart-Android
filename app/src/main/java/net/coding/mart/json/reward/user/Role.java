package net.coding.mart.json.reward.user;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 16/10/13.
 */

public class Role implements Serializable {

    private static final long serialVersionUID = 4012348730356370659L;

    @SerializedName("roleName")
    @Expose
    public String roleName;
    @SerializedName("roleSkills")
    @Expose
    public List<String> roleSkills = new ArrayList<>();
    @SerializedName("specialSkill")
    @Expose
    public String specialSkill;

    public String nameInBracket() {
        return String.format("【%s】", roleName);
    }

    public boolean isSoftEngineer() {
        return "开发工程师".equals(roleName);
    }

    public String getSpecialSkill() {
        if (TextUtils.isEmpty(specialSkill)) {
            return "未填写";
        }

        return specialSkill;
    }
}
