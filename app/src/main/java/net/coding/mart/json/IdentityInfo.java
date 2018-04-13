package net.coding.mart.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chenchao on 16/3/24.
 */
public class IdentityInfo implements Serializable {

    private static final long serialVersionUID = -4180314455843688625L;
    @SerializedName(value = "identity_img_front", alternate = {"identityImgFront"})
    @Expose
    public String identityImgFront = "";
    @SerializedName("alipay")
    @Expose
    public String alipay = "";
    @SerializedName("identity")
    @Expose
    public String identity = "";
    @SerializedName("name")
    @Expose
    public String name = "";
    @SerializedName(value = "identity_img_auth", alternate = {"identityImgAuth"})
    @Expose
    public String identityImgAuth = "";
    @SerializedName(value = "identity_img_back", alternate = {"identityImgBack"})
    @Expose
    public String identityImgBack = "";
    @SerializedName("status")
    @Expose
    public String status = "0";
    @SerializedName(value = "reject_reason_text", alternate = {"rejectReasonText"})
    @Expose
    public String rejectReasonText = "";
    @SerializedName(value = "reject_detail", alternate = {"rejectDetail"})
    @Expose
    public String rejectDetail = "";

    public int getStatus() {
        try {
            return Integer.valueOf(status);
        } catch (Exception e) {
            return 0;
        }
    }

}
