[源码传送门](https://github.com/atove/AstraHttp)

[![](https://jitpack.io/v/atove/AstraHttp.svg)](https://jitpack.io/#atove/AstraHttp)

# AstraHttp
一个优雅的 http 网络请求框架，用极简的代码实现复杂的网络请求。  
基于 OkHttp3 封装

##如何使用
先在 build.gradle 的 repositories 添加:
```
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```
然后在 dependencies 添加:
```
	dependencies {
	        compile 'com.github.atove:AstraHttp:VERSION_CODE'
	}
```
用[这里](https://github.com/atove/AstraHttp/releases)的真实发行版本号, 替换 VERSION_CODE

##编写 MobileAPI 文件
在 assets 文件夹新建 url.xml 文件  
然后在 url.xml 文件中添加
```
<?xml version="1.0" encoding="utf-8"?>
<url>
    <!--登录-->
    <Node
        Key="login"
        Expires="0"
        NetType="post"
        Host="MainHost"
        DecorateType="json"
        Url="/api/user/login"
    />

    <!--上传图片-->
    <Node
        Key="file"
        Expires="0"
        NetType="post"
        Host="MainHost"
        DecorateType="from"
        Url="/api/resource/coverImage" />

</url>
```
在 Application 的 onCreate 方法中添加
```
//注册MobileAPI 文件
RemoteService.getInstance().init(this, "url.xml");
//注册Host，对应url.xml 中Host
RemoteService.getInstance().addHost("MainHost", "http://xxx.xxx.xxx");
//注册Request，对Request进行统一处理，对应url.xml 中DecorateType
RemoteService.getInstance().addrequestDecorate("from", new FromRequest());
RemoteService.getInstance().addrequestDecorate("json", new JsonRequest());
//是否打印Log
RemoteService.getInstance().isPrintLog(true);
```

##自定义Request
继承 RequestDecorate 可以实现对请求的统一处理。  
例如 FromRequest ：
```
public class FromRequest extends RequestDecorate {
    public FromRequest() {
    }

    @Override
    public String getMediaType() {
        return "multipart/form-data";
    }

    @Override
    public String getContent() {
        return null;
    }

    @Override
    public HashMap<String, String> getRequestHeader() {
        String accesskey = "xxxxxxxxxx";
        addRequestHeader("xxxxxxxxxx", accesskey);
        return super.getRequestHeader();
    }
}
```
##发起请求
```
RemoteService.getInstance().invoke("login")
        //添加 url 后缀，会拼接在 url 后边，用 / 分隔，非必须，可添加多个
        .addUrlSuffix("xxxxxx")
        //添加参数，可添加多个，value 可以是 String、int
        .addParam("loginname", "xxx")
        .addParam("password", "xxx")
        //上传文件，只能单个文件上传
        .setFile(new File("PATH"))
        //请求回调
        .setRequestCall(new RequestCallback() {
            @Override
            public void onSuccess(String content) {
                Log.d("请求成功", content);
            }

            @Override
            public void onFail(int code, String errorMessage) {

            }
        })
        .start();
```
##请求回调
继承 RequestCallback 对返回的数据进行统一处理  
例如 JsonRequestCallBack ：
```
public abstract class JsonRequestCallBack extends BaseRequestCallBack {

    private JSONObject jsonObject;
    Handler myHandler = new Handler() {
        //2.重写消息处理函数
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //判断发送的消息
                case ON_SUCCESS:
                    //更新View
                    success(jsonObject);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onSuccess(JSONObject result) {
        jsonObject = result;
        Message message = new Message();
        //发送消息与处理函数里一致
        message.what = ON_SUCCESS;
        //内部类调用外部类的变量
        myHandler.sendMessage(message);
    }

    public abstract void success(JSONObject result);
}
```

>**功能正在完善中，并持续更新！有问题欢迎留言**

#感谢
[OkHttp](http://square.github.io/okhttp/)