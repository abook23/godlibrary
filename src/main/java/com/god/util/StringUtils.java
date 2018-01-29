package com.god.util;

/**
 * Created by abook23 on 2016/1/28.
 */
public class StringUtils {

    //   找到大写位置
//    String str = "idName";
//    Matcher m = Pattern.compile("[A-Z]").matcher(str);
//    List<Integer> lens = new ArrayList<>();
//    while (m.find()) {
//        lens.add(m.start());
//    }


    /**
     * 将驼峰式命名的字符串转换为下划线大写方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串.
     * 例如：HelloWorld HELLO_WORLD
     * HElloWorld HELLO_WORLD
     *
     * @param name 转换前的驼峰式命名的字符串
     * @return 转换后下划线大写方式命名的字符串
     */
    public static String underscoreName(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            // 将第一个字符处理成大写
            result.append(name.substring(0, 1).toUpperCase());
            // 循环处理其余字符
            for (int i = 1; i < name.length(); i++) {
                char c = name.charAt(i);
                // 在大写字母前添加下划线
                if (Character.isUpperCase(c) && !Character.isDigit(c)) {
                    result.append("_");
                    if (Character.isUpperCase(name.charAt(i - 1)))
                        result.deleteCharAt(result.length() - 1);
                }
                // 其他字符直接转成大写
                result.append(c);
            }
        }
        return result.toString().toUpperCase();
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。
     * 例如：HELLO_WORLD HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String camelName(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母小写
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 手机号码
     */
    @Deprecated
    public static boolean isPhone(String value) {
        return isMobileNO(value);
    }

    public static boolean isMobileNO(String mobiles) {
        /**
         * /*
         移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         联通：130、131、132、152、155、156、185、186
         电信：133、153、180、189、（1349卫通）
         总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][1-9]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return !isEmpty(mobiles) && mobiles.matches(telRegex);
    }

    public static boolean isIdNumberNo(String id) {
        String telRegex = "\\d{18}|\\d{17}[x|X]";
        return !isEmpty(id) && id.matches(telRegex);
    }

    public static boolean isEmail(String value) {
        return value.contains("@") && value.contains(".com");
    }

    public static String hidePhoneNumber(String number) {
        if (number == null)
            return "";
        if (!isMobileNO(number))
            return "";
        String a = number.substring(0, 3);
        String b = number.substring(number.length() - 4, number.length());
        return a + "****" + b;
    }

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0 || "null".equals(str))
            return true;
        else
            return false;
    }
}
