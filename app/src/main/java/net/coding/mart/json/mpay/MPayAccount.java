package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 16/8/4.
 */
public class MPayAccount extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = 6903425236710674805L;

    @SerializedName("account")
    @Expose
    public Account account;
    @SerializedName("platforms")
    @Expose
    public List<String> platforms = new ArrayList<>();
}
