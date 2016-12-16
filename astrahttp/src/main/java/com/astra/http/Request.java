package com.astra.http;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * 请求
 * Created by Astra on 16/12/1.
 */
public class Request {
    private String tag;
    private RequestCallback requestCall;
    private UrlData urlData;
    private ArrayList<RequestParameter> requestParameters;


    protected Request(UrlData urlData) {
        this.urlData = urlData;
    }

    public String getTag() {
        return tag;
    }

    public Request setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public Request setRequestCall(RequestCallback requestCall) {
        this.requestCall = requestCall;
        return this;
    }

    public Request addParam(String name, String value) {
        if (requestParameters == null){
            requestParameters = new ArrayList<>();
        }
        RequestParameter parameter = new RequestParameter(name, value);
        requestParameters.add(parameter);
        return this;
    }

    public void start(){
        OkHttpClient okHttpClient = new OkHttpClient();
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder()
                .url(urlData.getUrl());
        switch (urlData.getNetType()){
            case "get" :
                builder.get();
                break;
            case "post" :

                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                if (requestParameters != null){
                    for (RequestParameter requestParameter : requestParameters){
                        formBodyBuilder.add(requestParameter.getKey(), requestParameter.getValue());
                    }
                }
                builder.post(formBodyBuilder.build());
                break;
            default:
                Log.e("网络请求", "没有这个方法：" + urlData.getNetType());
                break;
        }
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(urlData.getUrl())
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (requestCall != null){
                    requestCall.onFail(e.getMessage());
                }
                Log.d("请求失败", "onFailure() e=" + e);
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String resp = response.body().string();
                if (requestCall != null){
                    requestCall.onSuccess(resp);
                }
                Log.d("请求成功", " onResponse() reuslt=" + resp);
            }
        });
    }
}
