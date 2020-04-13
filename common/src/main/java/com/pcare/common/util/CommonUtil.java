package com.pcare.common.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author: gl
 * @CreateDate: 2019/12/1
 * @Description: 一些通用类
 */
public class CommonUtil {

    public static String getDateStr(Date date, String format) {
        if (null == date) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
    public static String getDateStr(String date, String format) {
        if (null == date) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        return getDateStr(getDate(date,format),format);
    }

    public static String getDateStr(Date date) {
        return getDateStr(date, null);
    }

    public static Date getDate(String date, String format) {
        if (null == date) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Date getDate(String date) {
        return getDate(date, null);
    }

    public static String getUUID(Context context) {
        if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            return Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//            return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } else return "aaaaaaaaaaaaaa";

    }

    public static String getRandomId(){
        return UUID.randomUUID().toString();
    }

    /**
     * 实体类转json
     * @param o
     * @return
     */
    public static String entityToJson(Object o,String[] keys){
        if(null != keys && keys.length>0){
            try {
                JSONObject object = new JSONObject(new GsonBuilder().disableHtmlEscaping().create().toJson(o));
                for (String k : keys){
                    if (object.has(k))
                        object.remove(k);
                }
                return object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }else
            return new GsonBuilder().disableHtmlEscaping().create().toJson(o);
    }
    public static String entityToJson(Object o){
        return entityToJson(o,null);
    }

    /**
     * json转实体类
     * @param s
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String s,Class<T> cls){
        return new Gson().fromJson(s,cls);

    }
}
