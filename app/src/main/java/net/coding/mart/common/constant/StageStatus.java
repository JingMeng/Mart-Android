package net.coding.mart.common.constant;

import net.coding.mart.common.widget.main.DropItem;

/**
 * Created by chenchao on 16/3/19.
 * 项目阶段状态
 */
public enum StageStatus implements DropItem {

    notStart(1, "未启动", 0xFFF7C45D, 0xFFFFFFFF),
    waitSubmit(2, "开发中", 0xFF4289DB, 0xFFFFFFFF),
    waitCheck(3, "待验收", 0xFFF7C45D, 0xFFFFFFFF),
    checkSuccess(4, "已验收", 0xFF4289DB, 0xFFFFFFFF),
    checkFail(5, "验收未通过", 0xFFE94F61, 0xFFFFFFFF),
    pause(6, "已暂停", 0xFFE94F61, 0xFFFFFFFF), // 没有了
    end(7, "已中止", 0xFFE94F61, 0xFFFFFFFF),
    warranty(9, "质保中", 0xFF6F58E4, 0xFFFFFFFF);

//    waitSubmit(0, "待交付", 0xFF666666, 0xFF666666),//--
//    waitCheck(1, "待验收", 0xFFF7C45D, 0xFFFFFFFF),//
//    checkFail(2, "验收未通过", 0xFFE94F61, 0xFFFFFFFF),//-
//    checkSuccess(3, "已验收", 0xFF4289DB, 0xFFFFFFFF),//-
//    payed(4, "已付款", 0xFFA9A9A9, 0xFF666666);

    public int color = 0xFF666666;
    public int bgColor = 0xFF000000;
    public int color2 = 0xFF666666;
    public String text = "";
    public final int id;

    StageStatus(int id, String text, int color, int colorBg, int color2) {
        this.id = id;
        this.text = text;
        this.color = color;
        this.bgColor = colorBg;
        this.color2 = color2;
    }

    StageStatus(int id, String text, int color, int colorBg) {
        this(id, text, color, colorBg, 0xFF666666);
    }

    public static StageStatus id2Enum(int id) {
        for (StageStatus item : StageStatus.values()) {
            if (item.id == id) {
                return item;
            }
        }
        return notStart;
    }

    static final String[] allText;
    static {
        StageStatus[] values = StageStatus.values();
        allText = new String[values.length];
        for (int i = 0; i < values.length; ++i) {
           allText[i] = values[i].text;
        }
    }

    public static int alias2Id(String alias) {
        for (StageStatus item : StageStatus.values()) {
            if (item.text.equals(alias)) {
                return item.id;
            }
        }

        return notStart.id;
    }

    public static String id2Name(int id) {
        return id2Enum(id).text;
    }

    @Override
    public String getAlics() {
        return text;
    }

    @Override
    public int getId() {
        return id;
    }

    public boolean isFinish() {
        return this == checkSuccess || this == end;
    }

    public boolean hightLight() {
        return this == waitSubmit
                || this == waitCheck
                || this == checkSuccess
                || this == checkFail;
    }
}
