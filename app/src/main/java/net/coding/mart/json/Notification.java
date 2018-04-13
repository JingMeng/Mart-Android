package net.coding.mart.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by chenchao on 15/11/9.
 */
public class Notification implements Serializable {

    private static final long serialVersionUID = -754845093297390824L;

    private static final String REG_REMOVE_HTML = "<([B-Zb-z][A-Za-z0-9]*)[^>]*>(.*?)</\\1>"; // 不包括 <a
    private static final String REG_REPLACEMENT = "$2";

    static SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat("MMM d, yyyy hh:mm:ss a", Locale.US);

    @SerializedName(value = "user_id", alternate = {"userId"})
    @Expose
    public int user_id;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("type")
    @Expose
    public int type;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("remind")
    @Expose
    public int remind;
    @SerializedName(value = "created_at", alternate = {"createdAt"})
    @Expose
    public String created_at;
    @SerializedName("id")
    @Expose
    public int id;

    public void setStatusRead() {
        this.status = 1;
    }

    public int getStatus() {
        return status;
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content.replaceAll(REG_REMOVE_HTML, REG_REPLACEMENT);
    }

    public Date getCreated_at() {
        try {
            return simpleDateFormat.parse(created_at);
        } catch (ParseException exception) {
            return new Date();
        }
    }
    public boolean isRead() {
        return status != 0;
    }
}
