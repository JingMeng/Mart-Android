package net.coding.mart.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by chenchao on 16/3/18.
 */
public class HttpResult<T> implements Serializable {

    private static final long serialVersionUID = 1371160288199830861L;

    @SerializedName("code")
    @Expose
    public int code;
    @SerializedName("msg")
    @Expose
    public Map<String, String> msg;
    @SerializedName("data")
    @Expose
    public T data;

    public String getErrorMessage() {
        if (msg != null && msg.size() > 0) {
            return msg.values().iterator().next();
        } else {
            return "未知错误";
        }
    }
}
