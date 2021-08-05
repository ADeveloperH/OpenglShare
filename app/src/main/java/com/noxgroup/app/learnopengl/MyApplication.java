package com.noxgroup.app.learnopengl;

import android.app.Application;
import android.content.Context;

/**
 * @author huangjian
 * @create 2021/7/30
 * @Description
 */
public class MyApplication extends Application {
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
    }
}
