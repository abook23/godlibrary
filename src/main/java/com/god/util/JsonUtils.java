package com.god.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author abook23 2015-9-6 14:47:27
 * @version 2.0
 */
public class JsonUtils {
    private static Gson gson;

    public static String toJson(Object object) {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
            gson = builder.create();
        }
        String json = gson.toJson(object);
        return json;
    }


    public static <T> T jsonToBean(String json, Class<T> class1) {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        Gson gson = builder.create();
        T t = gson.fromJson(json, class1);
        return t;
    }

    public static <T> T jsonToBean(Object object, Class<T> class1) {
        String json = toJson(object);
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        Gson gson = builder.create();
        T t = gson.fromJson(json, class1);
        return t;
    }

    public static <T> List<T> jsonToBean(List<?> list, Class<T> class1) {
        return fromJsonArray(list, class1);
    }

    public static <T> List<T> fromJsonArray(String json, Class<T> clazz) {
        List<T> lst = new ArrayList<>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (JsonElement elem : array) {
            lst.add(new Gson().fromJson(elem, clazz));
        }
        return lst;
    }

//    public static <T> List<T> fromJsonArray(List<?> list, Class<T> class1) {
//        String json = toJson(list);
//        return gson.fromJson(json, new TypeToken<ArrayList<T>>() {
//        }.getType());
//    }

    public static <T> List<T> fromJsonArray(List<?> list, Class<T> clazz){
        List<T> lst = new ArrayList<>();
        JsonArray array = new JsonParser().parse(toJson(list)).getAsJsonArray();
        for (final JsonElement elem : array) {
            lst.add(new Gson().fromJson(elem, clazz));
        }
        return lst;
    }


    public static List<Object> jsonToList(JSONArray jsonArray) throws JSONException {
        List<Object> list = new ArrayList<>();
        JSONArray array = jsonArray;
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONObject) {
                HashMap<String, Object> hs = jsonToHasMap((JSONObject) value);
                list.add(hs);
            } else {
                list.add(jsonToList((JSONArray) value));
            }
        }
        return list;
    }

    public static HashMap<String, Object> jsonToHasMap(JSONObject jsonObject) throws JSONException {
        HashMap<String, Object> hs = new HashMap<String, Object>();
        JSONObject object = jsonObject;
        Iterator<?> iterator = object.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            Object value = object.get(key);

            if (value instanceof JSONObject) {
                JSONObject jsonObject2 = new JSONObject(value.toString());
                hs.put(key, jsonToHasMap(jsonObject2));
            } else {
                if (value instanceof JSONArray) {
                    JSONArray array = new JSONArray(value.toString());
                    List<HashMap<?, ?>> list = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        list.add(jsonToHasMap(array.getJSONObject(i)));
                    }
                    hs.put(key, list);
                } else {
                    hs.put(key, value);
                }
            }
        }
        return hs;
    }
}
