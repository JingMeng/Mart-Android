package net.coding.mart.common.constant;

public enum ApplyStatus {

    UNKNOWN_STATUS(0, "未知", 0),
    CHECKING(1, "审核中", 0xFFEBBA59),
    PASSED(2, "已通过", 0),
    REJECTED(3, "已拒绝", 0xFFDB5858),
    CANCELED(4, "已取消", 0xFF8796A8);

    public int id;
    public String alias;
    public int color;

    ApplyStatus(int id, String alias, int color) {
        this.id = id;
        this.alias = alias;
        this.color = color;
    }
}
