package net.coding.mart.common.constant;

import net.coding.mart.R;

public enum VipType {

    NOT_VIP(R.mipmap.flag_team_normal),
    SILVER(R.mipmap.flag_team_sliver),
    GOLD(R.mipmap.flag_team_gold),
    DIAMOND(R.mipmap.flag_team_diamond);

    public int icon;

    VipType(int icon) {
        this.icon = icon;
    }
}
