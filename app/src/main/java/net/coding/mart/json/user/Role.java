
package net.coding.mart.json.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Role implements Serializable {

    private static final long serialVersionUID = 1253387963581008721L;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("code")
    @Expose
    public String code = "";
    @SerializedName("name")
    @Expose
    public String name = "";
    @SerializedName("data")
    @Expose
    public String data = "";
    @SerializedName("selected")
    @Expose
    public boolean selected;
    @SerializedName("description")
    @Expose
    public String description = "";

    public static int SOFT_ENGINEER_ID = 1;

}
