package com.astra.http;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 获取UrlData实体
 * Created by Astra on 16/12/1.
 */

class UrlConfigManager {
    private ArrayList<UrlData> urlDatas;
    private HashMap<String, String> hosts;
    protected UrlConfigManager(Context context, String path) {
        try {
            InputStream is = context.getAssets().open(path);
            urlDatas = parse(is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UrlData findURL (String key){
        if (urlDatas != null && urlDatas.size() > 0){
            for (UrlData urlData : urlDatas){
                if (key.equals(urlData.getKey())){
                    return urlData;
                }
            }
        }
        return null;
    }

    private ArrayList<UrlData> parse(InputStream is) throws Exception {
        ArrayList<UrlData> urlDatas = null;
        UrlData urlData = null;
        XmlPullParser parser = Xml.newPullParser(); //由android.util.Xml创建一个XmlPullParser实例
        parser.setInput(is, "UTF-8");               //设置输入流 并指明编码方式

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    urlDatas = new ArrayList<>();

                    break;
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("Node")) {
                        urlData = new UrlData();
                        urlData.setKey(parser.getAttributeValue(null, "Key"));
                        urlData.setExpires(Integer.parseInt(parser.getAttributeValue(null, "Expires")));
                        urlData.setNetType(parser.getAttributeValue(null, "NetType"));
                        urlData.setUrl(getUrl(parser.getAttributeValue(null, "Host"), parser.getAttributeValue(null, "Url")));

                    }else if (parser.getName().equals("Host")){
                        if (hosts == null){
                            hosts = new HashMap<>();
                        }
                        String key = parser.getAttributeValue(null, "Key");
                        parser.next();
                        hosts.put(key, parser.getText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("Node")) {
                        urlDatas.add(urlData);
                        urlData = null;
                    }
                    break;
            }
            eventType = parser.next();
        }
        return urlDatas;
    }

    private String getUrl(String hostKey, String url){
        if (hosts == null || hosts.isEmpty() || hosts.get(hostKey) == null){
            return url;
        }else {
            return hosts.get(hostKey) + url;
        }
    }

}
