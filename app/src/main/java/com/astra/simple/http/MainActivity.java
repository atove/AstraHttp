package com.astra.simple.http;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.astra.http.RemoteService;
import com.astra.http.RequestCallback;

import org.json.JSONArray;

import java.io.File;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //注册MobileAPI 文件
        RemoteService.getInstance().init(this, "url.xml");
        //注册Host，对应url.xml 中Host
        RemoteService.getInstance().addHost("MainHost", "http://hulu.leanapp.cn");
        //注册Request，对Request进行统一处理，对应url.xml 中DecorateType
        RemoteService.getInstance().addrequestDecorate("from", new FromRequest());
        RemoteService.getInstance().addrequestDecorate("json", new JsonRequest());
        //是否打印Log
        RemoteService.getInstance().isPrintLog(true);


        getTime();
    }
    public String getUUID() {

        final TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        //Log.e("error", "uuid=" + uniqueId);
        return uniqueId;
    }

    public String getTime(){

        long time=System.currentTimeMillis();//获取系统时间的10位的时间戳

        String  str=String.valueOf(time);
        Log.d("时间", str);

        return str;

    }

    public void onClick(View view) {
        JSONArray array = new JSONArray();
        array.put("59016a1e1b69e60058bbc823");
        array.put("59c3659d570c35004413f590");
        array.put("5923dca6a0bb9f005f70d6e1");
        switch (view.getId()){


            case R.id.btn_login:
                RemoteService.getInstance().invoke("login")
                        //添加 url 后缀，会拼接在 url 后边，用 / 分隔，非必须，可添加多个
                        .addUrlSuffix("xxxxx")
                        //添加参数，可添加多个，value 可以是 String、int
                        .addParam("loginname", "xxx")
                        .addParam("password", "xxx")
                        .addParam("tags", array)
                        //上传文件，只能单个文件上传
                        .setFile(new File("PATH"))
                        //请求回调
                        .setRequestCall(new RequestCallback() {
                            @Override
                            public void onSuccess(String content) {
                                Log.d("1234", content);
                            }

                            @Override
                            public void onFail(int code, String errorMessage) {

                            }
                        })
                        .start();

                /*RemoteService.getInstance()
                        .invoke("file")
                        .setFile(new File("file:///storage/emulated/0/Pictures/Screenshots/Screenshot_20160923-165949.png"))
                        .setRequestCall(new RequestCallback() {
                            @Override
                            public void onSuccess(String content) {
                                Log.d("1234", content);
                            }

                            @Override
                            public void onFail(int code, String errorMessage) {

                            }
                        })
                        .start();*/

                break;
        }
    }
}
