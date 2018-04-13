package net.coding.mart.json.v2.phase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;
import java.util.List;

public class Phases extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = -6872110986964757282L;

    @SerializedName("phases")
    @Expose
    public List<Phase> phases = null;

}
