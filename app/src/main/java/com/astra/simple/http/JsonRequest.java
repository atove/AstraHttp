package com.astra.simple.http;

import com.astra.http.RequestDecorate;
import com.astra.http.RequestHeader;
import com.astra.http.RequestParameter;

import java.util.ArrayList;

/**
 * Created by Astra on 17/1/11.
 */

public class JsonRequest extends RequestDecorate {
    public JsonRequest() {
        addRequestHeader("accesskey", "huluaff7f7b455e6d529|3f3907d90c3b108a7fa8c6fe580ec398c6ee83bb6e424a17cc8cd018bc680d52066c6fe5e1eca973|962e80bdc7b83327e5f4788ccbc2b1cb|1430356494320");
    }

    @Override
    public String getMediaType() {
        return "application/json";
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
            return s;
        }
    }

}
