package net.coding.mart.json.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpPagerResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 2017/10/20.
 */

public class V2ApplyPager extends BaseHttpPagerResult implements Serializable {

    private static final long serialVersionUID = -4840717453922760765L;

    @SerializedName("apply")
    @Expose
    public List<V2Apply> applys = new ArrayList<>(0);
}
