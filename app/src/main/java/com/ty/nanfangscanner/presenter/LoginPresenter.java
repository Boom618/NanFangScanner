package com.ty.nanfangscanner.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.ty.nanfangscanner.bean.LoginInfo;
import com.ty.nanfangscanner.net.HttpMethods;
import com.ty.nanfangscanner.net.ProgressSubscriber;
import com.ty.nanfangscanner.utils.UIUtils;
import com.zhouyou.http.exception.ApiException;

import okhttp3.RequestBody;

/**
 * @author TY on 2018/11/1.
 */
public class LoginPresenter {

    private static final int CODE_400 = 400;
    private static final int CODE_401 = 401;
    private static final int CODE_1009 = 1009;

    LoginUiInterface loginUI;

    LoginModelInterface loginModel = new LoginModelInterfaceImpl();

    public LoginPresenter(LoginUiInterface loginUI) {
        this.loginUI = loginUI;
    }


    public void userLogin(Context context, final SharedPreferences mSp, final String userNo, final String pwd, RequestBody requestBody) {

        loginUI.showLoading();

        HttpMethods.getInstance().login(new ProgressSubscriber<LoginInfo>(context, true) {
            @Override
            public void onNext(LoginInfo loginInfo) {
                super.onNext(loginInfo);

                loginUI.hideLoading();

                if (loginInfo != null) {
                    loginModel.saveData(loginInfo, mSp, userNo, pwd);

                    loginUI.gotoNextActivity();
                }

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);

                loginUI.hideLoading();

                String message;
                if (e.getCode() == CODE_400) {
                    message = "用户名或密码错误！";
                } else if (e.getCode() == CODE_401) {
                    message = "权限拒绝，请联系管理员";
                } else if (e.getCode() == CODE_1009) {
                    message = "网络异常";
                } else {
                    message = e.getCode() + ":" + e.getMessage();
                }
                UIUtils.showToast(message);
            }
        }, requestBody);
    }
}
