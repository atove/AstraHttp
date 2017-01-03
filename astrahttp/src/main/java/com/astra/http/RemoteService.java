package com.astra.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;


/**
 * Created by Astra on 16/12/1.
 */

public class RemoteService {
    public static boolean isPrintLog;
    private static RemoteService mInstance;
    private UrlConfigManager urlConfigManager;
    private static String mPath;

    public void init (Context context, String path){
        mPath = path;
        if (TextUtils.isEmpty(mPath)){
            Log.d("RemoteService", "RemoteService初始化错误：path 不能为空");
            return;
        }
        urlConfigManager = new UrlConfigManager(context, path);
    }

    public static RemoteService getInstance(){
        if (mInstance == null) {
            synchronized (UrlConfigManager.class) {
                if (mInstance == null) {
                    mInstance = new RemoteService();
                }
            }
        }
        return mInstance;
    }
    public void isPrintLog(Boolean isLog){
        isPrintLog = isLog;
    }
    public Request invoke(String key){
        if (urlConfigManager == null){
            Log.e("RemoteService", "RemoteService没有初始化");
            return null;
        }
        UrlData urlData = urlConfigManager.findURL(key);
        return new Request(urlData);
    }

    public void addHost(String key, String host){
        urlConfigManager.addHost(key, host);
    }
}
