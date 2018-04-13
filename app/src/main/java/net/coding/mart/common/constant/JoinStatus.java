package net.coding.mart.common.constant;

import java.io.Serializable;

/**
 * Created by chenchao on 16/5/30.
 * 参与项目状态
 */
public enum JoinStatus implements Serializable {
    noJoin(-1, "未加入", 0xFF666666, 0xFFEEEEEE),
    joinStart(0, "待审核", 0xFFFFFFFF, 0xFF666666),
    joinStartCheck(1, "审核中", 0xFFFFFFFF, 0xFFF7C45D),
    joinsStartSucess(2, "已通过", 0xFFFFFFFF, 0xFF64C378),
    josinStartFail(3, "已拒绝", 0xFFFFFFFF, 0xFFE94F61),
    joinsStartCancel(4, "已取消", 0xFFFFFFFF, 0xFFDDDDDD);

    JoinStatus(int id, String text, int color, int colorBg) {
        this.id = id;
        this.text = text;
        this.color = color;
        this.bgColor = colorBg;
    }

    public static JoinStatus id2Enum(int id) {
        for (JoinStatus item : JoinStatus.values()) {
            if (id == item.id) {
                return item;
            }
        }

        return noJoin;
    }

    public int color = 0xFF666666;
    public int bgColor = 0xFF000000;
    public String text = "";
    public final int id;

    public boolean needReApply() {
        return this != joinStart && this != joinStartCheck;
    }

    public boolean isSignUp() {
        return this == joinStart || this == joinStartCheck;
    }

    public boolean isPass() {
        return this.equals(joinsStartSucess);
    }

    public boolean isReject() {
        return this == josinStartFail;
    }

    public boolean isCancel() {
        return this == joinsStartCancel;
    }
}
