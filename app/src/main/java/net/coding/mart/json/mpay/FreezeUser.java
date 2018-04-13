package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chenchao on 16/9/1.
 */
public class FreezeUser implements Serializable {

    private static final long serialVersionUID = 1188169307985452166L;

    @SerializedName("userId")
    @Expose
    public int userId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("globalKey")
    @Expose
    public String globalKey;
    @SerializedName("email")
    @Expose
    public String email;
}
