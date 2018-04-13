package net.coding.mart.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class File implements Serializable {

    private static final long serialVersionUID = -1577258917951839387L;
    @SerializedName("filename")
    @Expose
    public String filename = "";
    @SerializedName("url")
    @Expose
    public String url = "";
    @SerializedName("extension")
    @Expose
    public String extension = "";
    @SerializedName("size")
    @Expose
    public int size;
    @SerializedName(value = "created_at", alternate = {"createdAt"})
    @Expose
    public String createdAt = "";
    @SerializedName("id")
    @Expose
    public int id;

}