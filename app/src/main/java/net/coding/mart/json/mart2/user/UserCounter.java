package net.coding.mart.json.mart2.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chenchao on 2017/5/4.
 * 用户统计计数数据
 */

public class UserCounter implements Serializable {

    private static final long serialVersionUID = 6040057921587992907L;

    @SerializedName("joined")
    @Expose
    public int joined;
    @SerializedName("published")
    @Expose
    public int published;
    @SerializedName("processing")
    @Expose
    public int processing;
    @SerializedName("completed")
    @Expose
    public int completed;
}
