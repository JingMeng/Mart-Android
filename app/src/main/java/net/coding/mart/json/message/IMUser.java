package net.coding.mart.json.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chenchao on 2017/2/14.
 */

public class IMUser implements Serializable {

    private static final long serialVersionUID = -7718604176385810928L;

    @SerializedName("userId")
    @Expose
    public int userId;
    @SerializedName("uid")
    @Expose
    public String uid = ""; // gk
    @SerializedName("nick")
    @Expose
    public String nick = "";
    @SerializedName("avatar")
    @Expose
    public String avatar = "";
    @SerializedName("identifier")
    @Expose
    public String identifier = ""; // sig
    @SerializedName("status")
    @Expose
    public String status = "";
}
