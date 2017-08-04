package com.gersion.library.utils;

import android.text.TextUtils;

/**
 * Created by Gersy on 2016/12/28.
 */
public class MatchUtils {
    /**
     * ~~ 创建时间：2017/5/25 14:46 ~~
     * 模糊匹配
     */
    public static boolean isMatch(String content, String condition) {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(condition)) {
            return false;
        }
        int minIndex = -1;
        char[] chars = condition.toCharArray();
        for (char c : chars) {
            int currentIndex = content.indexOf(c,minIndex+1);
            if (currentIndex>minIndex){
                minIndex = currentIndex;
            }else{
                return false;
            }
        }
        return true;
    }

    /**
     * ~~ 创建时间：2017/5/25 14:44 ~~
     * 以字符串为单位的匹配
     */
    public static int getIndex(String content, String condition) {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(condition)) {
            return -1;
        }
        return content.indexOf(condition);
    }

    /**
     * ~~ 创建时间：2017/5/25 14:45 ~~
     * 模糊匹配每个字符，得到每个字符第一次出现的数组
     */
    public static int[] getIndexArray(String content, String condition) {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(condition)) {
            return null;
        }
        int minIndex = -1;
        int[] array = new int[condition.length()];
        char[] chars = condition.toCharArray();
        int i = 0;
        for (char c : chars) {
            int currentIndex = content.indexOf(c,minIndex+1);
            if (currentIndex>minIndex){
                minIndex = currentIndex;
                array[i++] = currentIndex;
            }else{
                return null;
            }
        }
        return array;
    }
//    public static boolean isMatch(String content, String condition) {
//        try {
//            if (TextUtils.isEmpty(content) || TextUtils.isEmpty(condition)) {
//                return false;
//            }
//            StringBuilder regex = new StringBuilder();
//            condition = condition.replaceAll(pattern, "");
//            for (int i = 0; i < condition.length(); i++) {
//                char c = condition.charAt(i);
//                if (c == '.') {
//                    regex.append(c + "*");
//                } else {
//                    regex.append(".*" + c);
//                }
//                if (i == condition.length() - 1) {
//                    regex.append(".*");
//                }
//            }
//            if (content.matches(regex.toString())) {
//                return true;
//            }
//            return false;
//        } catch (Exception e) {
//            LoggerUtils.d(e.getMessage());
//            return false;
//        }
//    }
}
