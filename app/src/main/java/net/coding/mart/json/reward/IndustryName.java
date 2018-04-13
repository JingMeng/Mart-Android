package net.coding.mart.json.reward;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by chenchao on 16/10/18.
 */

public class IndustryName implements Serializable {

    private static final long serialVersionUID = -7596116623875786520L;

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name = "";
    @SerializedName("createdAt")
    @Expose
    public String createdAt = "";
    @SerializedName("updatedAt")
    @Expose
    public String updatedAt = "";
    @SerializedName("description")
    @Expose
    public String description = "";
    @SerializedName("sort")
    @Expose
    public int sort;

    @NonNull
    public static String createName(ArrayList<IndustryName> industryNames) {
        StringBuffer buffer = new StringBuffer();
        boolean isFirst = true;
        for (IndustryName item : industryNames) {
            if (isFirst) {
                isFirst = false;
            } else {
                buffer.append(",");
            }

            buffer.append(item.name);
        }

        return buffer.toString();
    }

    @NonNull
    public static String createIdString(ArrayList<IndustryName> industryNames) {
        StringBuffer buffer = new StringBuffer();
        boolean isFirst = true;
        for (IndustryName item : industryNames) {
            if (isFirst) {
                isFirst = false;
            } else {
                buffer.append(",");
            }

            buffer.append(String.valueOf(item.id));
        }

        return buffer.toString();
    }

    public void setData(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
