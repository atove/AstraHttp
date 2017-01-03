package com.astra.http;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
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

    private File file;
    public Request setFile(File file){
        this.file = file;
        return this;
    }
    private byte[] bytes;
    public Request setData(byte[] data){
        bytes = data;
        return this;
    }

    public void start(){
        OkHttpClient okHttpClient = new OkHttpClient();
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder()
                .url(urlData.getUrl());
        if (RemoteService.isPrintLog){
            Log.d("请求地址" + urlData.getNetType(), urlData.getUrl());
        }
        switch (urlData.getNetType()){
            case "get" :

                builder.get();
                break;
            case "post" :

                MultipartBody.Builder multipartBodybuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                if (file != null){
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
                    multipartBodybuilder.addPart( Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.jpg\""),fileBody);
                }else if (bytes != null){
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), bytes);
                    multipartBodybuilder.addPart( Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.jpg\""),fileBody);
                }

                if (requestParameters != null){
                    for (RequestParameter requestParameter : requestParameters){
                        multipartBodybuilder.addFormDataPart(requestParameter.getKey(), requestParameter.getValue());
                    }
                }

                if(RemoteService.isPrintLog) {
                    Log.d("请求参数", paramSerialize(requestParameters));
                }
                RequestBody requestBody = multipartBodybuilder.build();
                builder.post(requestBody);
                break;
            default:
                Log.e("网络请求", "没有这个方法：" + urlData.getNetType());
                break;
        }
        final okhttp3.Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (requestCall != null){
                    requestCall.onFail(-1, e.getMessage());
                }
                if(RemoteService.isPrintLog) {
                    Log.d("请求失败", "onFailure() e=" + e);
                }

            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String resp = response.body().string();
                if (response.code() == 200){
                    if (requestCall != null){
                        requestCall.onSuccess(resp);
                    }
                    if(RemoteService.isPrintLog) {
                        Log.d("请求成功", " onResponse() reuslt=" + resp);
                    }
                }else {
                    if (requestCall != null){
                        requestCall.onFail(response.code(), resp);
                    }
                    if (RemoteService.isPrintLog){
                        Log.d("请求失败", " onResponse() code=" + response.code() + "   reuslt=" + resp);
                    }
                }



            }
        });
    }

    public static String paramSerialize(ArrayList<RequestParameter> parameters){
        if (parameters == null){
            return "";
        }
        StringBuffer p = new StringBuffer();
        for (RequestParameter requestParameter : parameters){
            p.append(requestParameter.getKey());
            p.append('=');
            p.append(requestParameter.getValue());
            p.append('&');
        }
        if (p.length() > 0){
            return p.substring(0, p.length() - 1);
        }else {
            return p.toString();
        }
    }
}
