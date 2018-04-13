package net.coding.mart.json.enterprise;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 7272305034594616576L;

    @SerializedName("userId")
    @Expose
    public int userId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("globalKey")
    @Expose
    public String globalKey;
    @SerializedName("auth")
    @Expose
    public boolean auth;
    @SerializedName("evaluation")
    @Expose
    public float evaluation;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("freeTime")
    @Expose
    public String freeTime;
    @SerializedName("province")
    @Expose
    public String province;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("identity")
    @Expose
    public String identity;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("excellentDeveloper")
    @Expose
    public boolean excellentDeveloper;
    @SerializedName("accountType")
    @Expose
    public String accountType;

}
