package com.example.wanandroid.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {
    private static SpUtils instance;
    private SharedPreferences sharedPreferences;
    private SpUtils(){

    }

    public static SpUtils getInstance(){
        if (instance == null){
            synchronized (SpUtils.class){
                if (instance == null){
                    instance = new SpUtils();
                }
            }
        }
        return instance;
    }

    public void init(Context context){
        if(sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences("zy_wan_android",Context.MODE_PRIVATE);
    }

    public void setString(String key,String word){
        sharedPreferences.edit().putString(key,word).commit();
    }

    public void setInt(String key,int word){
        sharedPreferences.edit().putInt(key,word).commit();
    }

    public void setFloat(String key,float word){
        sharedPreferences.edit().putFloat(key,word).commit();
    }

    public int getInt(String key,int defaultWord){
        return sharedPreferences.getInt(key,defaultWord);
    }

    public float getFloat(String key,float defaultWord){
        return sharedPreferences.getFloat(key,defaultWord);
    }

}
