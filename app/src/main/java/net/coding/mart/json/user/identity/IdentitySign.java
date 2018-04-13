package net.coding.mart.json.user.identity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;

public class IdentitySign extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = 1697516462580699584L;

    @SerializedName("userId")
    @Expose
    public int userId;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("documentId")
    @Expose
    public String documentId;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("signerUri")
    @Expose
    public String signerUri;
    @SerializedName("signerUriQr")
    @Expose
    public String signerUriQr;
}
