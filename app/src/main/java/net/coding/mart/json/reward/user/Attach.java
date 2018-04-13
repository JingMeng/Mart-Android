package net.coding.mart.json.reward.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chenchao on 16/10/13.
 */

public class Attach implements Serializable {

    private static final long serialVersionUID = 5440745927131081575L;

    @SerializedName("fileName")
    @Expose
    public String fileName;
    @SerializedName("fileUrl")
    @Expose
    public String fileUrl;

}
