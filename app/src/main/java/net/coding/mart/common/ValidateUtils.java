package net.coding.mart.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pansh on 2015/11/15 0015.
 */
public class ValidateUtils {


    /**
     * 验证身份证
     * @param icard
     * @return
     */
    public static boolean checkIdentity(String icard){

        //定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
        Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        //通过Pattern获得Matcher
        Matcher idNumMatcher = idNumPattern.matcher(icard);

        return idNumMatcher.matches();
    }
}
