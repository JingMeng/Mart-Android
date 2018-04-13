package net.coding.mart.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chenchao on 16/4/1.
 * 广告 banner 数据
 */
public class Banner implements Serializable {
    private static final long serialVersionUID = -6058941342983340998L;
    @SerializedName("name")
    @Expose
    public String name = "";
    public String code = "";
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName(value = "created_at", alternate = {"createdAt"})
    @Expose
    public String createdAt = "";
    @SerializedName(value = "updated_at", alternate = {"updatedAt"})
    @Expose
    public String updatedAt = "";
    @SerializedName("title")
    @Expose
    public String title = "";
    @SerializedName("link")
    @Expose
    public String link = "";
    @SerializedName("image")
    @Expose
    public String image = "";
    @SerializedName("sort")
    @Expose
    public int sort;
    @SerializedName("id")
    @Expose
    public int id;
}
