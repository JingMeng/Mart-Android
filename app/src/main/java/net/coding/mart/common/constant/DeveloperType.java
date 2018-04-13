package net.coding.mart.common.constant;

/**
 * Created by chenchao on 2017/1/20.
 * 开发者类型
 */
public enum DeveloperType {
    SOLO(1, "独立开发者"),
    TEAM(2, "开发者团队");

    DeveloperType(int id, String text) {
        this.id = id;
        this.alics = text;
    }

    public int id;
    public String alics;

    public static DeveloperType id2Enum(int id) {
        for (DeveloperType item : DeveloperType.values()) {
            if (item.id == id) {
                return item;
            }
        }

        return SOLO;
    }
}
