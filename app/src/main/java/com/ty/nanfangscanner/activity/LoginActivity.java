package com.ty.nanfangscanner.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.bean.LoginInfo;
import com.ty.nanfangscanner.bean.PostLoginJson;
import com.ty.nanfangscanner.constant.ConstantUtil;
import com.ty.nanfangscanner.net.HttpMethods;
import com.ty.nanfangscanner.net.ProgressSubscriber;
import com.ty.nanfangscanner.utils.TimeUtil;
import com.ty.nanfangscanner.utils.UIUtils;
import com.ty.nanfangscanner.utils.Utils;
import com.zhouyou.http.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_num)
    EditText etNum;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    private SharedPreferences mSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        tvVersion.setText(Utils.getVersion());
        mSp = getSharedPreferences(ConstantUtil.USER_SP_NAME, MODE_PRIVATE);
        String  userName = mSp.getString(ConstantUtil.SP_USER_NAME, "");
        String password = mSp.getString(ConstantUtil.SP_USER_PASSWORD, "");
        if (!TextUtils.isEmpty(userName)&&!TextUtils.isEmpty(password)){
            etNum.setText(userName);
            etPassword.setText(password);
        }
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNo = etNum.getText().toString().trim();
                String pwd = etPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(userNo) && !TextUtils.isEmpty(pwd)) {
                    PostLoginJson loginJson = new PostLoginJson();
                    loginJson.setClientName(userNo);
                    loginJson.setClientPassword(pwd);
                    String jsonStr = Utils.toJson(loginJson, 1);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonStr);
                    doLogin(requestBody);
                } else {
                    UIUtils.showToast("账号/密码不能为空");
                }
            }
        });
    }

    private void doLogin(RequestBody requestBody) {
        HttpMethods.getInstance().login(new ProgressSubscriber<LoginInfo>(this, true) {
            @Override
            public void onNext(LoginInfo loginInfo) {
                super.onNext(loginInfo);
                if (loginInfo != null) {
                    String userNo = etNum.getText().toString().trim();
                    String pwd = etPassword.getText().toString().trim();
                    mSp.edit().putString(ConstantUtil.SP_TOKEN, loginInfo.getAccessToken())
                            .putString(ConstantUtil.SP_TOKEN_TYPE, loginInfo.getTokenType())
                            .putString(ConstantUtil.SP_USER_NAME, userNo)
                            .putString(ConstantUtil.SP_USER_PASSWORD, pwd)
                            .putString(ConstantUtil.SP_TOKEN_UPDATE_TIME, TimeUtil.getCurretTime())//token更新的时间
                            .putInt(ConstantUtil.SP_EXPIRESIN, loginInfo.getExpiresIn())
                            .putInt(ConstantUtil.SP_USERTYPE, loginInfo.getUserType())
                            .apply();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                String message;
                if (e.getCode()==400){
                    message="用户名或密码错误！";
                }else if (e.getCode()==401){
                    message="权限拒绝，请联系管理员";
                }else if (e.getCode()==1009){
                    message="网络异常";
                } else{
                    message=e.getCode()+":"+e.getMessage();
                }
                UIUtils.showToast(message);
            }
        }, requestBody);
    }
}
