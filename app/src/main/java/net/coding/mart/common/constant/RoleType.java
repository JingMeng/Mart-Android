package net.coding.mart.common.constant;

import net.coding.mart.common.widget.main.DropItem;

/**
 * Created by chenchao on 16/3/19.
 */
public enum RoleType implements DropItem {

    All("所有角色", -1),
    AllStack("全栈开发", 3),
    FrontEnd("前端开发", 5),
    Backend("后端开发", 9),
    App("应用开发", 1),
    Ios("iOS开发", 4),
    Android("Android开发", 6),
    ProductManager("产品经理", 10),
    Desinger("设计师", 2),
    Team("开发团队", 11);

    public final String alias;
    public final int id;

    RoleType(String alias, int id) {
        this.alias = alias;
        this.id = id;
    }

    public static String[] allTypeName() {
        RoleType[] allTypes = RoleType.values();
        String[] names = new String[allTypes.length];
        for (int i = 0; i < names.length; ++i) {
            names[i] = allTypes[i].alias;
        }
        return names;
    }

    public static int name2Id(String name) {
        for (RoleType item : RoleType.values()) {
            if (item.alias.equals(name)) {
                return item.id;
            }
        }
        return All.id;
    }

    public static String idToName(int id) {
        for (RoleType item : RoleType.values()) {
            if (item.id == id) {
                return item.alias;
            }
        }
        return All.alias;
    }


    @Override
    public String getAlics() {
        return alias;
    }

    @Override
    public int getId() {
        return id;
    }
}
