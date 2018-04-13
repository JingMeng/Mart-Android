package net.coding.mart.common.constant;

import java.io.Serializable;

/**
 * Created by chenchao on 16/11/22.
 * 火锅的类型
 */

public enum Huoguo implements Serializable {

    web("P001"),
    weixin("P004"),
    ios("P002"),
    android("P003"),
    front("P005"),
    background("P006"),
    h5("P007"),
    crawler("P008");

    public static final String[] idsSort = new String[]{
            web.code, weixin.code, ios.code, android.code, background.code, front.code, h5.code, crawler.code
    };

    public String code;

    Huoguo(String code) {
        this.code = code;
    }
}
