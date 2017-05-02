package com.god.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yzz on 2015/12/11.
 */
public class Emoji {


    public static String deletEmoji(String str) {
        if (str == null)
            return null;
        String regex = "[!-~\u4E00-\u9FA5\\s]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(str);

        StringBuffer sb = new StringBuffer();
        while (match.find()) {
            sb.append(match.group());
        }

        return sb.toString();
    }
}
