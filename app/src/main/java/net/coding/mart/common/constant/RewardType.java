package net.coding.mart.common.constant;

import android.content.Context;
import android.graphics.drawable.Drawable;

import net.coding.mart.R;
import net.coding.mart.common.widget.main.DropItem;

/**
 * Created by chenchao on 16/3/19.
 * 项目类型
 */
public enum RewardType implements DropItem {

    all("所有类型", -1, R.mipmap.job_type_0, "ALL"),
    web("Web 网站", 0, R.mipmap.job_type_0, "WEB"),
    mobile("APP 开发", 5, R.mipmap.job_type_5, "APP"),
    wechat("微信公众号", 2, R.mipmap.job_type_2, "WECHAT"),
    html5("HTML5 应用", 3, R.mipmap.job_type_3, "HTML5"),
    consult("咨询", 6, R.mipmap.job_type_6, "ZHIXUN"),
    other("其它", 4, R.mipmap.job_type_4, "OTHER"),
    weapp("小程序", 7, R.mipmap.job_type_4, "WEAPP");

    public final String alias;
    private final int iconId;
    public final int id;
    public final String newType;

    RewardType(String alias, int id, int iconId, String newType) {
        this.alias = alias;
        this.id = id;
        this.iconId = iconId;
        this.newType = newType;
    }

    public static String[] allTypeName() {
        RewardType[] allTypes = RewardType.values();
        String[] names = new String[allTypes.length];
        for (int i = 0; i < names.length; ++i) {
            names[i] = allTypes[i].alias;
        }
        return names;
    }

    public static int name2Id(String name) {
        for (RewardType item : RewardType.values()) {
            if (item.alias.equals(name)) {
                return item.id;
            }
        }

        return all.id;
    }

    public static RewardType name2Enum(String name) {
        for (RewardType item : RewardType.values()) {
            if (item.alias.equals(name)) {
                return item;
            }
        }

        return web;
    }

    public static RewardType newType2Enum(String name) {
        for (RewardType item : RewardType.values()) {
            if (item.newType.equals(name)) {
                return item;
            }
        }

        return web;
    }


    public static RewardType idToEnum(int id) {
        for (RewardType item : RewardType.values()) {
            if (item.id == id) {
                return item;
            }
        }

        return weapp;
    }

    public static String idToName(int id) {
        for (RewardType item : RewardType.values()) {
            if (item.id == id) {
                return item.alias;
            }
        }

        return other.alias;
    }

    public static Drawable iconFromType(Context context, String title) {
        Integer resId = R.mipmap.job_type_4;
        for (RewardType item : RewardType.values()) {
            if (item.alias.equals(title)) {
                resId = item.iconId;
            }
        }

        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        return drawable;
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
