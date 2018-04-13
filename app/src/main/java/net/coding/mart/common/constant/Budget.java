package net.coding.mart.common.constant;

import net.coding.mart.common.widget.main.DropItem;

/**
 * 项目金额类型
 */
public enum Budget implements DropItem {

    budget1("2万以下", 0),
    budget3("2-5万", 1),
    budget5("5-10万", 2),
    budgetMax("10万以上", 3);

    public final String alias;
    public final int id;

    Budget(String alias, int id) {
        this.alias = alias;
        this.id = id;
    }

    public static String[] allTypeName() {
        Budget[] allTypes = Budget.values();
        String[] names = new String[allTypes.length];
        for (int i = 0; i < names.length; ++i) {
            names[i] = allTypes[i].alias;
        }
        return names;
    }

    public static int name2Id(String name) {
        for (Budget item : Budget.values()) {
            if (item.alias.equals(name)) {
                return item.id;
            }
        }

        return budgetMax.id;
    }

    public static String idToName(int id) {
        for (Budget item : Budget.values()) {
            if (item.id == id) {
                return item.alias;
            }
        }

        return budgetMax.alias;
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
