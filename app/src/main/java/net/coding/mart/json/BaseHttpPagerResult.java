package net.coding.mart.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chenchao on 16/9/1.
 */
public class BaseHttpPagerResult extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = -3890362220178290161L;

    @SerializedName("pager")
    @Expose
    public PageInfo pageInfo;
}
