package net.coding.mart.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chenchao on 16/6/24.
 * 新的 pagerinfo, 适用于服务器使用了 protobuf
 */
public class PageInfo implements Serializable {

    private static final long serialVersionUID = 8476418472655876413L;

    @SerializedName("page")
    @Expose
    public int page;
    @SerializedName("pageSize")
    @Expose
    public int pageSize;
    @SerializedName("totalPage")
    @Expose
    public int totalPage;
    @SerializedName("totalRow")
    @Expose
    public int totalRow;

}
