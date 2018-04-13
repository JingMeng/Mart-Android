package net.coding.mart.common.constant;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by chenchao on 16/8/25.
 */
public class RewardStageConsts {

    public final static short PAYED_UNPAY = 1;  // "未付款"
    public final static short PAYED_PENDING = 2; // "待付款"
    public final static short PAYED_DONE = 3; // "已付款"
    public final static short PAYED_SUCCESS = 4; // "交易完成"
    public final static short PAYED_REFUND = 5; // "已退款"

    private final static HashMap<Short, String> PAYED = new HashMap<Short, String>() {{
        put(PAYED_UNPAY, "未支付");
        put(PAYED_DONE, "已支付");
        put(PAYED_PENDING, "待支付");
        put(PAYED_SUCCESS, "交易完成");
        put(PAYED_REFUND, "已退款");
    }};


    public static String getPayedName(int payed) {
        String name = PAYED.get(payed);
        if (TextUtils.isEmpty(name)) {
            name = "";
        }

        return name;
    }

    // 是否曾经付过款，退款也算
    public static boolean isPayed(int payed) {
        switch (payed) {
            case PAYED_UNPAY:
            case PAYED_PENDING:
                return false;
            default:
                return true;
        }
    }

}
