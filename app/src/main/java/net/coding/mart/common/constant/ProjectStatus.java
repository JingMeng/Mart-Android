package net.coding.mart.common.constant;

/**
 * Created by chenchao on 2017/9/28.
 */

public enum ProjectStatus {

    UNKONWN_STATUS(0, "未知"),
    NO_PAYMENT(1, "待支付"),
    RECRUITING(2, "招募中"),
    DEVELOPING(3, "开发中"),
    FINISHED(4, "已结束"),
    CANCELED(5, "已取消"),
    CHECKING(6, "审核中"),
    REJECTED(7, "审核未通过");

    public int id;
    public String alias;

    ProjectStatus(int id, String alias) {
        this.id = id;
        this.alias = alias;
    }


}
