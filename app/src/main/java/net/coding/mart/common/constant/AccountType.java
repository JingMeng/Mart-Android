package net.coding.mart.common.constant;

import java.io.Serializable;

/**
 * Created by chenchao on 16/6/15.
 * 用户类型，开发者 需求方
 */
public enum AccountType implements Serializable {

    DEVELOPER(1),
    DEMAND(2);

    AccountType(int id) {
        this.id = id;
    }

    public int id;

    public static AccountType id2Enum(int id) {
        for (AccountType item : values()) {
            if (item.id == id) {
                return item;
            }
        }

        return DEVELOPER;
    }

    public boolean isPublish() {
        return this == DEMAND;
    }
}
