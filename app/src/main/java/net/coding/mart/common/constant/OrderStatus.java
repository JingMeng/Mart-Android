package net.coding.mart.common.constant;

public enum OrderStatus {

    Pending("正在处理"),
    Success("已完成"),
    Fail("处理失败"),
    Cancel("已取消");

    public final String alics;

    OrderStatus(String alics) {
        this.alics = alics;
    }

    public static String getAlics(String type) {
        for (OrderStatus item : OrderStatus.values()) {
            if (item.name().equals(type)) {
                return item.alics;
            }
        }

        return "";
    }

    public static OrderStatus name2enum(String name) {
        for (OrderStatus item : OrderStatus.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }

        return Pending;
    }
}
