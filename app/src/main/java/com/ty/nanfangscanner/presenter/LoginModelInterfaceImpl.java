package com.ty.nanfangscanner.presenter;

import android.content.SharedPreferences;

import com.ty.nanfangscanner.bean.LoginInfo;
import com.ty.nanfangscanner.constant.ConstantUtil;
import com.ty.nanfangscanner.utils.TimeUtil;


/**
 * @author TY on 2018/11/1.
 */
public class LoginModelInterfaceImpl implements LoginModelInterface {


    @Override
    public void saveData(LoginInfo loginInfo,SharedPreferences mSp,String userNo,String pwd) {

        mSp.edit().putString(ConstantUtil.SP_TOKEN, loginInfo.getAccessToken())
                .putString(ConstantUtil.SP_TOKEN_TYPE, loginInfo.getTokenType())
                .putString(ConstantUtil.SP_USER_NAME, userNo)
                .putString(ConstantUtil.SP_USER_PASSWORD, pwd)
                //token更新的时间
                .putString(ConstantUtil.SP_TOKEN_UPDATE_TIME, TimeUtil.getCurretTime())
                .putInt(ConstantUtil.SP_EXPIRESIN, loginInfo.getExpiresIn())
                .putInt(ConstantUtil.SP_USERTYPE, loginInfo.getUserType())
                .apply();
    }
}
