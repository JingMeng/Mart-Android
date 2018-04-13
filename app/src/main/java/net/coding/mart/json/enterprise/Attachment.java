package net.coding.mart.json.enterprise;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Attachment implements Serializable {

    private static final long serialVersionUID = -5499619603134177795L;

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("filename")
    @Expose
    public String filename;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("extension")
    @Expose
    public String extension;
    @SerializedName("size")
    @Expose
    public int size;

}
