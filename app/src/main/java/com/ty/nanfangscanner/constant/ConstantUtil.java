package com.ty.nanfangscanner.constant;


import com.ty.nanfangscanner.utils.Utils;

/**
 * desc:
 * author: XingZheng
 * date:2017/1/6
 */

public class ConstantUtil {

    public static String FILE_DIR = Utils.getSystemFilePath()+"/";

    public static  final String COMPANY_CODE="nanfangqiye";

    public static  final String TERMINALLID="7";

    /**
     * 用戶信息SharedPreferences文件名
     */
    public static  final String USER_SP_NAME="userInfo";

    /**
     *SP登录状态的Key
     */
    public static final String SP_LOGIN_STATUS="isLogin";

    /**
     * 存放产品和品牌信息的文件名
     */
    public static final String PRODUCT_SP_FILE="productAndBrand";

    /**
     * SP中的产品信息key
     */
    public static final String SP_PRODUCT="product";

    /**
     * SP中的品牌信息key
     */
    public static final String SP_BRAND="brand";

    public static final String SP_TOKEN = "accessToken";

    public static final String SP_TOKEN_UPDATE_TIME = "updateTokenTime";

    public static final String SP_TOKEN_TYPE="tokenType";

    public static final String SP_EXPIRESIN="expiresIn";

    public static final String SP_USERTYPE="userType";

    public static final String SP_USER_NAME="userName";

    public static final String SP_USER_PASSWORD="userPassword";

    /**
     * 存放登记数据信息的文件名
     */
    public static final String REGISTER_CACHE="registerInfo";

    /**
     * 存放激活数据信息的文件名
     */
    public static final String ACTIVATION_CACHE="activationInfo";
}
