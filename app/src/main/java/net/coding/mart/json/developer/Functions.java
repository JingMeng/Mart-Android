package net.coding.mart.json.developer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chenchao on 16/5/27.
 */
public class Functions implements Serializable {

    private static final long serialVersionUID = 7792359215517889805L;
    @SerializedName("quotations")
    @Expose
    public HashMap<String, Quotation> quotations = new HashMap<>();
    @SerializedName("platforms")
    @Expose
    public ArrayList<String> platforms = new ArrayList<>();

}
