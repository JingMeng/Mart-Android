
package net.coding.mart.json.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.File;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Prd implements Serializable {

    private static final long serialVersionUID = 6130349249318471320L;
    @SerializedName("filesToShow")
    @Expose
    public List<File> filesToShow = new ArrayList<>();

}
