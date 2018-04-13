package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderMapperTrade implements Serializable {

    private static final long serialVersionUID = -1456726469466294037L;

    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("value")
    @Expose
    public String value;
    @SerializedName("names")
    @Expose
    public List<String> names;
}
