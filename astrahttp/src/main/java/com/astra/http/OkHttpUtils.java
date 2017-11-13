package com.astra.http;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Astra on 17/10/9.
 */

public class OkHttpUtils {
    private static class OkHttpClientInstance{
        static OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(RemoteService.timeout, TimeUnit.SECONDS)
                .readTimeout(RemoteService.timeout,TimeUnit.SECONDS)
                .writeTimeout(RemoteService.timeout,TimeUnit.SECONDS)
                .build();
    }
    public static OkHttpClient getOkHttpClient (){
        return OkHttpClientInstance.okHttpClient;
    }

    private static Map<Object, List<Request>> requestsMap = new ConcurrentHashMap<>();

    public static void addRequest(Request request){
        if (request.getTag() == null){
            return;
        }
        List<Request> requests = requestsMap.get(request.getTag());
        if (null == requests){
            requests = new LinkedList<>();
            requestsMap.put(request.getTag(), requests);
        }
        requests.add(request);
    }

    public static void removeRequest(Request request){
        if (request.getTag() == null){
            return;
        }
        List<Request> requests = requestsMap.get(request.getTag());
        if (null != requests){
            requests.remove(request);
        }
    }

    static void cancelAsTag(Object tag){
        if (tag == null){
            return;
        }
        List<Request> requests = requestsMap.get(tag);
        if (null != requests){
            for (Request r : requests){
                if (r != null){
                    r.cancel();
                }
            }
            requestsMap.remove(tag);
        }
    }
}
