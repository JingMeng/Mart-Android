package net.coding.mart.json.user.exam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import javax.annotation.Generated;

/**
 * Created by chenchao on 16/10/27.
 */

@Generated("org.jsonschema2pojo")
public class Score implements Serializable {

    private static final long serialVersionUID = 8058843397763911125L;

    @SerializedName(value = "user_id", alternate = {"userId"})
    @Expose
    public int userId;
    @SerializedName("total")
    @Expose
    public int total;
    @SerializedName("wrong")
    @Expose
    public int wrong;
    @SerializedName("correct")
    @Expose
    public int correct;
    @SerializedName(value = "created_at", alternate = {"createdAt"})
    @Expose
    public String createdAt;
    @SerializedName("id")
    @Expose
    public int id;

}
