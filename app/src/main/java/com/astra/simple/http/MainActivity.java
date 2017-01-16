package com.astra.simple.http;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.astra.http.RemoteService;
import com.astra.http.RequestCallback;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RemoteService.getInstance().init(this, "url.xml");
        RemoteService.getInstance().addHost("MainHost", "http://hulu.leanapp.cn");
        RemoteService.getInstance().addrequestDecorate("json", new JsonRequest());
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
        switch (view.getId()){
            case R.id.btn_login:
                RemoteService.getInstance().invoke("login")
                        .addParam("loginname", "17090020673")
                        .addParam("password", "ls123456")
                        .addParam("deviceId", getUUID())
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
                break;
        }
    }
}
