package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpPagerResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 16/9/1.
 */
public class FreezeDynamiicPager extends BaseHttpPagerResult implements Serializable {

    private static final long serialVersionUID = 72043814675835252L;

    @SerializedName("freeze")
    @Expose
    public List<FreezeDynamic> freezeDynamics = new ArrayList<>();
}
