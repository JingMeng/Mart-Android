package net.coding.mart.json.enterprise;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;

public class EnterpriseCertificate extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = 8173115697522569108L;

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("userId")
    @Expose
    public int userId;
    @SerializedName("legalRepresentative")
    @Expose
    public String legalRepresentative;
    @SerializedName("businessLicenceNo")
    @Expose
    public String businessLicenceNo;
    @SerializedName("businessLicenceImg")
    @Expose
    public int businessLicenceImg;
    @SerializedName("status")
    @Expose
    public String status = "";
    @SerializedName("rejectReason")
    @Expose
    public String rejectReason = "";
    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("attachment")
    @Expose
    public Attachment attachment;
    @SerializedName("createdAt")
    @Expose
    public long createdAt;

}
