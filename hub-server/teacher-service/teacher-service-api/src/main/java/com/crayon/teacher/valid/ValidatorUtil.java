package com.crayon.teacher.valid;

/**
 * @author Mengdl
 * @date 2025/04/28
 */
public class ValidatorUtil {

    /**
     * 定义手机号的正则表达式
     */
    private static final String MOBILE_PATTERN = "^1[3-9]\\d{9}$";

    public static boolean isMobile(String mobile) {
        if (mobile == null) {
            return false;
        }
        return mobile.matches(MOBILE_PATTERN);
    }

}
