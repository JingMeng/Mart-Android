package net.coding.mart.json;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by chenchao on 16/2/1.
 */
public class ChargePayed implements Serializable {
    private static final long serialVersionUID = -1905124922968474692L;
    int id;
    //    user, // 用户对象
//    reward, // 项目对象
    String created_at = "";
    String updated_at = "";
    int status; // 0：待支付，1：交易成功，2：交易关闭
    double price;
    String format_price = "";
    String platform = ""; // alipay 或 wechat

    public ChargePayed(JSONObject json) {
        id = json.optInt("id", 0);
//    user, // 用户对象
//    reward, // 项目对象
        created_at = json.optString("created_at", "");
        updated_at = json.optString("updated_at", "");
        status = json.optInt("status"); // 0：待支付，1：交易成功，2：交易关闭
        price = json.optDouble("price");
        format_price = json.optString("format_price", "");
        platform = json.optString("platform", ""); // alipay 或 wechat
    }

    public boolean payedSuccess() {
        return status == 1;
    }

    public double getPrice() {
        return price;
    }
}
