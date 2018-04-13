package net.coding.mart.json.mart2.user;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class City implements Serializable {

    private static final long serialVersionUID = -7833741118983884888L;

    @SerializedName("parentId")
    @Expose
    public int parentId;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name = "";
    @SerializedName("alias")
    @Expose
    public String alias = "";
    @SerializedName("pinyin")
    @Expose
    public String pinyin = "";
    @SerializedName("abbr")
    @Expose
    public String abbr = "";
    @SerializedName("zip")
    @Expose
    public String zip = "";
    @SerializedName("level")
    @Expose
    public int level;

    public void clear() {
        id = 0;
        name = "";
    }
}
