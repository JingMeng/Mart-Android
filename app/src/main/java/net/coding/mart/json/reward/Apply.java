
package net.coding.mart.json.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Apply implements Serializable {

    private static final long serialVersionUID = -7872321142278892442L;

    @SerializedName("coders")
    @Expose
    public ArrayList<Coder> coders = new ArrayList<>();
}
