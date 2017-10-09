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
    static int timeout = 10;//超时时间，默认10秒
    private static RemoteService mInstance;
    HashMap<String, String> hosts;
    private UrlConfigManager urlConfigManager;
    private static String mPath;
    HashMap<String, RequestDecorate> requestDecorateHashMap;

    public void init (Context context, String path){
        mPath = path;
        if (TextUtils.isEmpty(mPath)){
            Log.d("RemoteService", "RemoteService初始化错误：path 不能为空");
            return;
        }
        urlConfigManager = new UrlConfigManager(context, path);
    }

    private static class Instance{
        final static RemoteService INSTANCE = new RemoteService();
    }
    public static RemoteService getInstance(){
        if (mInstance == null) {
            synchronized (UrlConfigManager.class) {
                if (mInstance == null) {
                    mInstance = new RemoteService();
                }
            }
        }
        return Instance.INSTANCE;
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
        if (hosts == null){
            hosts = new HashMap<>();
        }
        hosts.put(key, host);
    }

    public void addrequestDecorate(String key, RequestDecorate requestDecorate){
        if (requestDecorateHashMap == null){
            requestDecorateHashMap = new HashMap<>();
        }

        requestDecorateHashMap.put(key, requestDecorate);
    }

    /**
     * 设置超时时间 单位：秒
     */
    public void setTimeout(int timeout){
        RemoteService.timeout = timeout;
    }

    /**
     * 根据 tag 取消请求
     * @param tag
     */
    public void cancelAsTag(Object tag){
        OkHttpUtils.cancelAsTag(tag);
    }
}
