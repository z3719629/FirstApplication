package com.crm.onenetcontroller.onenet.utils;

import com.crm.onenetcontroller.onenet.DeviceItem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.ParameterizedType;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/1/30.
 */
public class GsonConvertUtil {
    public static <T> T convert(String jsonStr, Class clazz) {
        T t = null;
        if(!"".equals(jsonStr)) {
            JsonObject resp = new JsonParser().parse(jsonStr).getAsJsonObject();
            int errno = resp.get("errno").getAsInt();

            if (0 == errno) {
                Gson gson = new Gson();
                t = (T) gson.fromJson(resp.get("data"), clazz);
            }
        }
        return t;
    }
}
