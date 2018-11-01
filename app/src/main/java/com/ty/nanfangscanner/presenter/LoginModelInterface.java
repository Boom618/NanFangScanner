package com.ty.nanfangscanner.presenter;

import android.content.SharedPreferences;

import com.ty.nanfangscanner.bean.LoginInfo;

/**
 * @author TY on 2018/11/1.
 */
public interface LoginModelInterface {

    /**
     * 保存数据
     * @param loginInfo
     * @param mSp
     * @param userNo
     * @param pwd
     */
    void saveData(LoginInfo loginInfo,SharedPreferences mSp,String userNo,String pwd);
}
