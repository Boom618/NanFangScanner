package com.ty.nanfangscanner.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.activity.base.BaseActivity;
import com.ty.nanfangscanner.constant.ConstantUtil;

/**
 * @author TY
 */
public class SplashActivity extends BaseActivity {

    /**
     * 判断是否登录过
     */
    private boolean isLogin;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        initEvent();
    }

    @Override
    protected int setActivityLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        //获取本地用户信息SharedPreferences数据
        SharedPreferences sp = getSharedPreferences(ConstantUtil.USER_SP_NAME, MODE_PRIVATE);
        isLogin = sp.getBoolean(ConstantUtil.SP_LOGIN_STATUS, false);
    }


    private void initEvent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLogin) {
                    //startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    gotoActivity(MainActivity.class);
                } else {
                    //跳转到登录界面
                    //startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    gotoActivity(LoginActivity.class);
                }
                finish();
            }
        }, 3000);
    }
}
