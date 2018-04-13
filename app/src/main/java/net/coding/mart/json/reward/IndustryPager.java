package net.coding.mart.json.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpPagerResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 16/10/18.
 */

public class IndustryPager extends BaseHttpPagerResult implements Serializable {

    private static final long serialVersionUID = 3124139824926825511L;

    @SerializedName("industryName")
    @Expose
    public List<IndustryName> industryName = new ArrayList<>();
}
