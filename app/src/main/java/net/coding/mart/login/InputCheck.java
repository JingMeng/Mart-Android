package net.coding.mart.login;

import android.content.Context;

import net.coding.mart.common.widget.SingleToast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenchao on 15/12/22.
 */
public class InputCheck {
    public static boolean isEmail(String s) {
        String regExp = "^.+@.+$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(s);
        return m.find();
    }

    public static boolean isPhone(String s) {
        String regExp = "^[0-9]{3,15}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(s);
        return m.find();
    }

    public static boolean checkEmail(Context context, String s) {
        boolean result = isEmail(s);
        if (!result) {
            SingleToast.showMiddleToast(context, "您输入的 Email 格式错误");
        }

        return result;
    }

    public static boolean checkPhone(Context context, String s) {
        boolean result = isPhone(s);
        if (!result) {
            SingleToast.showMiddleToast(context, "请输入有效的手机号");
        }
        return result;
    }

    public static boolean checkMPayPassword(Context context, String s) {
        String passwordRegEx = "^.{6,16}$";
        Pattern p = Pattern.compile(passwordRegEx);
        Matcher m = p.matcher(s);
        if (m.find()) {
            return true;
        } else {
            SingleToast.showMiddleToast(context, "密码格式为 6-16位字母、数字或字符");
            return false;
        }
    }
}
