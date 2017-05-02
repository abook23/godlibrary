package com.god.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by abook23 on 2016/9/6.
 * E-mail abook23@163.com
 */
public class BeanUtils {

    public static Map<String, Object> toMap(Object o){
        Map<String, Object> map = new HashMap<>();
        try {
            Field[] fields = o.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String proName = field.getName();
                Object proValue = field.get(o);
                if (proValue != null)
                    map.put(proName, proValue);
                //map.put(proName.toUpperCase(), proValue);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }


    public static Object map2PO(Map<String, Object> map, Object o) throws Exception {
        if (!map.isEmpty()) {
            for (String k : map.keySet()) {
                Object v = "";
                if (!k.isEmpty()) {
                    v = map.get(k);
                }
                Field[] fields = o.getClass().getDeclaredFields();
                // String clzName = o.getClass().getSimpleName();
                for (Field field : fields) {
                    int mod = field.getModifiers();
                    if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                        continue;
                    }
                    if (field.getName().toUpperCase().equals(k)) {
                        field.setAccessible(true);
                        field.set(o, v);
                    }

                }
            }
        }
        return o;
    }
}
