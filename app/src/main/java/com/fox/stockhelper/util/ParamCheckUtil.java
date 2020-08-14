package com.fox.stockhelper.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 参数校验工具
 * @author lusongsong
 */
public class ParamCheckUtil {
    /**
     * 邮件验证
     */
    static final String EMAIL_PATTERN =
            "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    /**
     * 验证码
     */
    static final String VERIFY_CODE_PATTERN = "^[0-9]{6}$";

    /**
     * 是否为邮箱
     * @param param
     * @return
     */
    public static boolean isEmail(String param) {
        return patternCheck(param, EMAIL_PATTERN);
    }

    /**
     * 是否为验证码
     * @param param
     * @return
     */
    public static boolean isVerifyCode(String param) {
        return patternCheck(param, VERIFY_CODE_PATTERN);
    }


    /**
     * 正则表达式匹配
     * @param param
     * @param patternStr
     * @return
     */
    public static boolean patternCheck(String param, String patternStr) {
        if (null == param || null == patternStr || param.equals("") || patternStr.equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(param);
        return matcher.matches();
    }
}
