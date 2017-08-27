package lyh.rookiemall;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 刘营海 on 2017/8/6.
 */

public class okhttpjiexi {

}
class get{
    OkHttpClient client=new OkHttpClient();
    String get(String url)throws IOException{
        Request request=new Request.Builder()
                .url(url)
                .build();
        Response response=client.newCall(request).execute();
        return response.body().string();
    }
    /*
    * OkHttpClient 客户端对象
    * Request是OKhttp中访问请求
    * Builder是辅助类
    * Response是OKhttp响应*/
}
class postjson{
    public static final MediaType JSON
            =MediaType.parse("application/JSON,charset=utf-8");
    OkHttpClient client=new OkHttpClient();
    String post(String url,String json)throws IOException{
        RequestBody body=RequestBody.create(JSON,json);
        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response=client.newCall(request).execute();
        return response.body().string();
    }
    /*
    * MediaType 数据类型
    * RequestBody 请求类型*/
}
class postdata {
    OkHttpClient client = new OkHttpClient();
    String post(String url, String json) throws IOException {
        RequestBody body=new FormBody.Builder()
                .add("sadad","sdasf")
                .build();
        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response=client.newCall(request).execute();
        return response.body().string();
    }
    /*
    * FormEncodingBuilder 表单构造器
    * FormEncodingBuilder已被FormBody取代*/
}