package net.coding.mart.json.mpay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chenchao on 16/8/5.
 */
public class InvoiceAccountSimple implements Serializable {

    private static final long serialVersionUID = -6601068766899177469L;

    @SerializedName("userId")
    @Expose
    public int userId;
    @SerializedName("invoiceNo")
    @Expose
    public String invoiceNo = "";
}
