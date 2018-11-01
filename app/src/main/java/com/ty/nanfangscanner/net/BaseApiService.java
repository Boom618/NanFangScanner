package com.ty.nanfangscanner.net;

import com.ty.nanfangscanner.bean.ActivationInfo;
import com.ty.nanfangscanner.bean.ActivationRecordInfo;
import com.ty.nanfangscanner.bean.BrandInfo;
import com.ty.nanfangscanner.bean.CheckInfo;
import com.ty.nanfangscanner.bean.LoginInfo;
import com.ty.nanfangscanner.bean.ProductInfo;
import com.ty.nanfangscanner.bean.RegisterCheckInfo;
import com.ty.nanfangscanner.bean.RegisterRecordInfo;
import com.ty.nanfangscanner.constant.ApiNameConstant;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author TY
 */
public interface BaseApiService {

    /**
     * 登录
     * @param requestBody
     * @return
     */
    @Headers("Accept: application/json")
    @POST(ApiNameConstant.DO_LOGIN)
    Observable<LoginInfo> login(@Body RequestBody requestBody);

    /**
     * 获取产品列表
     *
     * @param authorization
     * @param companyCode
     * @param terminalId
     * @param status
     * @return
     */
    @Headers("Accept: application/json")
    @GET(ApiNameConstant.GET_PRODUCT)
    Observable<List<ProductInfo>> getProducts(@Header("Authorization") String authorization,
                                              @Query("companyCode") String companyCode,
                                              @Query("terminalId") String terminalId,
                                              @Query("status") int status);

    /**
     * 获取品牌列表
     *
     * @param authorization
     * @param companyCode
     * @param terminalId
     * @param status
     * @return
     */
    @Headers("Accept: application/json")
    @GET(ApiNameConstant.GET_BRANDS)
    Observable<List<BrandInfo>> getBrands(@Header("Authorization") String authorization,
                                          @Query("companyCode") String companyCode,
                                          @Query("terminalId") String terminalId,
                                          @Query("status") int status);


    /**
     *
     * 登记检验
     * @param authorization
     * @param requestBody
     * @param companyCode
     * @param terminalId
     * @return
     */
    @Headers("Accept: application/json")
    @POST(ApiNameConstant.REGISTRATION_CHECK)
    Observable<List<RegisterCheckInfo>> registerCheck(@Header("Authorization") String authorization,
                                                      @Body RequestBody requestBody,
                                                      @Path("companyCode") String companyCode,
                                                      @Query("terminalId") String terminalId);

    /**
     * 号段登记
     *
     * @param authorization
     * @param requestBody
     * @param companyCode
     * @param terminalId
     * @return
     */
    @Headers("Accept: application/json")
    @POST(ApiNameConstant.REGISTRATION)
    Observable<List<RegisterCheckInfo>> register(@Header("Authorization") String authorization,
                                                 @Body RequestBody requestBody,
                                                 @Path("companyCode") String companyCode,
                                                 @Query("terminalId") String terminalId);

    /**
     * 激活
     *
     * @param authorization
     * @param requestBody
     * @param companyCode
     * @param terminalId
     * @return
     */
    @Headers("Accept: application/json")
    @POST(ApiNameConstant.DO_ACTIVATION)
    Observable<List<ActivationInfo>> activation(@Header("Authorization") String authorization,
                                                @Body RequestBody requestBody,
                                                @Path("companyCode") String companyCode,
                                                @Query("terminalId") String terminalId);

    /**
     * 号段检验
     *
     * @param authorization
     * @param codeUrl
     * @param companyCode
     * @param terminalId
     * @return
     */
    @Headers("Accept: application/json")
    @POST(ApiNameConstant.DO_CHECK)
    Observable<CheckInfo> check(@Header("Authorization") String authorization,
                                @Path("companyCode") String companyCode,
                                @Query("codeUrl") String codeUrl,
                                @Query("terminalId") String terminalId);

    /**
     * 获取激活记录
     * @param authorization
     * @param requestBody
     * @param companyCode
     * @param terminalId
     * @return
     */
    @Headers("Accept: application/json")
    @POST(ApiNameConstant.ACTIVATION_RECORD)
    Observable<List<ActivationRecordInfo>> getActivationRecord(@Header("Authorization") String authorization,
                                                               @Body RequestBody requestBody,
                                                               @Path("companyCode") String companyCode,
                                                               @Query("terminalId") String terminalId);

    /**
     * 获取登记记录
     * @param authorization
     * @param requestBody
     * @param companyCode
     * @param terminalId
     * @return
     */
    @Headers("Accept: application/json")
    @POST(ApiNameConstant.REGISTRATION_RECORD)
    Observable<List<RegisterRecordInfo>> getRegisterRecord(@Header("Authorization") String authorization,
                                                           @Body RequestBody requestBody,
                                                           @Path("companyCode") String companyCode,
                                                           @Query("terminalId") String terminalId);
}
