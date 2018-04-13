
package net.coding.mart.json.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Skill implements Serializable {

    private static final long serialVersionUID = 7906873612271909557L;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("code")
    @Expose
    public String code = "";
    @SerializedName("name")
    @Expose
    public String name = "";
    @SerializedName("description")
    @Expose
    public String description = "";
    @SerializedName("selected")
    @Expose
    public boolean selected;

}
