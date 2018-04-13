package net.coding.mart.common.constant;

/**
 * Created by chenchao on 2017/1/20.
 * 认证阶段
 */
public enum IdentityStatus {
    UNCHECKED(0, ""),
    CHECKED(1, ""),
    REJECTED(2, ""),
    CHECKING(3, "");

    IdentityStatus(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int id;
    public String text;

    public static IdentityStatus id2Enum(int id) {
        for (IdentityStatus item : IdentityStatus.values()) {
            if (item.id == id) {
                return item;
            }
        }

        return UNCHECKED;
    }

}
