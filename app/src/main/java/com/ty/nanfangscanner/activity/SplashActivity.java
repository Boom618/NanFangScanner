package com.ty.nanfangscanner.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.constant.ConstantUtil;

public class SplashActivity extends AppCompatActivity {

    private boolean isLogin;//判断是否登录过

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       // StatusBarUtil.setTransparent(SplashActivity.this);
        initData();
        initEvent();
    }

    private void initData() {
        //获取本地用户信息SharedPreferences数据
        SharedPreferences sp = getSharedPreferences(ConstantUtil.USER_SP_NAME, MODE_PRIVATE);
        isLogin = sp.getBoolean(ConstantUtil.SP_LOGIN_STATUS, false);
    }

    private void initEvent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLogin) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    //跳转到登录界面
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 3000);
    }
}
