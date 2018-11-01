package com.ty.nanfangscanner.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ty.nanfangscanner.bean.ActivationInfo;
import com.ty.nanfangscanner.bean.ActivationRecordInfo;
import com.ty.nanfangscanner.bean.BrandInfo;
import com.ty.nanfangscanner.bean.CheckInfo;
import com.ty.nanfangscanner.bean.LoginInfo;
import com.ty.nanfangscanner.bean.ProductInfo;
import com.ty.nanfangscanner.bean.RegisterCheckInfo;
import com.ty.nanfangscanner.bean.RegisterRecordInfo;
import com.ty.nanfangscanner.constant.ApiNameConstant;
import com.ty.nanfangscanner.gson.DoubleDefault0Adapter;
import com.ty.nanfangscanner.gson.IntegerDefault0Adapter;
import com.ty.nanfangscanner.gson.LongDefault0Adapter;
import com.ty.nanfangscanner.gson.StringDefault0Adapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * description:
 * author: XingZheng
 * date: 2016/11/22.
 * @author TY
 */
public class HttpMethods {

    /**
     * 默认超时时间
     */
    public static final int DEFAULT_TIMEOUT = 20;
    private  Retrofit mRetrofit;
    private final BaseApiService mService;
    private static Gson gson;
    private String baseUrl= ApiNameConstant.BASE_URL;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private HttpMethods() {
        //创建OKHttpClient
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        mRetrofit = new Retrofit.Builder()
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();

        mService = mRetrofit.create(BaseApiService.class);

    }

    public HttpMethods(String url) {
        //创建OKHttpClient
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        mRetrofit = new Retrofit.Builder()
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();

        mService = mRetrofit.create(BaseApiService.class);

    }

    //构建单例
    public static class SingletonHolder {
        public static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void login(ProgressSubscriber<LoginInfo> subscriber, RequestBody requestBody){
        mService.login(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getProducts(ProgressSubscriber<List<ProductInfo>> subscriber, String authorization,
                            String companyCode, String terminalId){
        mService.getProducts(authorization,companyCode,terminalId,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getBrands(ProgressSubscriber<List<BrandInfo>> subscriber, String authorization,
                          String companyCode, String terminalId){
        mService.getBrands(authorization,companyCode,terminalId,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 登记检验
     * @param subscriber
     * @param authorization
     * @param requestBody
     * @param companyCode
     * @param terminalId
     */
    public void doRegisterCheck(ProgressSubscriber<List<RegisterCheckInfo>> subscriber, String authorization,
                                RequestBody requestBody, String companyCode, String terminalId){
        mService.registerCheck(authorization,requestBody,companyCode,terminalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 登记
     * @param subscriber
     * @param authorization
     * @param requestBody
     * @param companyCode
     * @param terminalId
     */
    public void doRegister(ProgressSubscriber<List<RegisterCheckInfo>> subscriber, String authorization,
                                RequestBody requestBody, String companyCode, String terminalId){
        mService.register(authorization,requestBody,companyCode,terminalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 激活
     * @param subscriber
     * @param authorization
     * @param requestBody
     * @param companyCode
     * @param terminalId
     */
    public void doActivation(ProgressSubscriber<List<ActivationInfo>> subscriber, String authorization,
                             RequestBody requestBody, String companyCode, String terminalId){
        mService.activation(authorization,requestBody,companyCode,terminalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 号段检验
     * @param subscriber
     * @param authorization
     * @param codeUrl
     * @param companyCode
     * @param terminalId
     */
    public void doCheck(ProgressSubscriber<CheckInfo> subscriber, String authorization,
                        String companyCode,  String codeUrl,String terminalId){
        mService.check(authorization,companyCode,codeUrl,terminalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取激活记录
     * @param subscriber
     * @param authorization
     * @param requestBody
     * @param companyCode
     * @param terminalId
     */
    public void getActivationRecord(ProgressSubscriber<List<ActivationRecordInfo>> subscriber, String authorization,
                                    RequestBody requestBody, String companyCode, String terminalId){
        mService.getActivationRecord(authorization,requestBody,companyCode,terminalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取登记记录
     * @param subscriber
     * @param authorization
     * @param requestBody
     * @param companyCode
     * @param terminalId
     */
    public void getRegisterRecord(ProgressSubscriber<List<RegisterRecordInfo>> subscriber, String authorization,
                                  RequestBody requestBody, String companyCode, String terminalId){
        mService.getRegisterRecord(authorization,requestBody,companyCode,terminalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public static Gson buildGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                    .registerTypeAdapter(long.class, new LongDefault0Adapter())
                    .registerTypeAdapter(String.class, new StringDefault0Adapter())
                    .create();
        }
        return gson;
    }

}
