package net.coding.mart.json.user;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginIdentityData implements Serializable {

    private static final long serialVersionUID = 7245352494163455034L;
    @SerializedName("loginIdentity")
    @Expose
    public int loginIdentity;
    @SerializedName("publishedCount")
    @Expose
    public int publishedCount;
    @SerializedName("joinedCount")
    @Expose
    public int joinedCount;

}