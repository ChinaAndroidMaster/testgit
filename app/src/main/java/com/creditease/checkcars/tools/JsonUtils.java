package com.creditease.checkcars.tools;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class JsonUtils
{

    static Gson gson = new Gson();

    public static < T > T parserJsonStr2Obj(JSONObject json, Class< T > obj)
    {
        String strjson = json.toString();
        T responseBean = gson.fromJson(strjson, obj);
        return responseBean;
    }

    /**
     * 将json对象封装为T对象
     *
     * @param <T>
     * @param json JSONObject
     * @param bean Class<T>
     * @return
     */
    public static < T > T parserJsonStringToObject(JSONObject json, Class< T > bean)
    {
        if ( json == null )
        {
            return null;
        }

        String jsonObject = json.toString();
        T responseBean = gson.fromJson(jsonObject, bean);
        return responseBean;
    }

    /**
     * 将json结构字符串封装为T对象
     *
     * @param json JSONObject
     * @param bean Class<T>
     * @return
     */
    public static < T > T parserJsonStringToObject(String jsonObject, Class< T > bean)
            throws JsonSyntaxException
    {
        if ( (jsonObject == null) || "".equals(jsonObject) )
        {
            return null;
        }
        try
        {
            return gson.fromJson(jsonObject, bean);
        } catch ( JsonSyntaxException e )
        {
            throw e;
        } catch ( IllegalStateException e )
        {
            throw new JsonSyntaxException(e);
        } catch ( Exception e )
        {
            throw new JsonSyntaxException(e);
        }
    }

    /**
     * 将json字符串转化成list集合
     *
     * @param json
     * @param type
     * @return 获取Type的方式 Type type = new TypeToken<List<QAnswer>>(){}.getType();
     */
    public static < T > List< T > parsonJson2Obj(String json, Type type)
    {
        List< T > list = null;
        list = gson.fromJson(json, type);
        return list;
    }

    // 将bean转换给json数据
    public static String requestObjectBean(Object bean)
    {
        if ( bean == null )
        {
            return null;
        }
        Gson gson = new Gson();
        String json = gson.toJson(bean);
        return json;
    }
}
