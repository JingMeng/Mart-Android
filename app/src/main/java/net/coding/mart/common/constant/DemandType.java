package net.coding.mart.common.constant;

/**
 * Created by chenchao on 2017/1/20.
 * 需求方类型
 */
public enum DemandType {
    PERSONAL(1, "个人需求方"),
    ENTERPRISE(2, "企业需求方");

    DemandType(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int id;
    public String text;

    public static DemandType id2Enum(int id) {
        for (DemandType item : DemandType.values()) {
            if (item.id == id) {
                return item;
            }
        }

        return PERSONAL;
    }
}
