package com.android.htc.greenroad.https;

import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liukun on 16/3/9.
 */
public class HttpMethods {

    //public static final String BASE_URL = "http://192.168.2.122/lvsetondao/index.php/Interfacy/";
   public static final String BASE_URL = "http://greenft.githubshop.com/index.php/Interfacy/";
//   public static final String BASE_URL = "http://greenft.githubshop."+ SPUtils.get(GreenRoadApplication.sApplication,SPUtils.CONFIG_PORT,"com")+"/index.php/Interfacy/";
//   public static final String BASE_URL = "http://192.168.98.99:"+ SPUtils.get(GreenRoadApplication.sApplication,SPUtils.CONFIG_PORT,"88")+"/index.php/Interfacy/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private static HttpMethods sMethods;

    //构造方法私有
    private HttpMethods() {

    }

    //获取单例
    public static HttpMethods getInstance(){
        if (sMethods == null) {
            sMethods = new HttpMethods();
        }
        Logger.i(BASE_URL);
        return sMethods;
    }

    public HttpUtilsApi getApi() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
/*
        String url_header= (String) SPUtils.get(GreenRoadApplication.sApplication, SPUtils.LINE_CONFIG, "http://greenft.githubshop.com");

        final String BASE_URL = url_header+"/index.php/Interfacy/";*/

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        HttpUtilsApi httpUtilsApi = retrofit.create(HttpUtilsApi.class);
        return httpUtilsApi;
    }

    public <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
         o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
