package net.coding.mart.json.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Owner implements Serializable {

    private static final long serialVersionUID = 8652652995357264519L;

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName(value = "created_at", alternate = {"createdAt"})
    @Expose
    public String createdAt = "";
    @SerializedName(value = "last_logined_at", alternate = {"lastLoginedAt"})
    @Expose
    public String lastLoginedAt = "";
    @SerializedName(value = "global_key", alternate = {"globalKey"})
    @Expose
    public String globalKey = "";
    @SerializedName("avatar")
    @Expose
    public String avatar = "";
    @SerializedName("gravatar")
    @Expose
    public String gravatar = "";
    @SerializedName("name")
    @Expose
    public String name = "";
    @SerializedName(value = "name_pinyin", alternate = {"namePinyin"})
    @Expose
    public String namePinyin = "";
    @SerializedName("phone")
    @Expose
    public String phone = "";
    @SerializedName("qq")
    @Expose
    public String qq = "";
    @SerializedName("email")
    @Expose
    public String email = "";
    @SerializedName("sex")
    @Expose
    public int sex;
    @SerializedName("slogan")
    @Expose
    public String slogan = "";
    @SerializedName("ip")
    @Expose
    public String ip = "";
    @SerializedName("home")
    @Expose
    public String home = "";
//    @SerializedName("status")
//    @Expose
//    public int status;

}
