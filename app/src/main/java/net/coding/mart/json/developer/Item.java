package net.coding.mart.json.developer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Created by liu on 16/6/21.
 */
@Generated("org.jsonschema2pojo")
public class Item {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("quoteCaseId")
    @Expose
    public Integer quoteCaseId;
    @SerializedName("code")
    @Expose
    public String code;
    @SerializedName("parentCode")
    @Expose
    public String parentCode;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("type")
    @Expose
    public Integer type;
    @SerializedName("valueType")
    @Expose
    public Integer valueType;
    @SerializedName("domType")
    @Expose
    public Integer domType;

    public boolean isFrontPlatform() {
        return type == 1 && code.equals("P005");
    }

}