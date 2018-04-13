package net.coding.mart.json.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;
import net.coding.mart.json.mart2.user.MartUser;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenchao on 2017/10/24.
 */

public class V2Owners extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = 7838400197225818135L;

    @SerializedName("user")
    @Expose
    public List<MartUser> owners;
}
