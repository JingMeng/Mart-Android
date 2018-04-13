
package net.coding.mart.json.user.identity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;

public class IdentityCheck extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = 6390121176915002493L;

    @SerializedName("userId")
    @Expose
    public int userId;
    @SerializedName("name")
    @Expose
    public String name = "";
    @SerializedName("identity")
    @Expose
    public String identity = "";
    @SerializedName("message")
    @Expose
    public String message = "";
    @SerializedName("result")
    @Expose
    public boolean result = false;
}
