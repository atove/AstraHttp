package com.astra.simple.http;

import android.util.Log;

import com.astra.http.RequestDecorate;
import com.astra.http.RequestHeader;
import com.astra.http.RequestParameter;

import java.util.ArrayList;

/**
 * Created by Astra on 17/1/11.
 */

public class JsonRequest extends RequestDecorate {
    public JsonRequest() {
        addRequestHeader("Content-Type", "application/json");
    }

    @Override
    public String getMediaType() {
        return "application/json; charset=utf-8";
    }

    @Override
    public String getContent() {
        if (requestParameters == null){
            return "";
        }else {
            StringBuffer sb = new StringBuffer();
            sb.append("{\"");
            for (RequestParameter parameter : requestParameters){
                sb.append(parameter.getKey());
                sb.append("\":");
                sb.append("\"");
                sb.append(parameter.getValue());
                sb.append("\",");
            }
            String s = sb.substring(0, sb.length() - 1);
            s += "}";
            return "{\"loginname\":\"17090020673\",\"password\":\"ls123456\",\"deviceId\":\"00000000-0198-ca5a-3928-8dd80545e614\"}";
        }
    }

    @Override
    public ArrayList<RequestHeader> getRequestHeader() {

        String accesskey = "huluaff7f7b455e6d529|74ab4ab038263409b2e045435c796891|07ffebfd75192d2caaad03765514a53b|1484118151711";
        addRequestHeader("accesskey", accesskey);
        Log.d("accesskey", accesskey);
        return super.getRequestHeader();
    }

}
