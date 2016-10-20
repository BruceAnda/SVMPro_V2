package com.lovearthstudio.duaui.util;


import android.text.TextUtils;
import java.util.regex.Pattern;

public class PasswordChecker {
    public static final int OK=0;
    public static final String OK_STR="合格";
    public static final int ERR_EMPTY=1;
    public static final String ERR_EMPTY_STR="密码不能为空";
    public static final int ERR_INVALID_LEN =2;
    public static final String ERR_INVALID_LEN_STR="密码长度必须在%d和%d之间";
    public static final int ERR_INVALID_START =3;
    public static final String ERR_INVALID_START_STR="密码必须以字母开头";
    public static final int ERR_MISS_CHAR=4;
    public static final String ERR_MISS_CHAR_STR="密码至少包含%d个小写字母,%d个大写字母,%d个数字";
    public static final int ERR_ILLEGAL_CHAR=5;
    public static final String ERR_ILLEGAL_CHAR_STR="密码不能包含特殊字符";
    public static final int ERR_REPEAT=6;
    public static final String ERR_REPEAT_STR="相同组合不能连续出现";

    private static final Pattern regex = Pattern.compile("([a-zA-Z0-9]+)\\1");

    public static int check(String rawPwd,int minLen,int maxLen,int minLow,int minUpper,int minNumeric,boolean allowOther) {
        if (rawPwd == null || rawPwd.isEmpty()|| TextUtils.isEmpty(rawPwd)) {
            return ERR_EMPTY;
        }
        if (rawPwd.length() < minLen || rawPwd.length() > maxLen) {
            return ERR_INVALID_LEN;
        }
        int lowerCount = 0,upperCount=0, numericCount = 0;
        for (int i = 0; i < rawPwd.length(); i++) {
            char aChar = rawPwd.charAt(i);
            if (aChar >= 'a' && aChar <= 'z') {          //lower case
                lowerCount++;
            }else if (aChar >= 'A' && aChar <= 'Z') {   //upper case
                upperCount++;
            }else {
                if(i==0) return ERR_INVALID_START;      //not startWith alpha
                if (aChar >= '0' && aChar <= '9') {      //numeric
                    numericCount++;
                }else {
                    if (!allowOther) return ERR_ILLEGAL_CHAR;
                }
            }
        }

        if (lowerCount < minLow||upperCount<minUpper || numericCount < minNumeric) {
            return ERR_MISS_CHAR;
        }
        if (regex.matcher(rawPwd).find()) {
            return ERR_REPEAT;
        }
        return 0;
    }

    public static String checkPwd(String rawPwd,int minLen,int maxLen,int minLow,int minUpper,int minNumeric,boolean allowOther) {
        if (rawPwd == null || rawPwd.isEmpty()|| TextUtils.isEmpty(rawPwd)) {
            return ERR_EMPTY_STR;
        }
        if (rawPwd.length() < minLen || rawPwd.length() > maxLen) {
            return String.format(ERR_INVALID_LEN_STR,minLen,maxLen);
        }
        int lowerCount = 0,upperCount=0, numericCount = 0;
        for (int i = 0; i < rawPwd.length(); i++) {
            char aChar = rawPwd.charAt(i);
            if (aChar >= 'a' && aChar <= 'z') {          //lower case
                lowerCount++;
            }else if (aChar >= 'A' && aChar <= 'Z') {   //upper case
                upperCount++;
            }else {
                if(i==0) return ERR_INVALID_START_STR;      //not startWith alpha
                if (aChar >= '0' && aChar <= '9') {      //numeric
                    numericCount++;
                }else {
                    if (!allowOther) return ERR_ILLEGAL_CHAR_STR;
                }
            }
        }

        if (lowerCount < minLow||upperCount<minUpper || numericCount < minNumeric) {
            return String.format(ERR_MISS_CHAR_STR, minLow,minUpper,minNumeric);
        }
        if (regex.matcher(rawPwd).find()) {
            return ERR_REPEAT_STR;
        }
        return OK_STR;
    }
}