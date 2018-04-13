package net.coding.mart.common;

import java.io.Serializable;

/**
 * Created by chenchao on 15/11/30.
 */
public class CommonExtra {


    public enum JumpParam implements Serializable {
        Default,
        FinishToList, // 成功后跳转到到 list 界面
    }

    public static final String FinishToJump = "FinishToJump"; // 完成后要跳转

}
