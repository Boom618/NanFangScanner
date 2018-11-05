package com.ty.nanfangscanner.constant;

/**
 * @author Administrator
 * @date 2017/7/26
 */

public class ApiNameConstant {

//    public final static String BASE_URL = "https://top-api.aax6.cn/";

      public final static String BASE_URL = "https://top-api-qa.aax6.cn/";//测试地址

    /**
     * 登录
     */
    public final static String DO_LOGIN = "data-service/isv/oauth/token";

    /**
     * 获取产品列表
     */
    public final static String GET_PRODUCT = "data-service/isv/products";

    /**
     * 获取品牌列表
     */
    public final static String GET_BRANDS = "data-service/isv/brands";

    /**
     * 登记号段检验（检查码段接口）
     */
    public final static String REGISTRATION_CHECK = "code-service/isv/registrationAndActivation/company/{companyCode}/seqNumRegistrationCheck";

    /**
     * 号段登记
     */
    public final static String REGISTRATION = "code-service/isv/registrationAndActivation/company/{companyCode}/seqNumRegistration";

    /**
     * 激活
     */
    public final static String DO_ACTIVATION = "code-service/isv/registrationAndActivation/company/{companyCode}/seqNumActivation";


    /**
     * 号段检验
     */
    public final static String DO_CHECK = "code-service/isv/registrationAndActivation/company/{companyCode}/seqNumInfo";

    /**
     * 号段登记记录
     */
    public final static String REGISTRATION_RECORD = "code-service/isv/registrationAndActivation/company/{companyCode}/seqNumRegistrationRecord";

    /**
     * 号段激活记录
     */
    public final static String ACTIVATION_RECORD = "code-service/isv/registrationAndActivation/company/{companyCode}/seqNumActivationRecord";
}
