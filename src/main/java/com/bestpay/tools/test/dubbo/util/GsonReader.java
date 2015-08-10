package com.bestpay.tools.test.dubbo.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiaju on 2015/8/1.
 */
public class GsonReader {
    static public Object readGsonToObject(String stream, Class clazz)
    {
        Gson gson = new Gson();
        return gson.fromJson(stream, clazz);
    }

    static public List<Object> readGsonToArray(String stream, Class clazz) {
        List<Object> resultList = new ArrayList();

        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(stream);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        Iterator it = jsonArray.iterator();
        while (it.hasNext()) {
            jsonElement = (JsonElement) it.next();
            stream = jsonElement.toString();
            Object result = gson.fromJson(stream, clazz);
            resultList.add(result);
        }
        return resultList;
    }

    static public String readMapToJson(Map<String, Object> map)
    {
        Gson gson = new Gson();
        return gson.toJson(map);
    }
}
