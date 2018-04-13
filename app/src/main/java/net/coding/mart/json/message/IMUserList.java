package net.coding.mart.json.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 2017/2/14.
 */

public class IMUserList extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = 4610983964126115411L;

    @SerializedName("user")
    @Expose
    public List<IMUser> user = new ArrayList<>(0);

}
