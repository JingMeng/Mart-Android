
package net.coding.mart.json.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserRole implements Serializable {

    private static final long serialVersionUID = 7435905779603510382L;
    @SerializedName(value = "user_id", alternate = {"userId"})
    @Expose
    public int userId;
    @SerializedName(value = "role_id", alternate = {"roleId"})
    @Expose
    public int roleId;
    @SerializedName(value = "good_at", alternate = {"goodAt"})
    @Expose
    public String goodAt = "";
    @SerializedName("abilities")
    @Expose
    public String abilities = "";
    @SerializedName(value = "created_at", alternate = {"createdAt"})
    @Expose
    public String createdAt = "";
    @SerializedName(value = "updated_at", alternate = {"updatedAt"})
    @Expose
    public String updatedAt = "";
    @SerializedName("id")
    @Expose
    public int id;

}
