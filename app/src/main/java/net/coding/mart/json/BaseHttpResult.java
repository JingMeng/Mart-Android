package net.coding.mart.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by chenchao on 16/3/18.
 * 不关心返回数据
 */
public class BaseHttpResult implements Serializable {

    private static final long serialVersionUID = -6309087919316007901L;

    @SerializedName("code")
    @Expose
    public int code = 0;
    @SerializedName("msg")
    @Expose
    public Map<String, String> msg;

    public String getErrorMessage() {
        if (msg != null && msg.size() > 0) {
            return msg.values().iterator().next();
        } else {
            return "未知错误";
        }
    }
}
