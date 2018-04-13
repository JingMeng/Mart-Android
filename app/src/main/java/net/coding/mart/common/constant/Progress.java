package net.coding.mart.common.constant;

import net.coding.mart.R;
import net.coding.mart.common.widget.main.DropItem;

/**
 * Created by chenchao on 16/3/19.
 * 发布需求状态
 */
public enum Progress implements DropItem {

    all(-1, "所有进度", 0, 0),
    waitCheck(0, "待审核", 0xFF666666, 0xFF666666),//--
    checking(1, "审核中", 0xFFF7C45D, 0xFFFFFFFF),//
    notPass(2, "未通过", 0xFFE94F61, 0xFFFFFFFF),//-
    cannel(3, "已取消", 0xFFDDDDDD, 0xFFFFFFFF),//-
    noStart(4, "未开始", 0xFFA9A9A9, 0xFF666666),//
    recruit(5, "招募中", 0xFF64C378, 0xFFFFFFFF, 0x3BBD79),//
    doing(6, "开发中", 0xFF2FAEEA, 0xFFFFFFFF, 0x2FAEEA),//
    finish(7, "已结束", 0xFFBACDD8, 0xFFFFFFFF, 0xBBCED7),
    waitPay(8, "待支付", 0xFFBACDDB, 0xFFFFFFFF),
    warranty(9, "质保中", 0xFF6F58E4, 0xFFFFFFFF);

    public int color = 0xFF666666;
    public int bgColor = 0xFF000000;
    public int color2 = 0xFF666666;
    public String text = "";
    public final int id;

    Progress(int id, String text, int color, int colorBg, int color2) {
        this.id = id;
        this.text = text;
        this.color = color;
        this.bgColor = colorBg;
        this.color2 = color2;
    }

    Progress(int id, String text, int color, int colorBg) {
        this(id, text, color, colorBg, 0xFF666666);
    }

    public static Progress id2Enum(int id) {
        for (Progress item : Progress.values()) {
            if (item.id == id) {
                return item;
            }
        }
        return waitCheck;
    }

    static final String[] allText;

    static {
        Progress[] values = Progress.values();
        allText = new String[values.length];
        for (int i = 0; i < values.length; ++i) {
            allText[i] = values[i].text;
        }
    }

    public static int alias2Id(String alias) {
        for (Progress item : Progress.values()) {
            if (item.text.equals(alias)) {
                return item.id;
            }
        }

        return all.id;
    }

    // 报名参与者能否编辑自己的参与信息
    public boolean canEditJoinApply() {
        switch (this) {
            case doing:
            case finish:
            case cannel:
                return false;
            default:
                return true;
        }
    }

    public static String id2Name(int id) {
        return id2Enum(id).text;
    }

    public static String[] getMainPickNames() {
        return new String[]{
                all.text,
                noStart.text,
                recruit.text,
                doing.text,
                finish.text
        };
    }

    public boolean showIMButton() {
        return this == recruit
                || this == doing
                || this == noStart
                || this == warranty
                || this == finish;
    }

    public boolean canJumpDetail() {
        return this == Progress.noStart
                || this == Progress.recruit
                || this == Progress.doing
                || this == Progress.finish
                || this == Progress.warranty;
    }

    public static int iconFromId(int progressId) {
        if (progressId == doing.id) {
            return R.mipmap.job_detail_progress_doing;
        } else if (progressId == finish.id) {
            return R.mipmap.job_detail_progress_finish;
        } else if (progressId == noStart.id) {
            return R.mipmap.job_detail_progress_no_start;
        } else if (progressId == recruit.id) {
            return R.mipmap.job_detail_progress_recruit;
        } else {
            return 0;
        }
    }

    public boolean joinedCanEnterRewardDetail() {
        return this == doing
                || this == finish
                || this == recruit
                || this == warranty;
    }

    @Override
    public String getAlics() {
        return text;
    }

    @Override
    public int getId() {
        return id;
    }
}
