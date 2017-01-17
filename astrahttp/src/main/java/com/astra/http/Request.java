package com.astra.http;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
    private ArrayList<RequestHeader> requestHeaders;
    private RequestDecorate requestDecorate;
    private String host;

    public void setRequestDecorate(RequestDecorate requestDecorate) {
        this.requestDecorate = requestDecorate;
    }

    protected Request(UrlData urlData) {
        this.urlData = urlData;
        if (RemoteService.getInstance().requestDecorateHashMap != null){
            this.requestDecorate = RemoteService.getInstance().requestDecorateHashMap.get(urlData.getDecorateType());
        }
        if (RemoteService.getInstance().hosts != null){
            this.host = RemoteService.getInstance().hosts.get(urlData.getHost());
        }
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

    private ArrayList<String> urlSuffixs;
    public Request addUrlSuffix(String name) {

        if (urlSuffixs == null){
            urlSuffixs = new ArrayList<>();
        }
        urlSuffixs.add(name);
        return this;
    }

    public Request addParam(String name, int value) {
        addParam(name, String.valueOf(value));
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
    public Request addHeader(String name, String value) {
        if (requestHeaders == null){
            requestHeaders = new ArrayList<>();
        }
        RequestHeader parameter = new RequestHeader(name, value);
        requestHeaders.add(parameter);
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
        if (requestDecorate != null){
            requestDecorate.setRequestParameters(requestParameters);
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = host + urlData.getUrl();
        if (urlSuffixs != null){
            for (String urlSuffix : urlSuffixs){
                if (!TextUtils.isEmpty(urlSuffix)){
                    url += "/" + urlSuffix;
                }
            }

        }

        okhttp3.Request.Builder builder = new okhttp3.Request.Builder()
                .url(url);

        
        switch (urlData.getNetType()){
            case "get" :
                getBody(builder);
                builder.get();
                break;
            case "post" :
                builder.post(getBody(builder));
                break;

            case "put" :
                builder.put(getBody(builder));
                break;
            case "del" :
                builder.delete(getBody(builder));
                break;
            default:
                Log.e("网络请求", "没有这个方法：" + urlData.getNetType());
                break;
        }
        final okhttp3.Request request = builder.build();

        if (RemoteService.isPrintLog){
        }

        if (RemoteService.isPrintLog){
            Log.d("请求地址" + urlData.getNetType(), request.url().toString());
            Headers headers = request.headers();
            for (int i = 0; i < headers.size(); i++){
                Log.d("请求头", headers.name(i) + "  : " + headers.value(i));
            }
        }

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
    private RequestBody getBody(okhttp3.Request.Builder builder){
        MultipartBody.Builder multipartBodybuilder = new MultipartBody.Builder();
        RequestBody requestBody;
        if (requestDecorate != null){

            HashMap<String, String> headers = requestDecorate.getRequestHeader();
            if (headers != null){
                for (String key : headers.keySet()) {
                    builder.addHeader(key, headers.get(key));
                }
            }

            MediaType mediaType;
            if (TextUtils.isEmpty(requestDecorate.getMediaType())){
                mediaType = MultipartBody.FORM;
            }else {
                mediaType = MediaType.parse(requestDecorate.getMediaType());
            }
            requestBody = RequestBody.create(mediaType, requestDecorate.getContent());
        }else{

            if (requestParameters != null){
                for (RequestParameter requestParameter : requestParameters){
                    multipartBodybuilder.addFormDataPart(requestParameter.getKey(), requestParameter.getValue());
                }
            }

            if (file != null){
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
                multipartBodybuilder.addPart( Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.jpg\""),fileBody);

            }else if (bytes != null){
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), bytes);
                multipartBodybuilder.addPart( Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.jpg\""),fileBody);
            }

            multipartBodybuilder.setType(MultipartBody.FORM);
            requestBody = multipartBodybuilder.build();
        }



        if(RemoteService.isPrintLog) {
            if (requestDecorate != null){
                Log.d("请求参数", requestDecorate.getContent());
            }else {
                Log.d("请求参数", paramSerialize(requestParameters));
            }
        }
        return requestBody;
    }
}
