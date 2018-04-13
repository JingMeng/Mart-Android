
package net.coding.mart.json.user.identity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;

public class IdentityUserInfo extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = -3878771890417868892L;

    public static final int STATUS_CHECKED = 1; // 通过身份认证

    @SerializedName("userId")
    @Expose
    public int userId;
    @SerializedName("name")
    @Expose
    public String name = "";
    @SerializedName("identity")
    @Expose
    public String identity = "";
    @SerializedName("email")
    @Expose
    public String email = "";
    @SerializedName("status")
    @Expose
    public String status = "";

    public boolean isCheking() {
        return status.toLowerCase().equals("checking");
    }

    public boolean isPassed() {
        return status.toLowerCase().equals("checked");
    }

    public boolean isNew() {
        String low = status.toLowerCase();
        return low.equals("new") || low.equals("rejected") || low.equals("canceled");
    }

}
