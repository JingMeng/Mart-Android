package net.coding.mart.json.global;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 2017/9/28.
 */

public class Settings extends BaseHttpResult implements Serializable {

    @SerializedName("setting")
    @Expose
    public List<Setting> settings = new ArrayList<>(0);
}
