package com.ty.nanfangscanner.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.activity.base.BaseActivity;
import com.ty.nanfangscanner.bean.PostLoginJson;
import com.ty.nanfangscanner.constant.ConstantUtil;
import com.ty.nanfangscanner.presenter.LoginPresenter;
import com.ty.nanfangscanner.presenter.LoginUIInterface;
import com.ty.nanfangscanner.utils.UIUtils;
import com.ty.nanfangscanner.utils.Utils;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 登录 Activity
 * @author TY
 */
public class LoginActivity extends BaseActivity implements LoginUIInterface {

    @BindView(R.id.et_num)
    EditText etNum;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    private SharedPreferences mSp;

    private LoginPresenter loginPresenter = new LoginPresenter(this);

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        tvVersion.setText(Utils.getVersion());
        mSp = getSharedPreferences(ConstantUtil.USER_SP_NAME, MODE_PRIVATE);
        String userName = mSp.getString(ConstantUtil.SP_USER_NAME, "");
        String password = mSp.getString(ConstantUtil.SP_USER_PASSWORD, "");
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
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

                    loginPresenter.userLogin(LoginActivity.this, mSp, userNo, pwd, requestBody);

                } else {
                    UIUtils.showToast("账号/密码不能为空");
                }
            }
        });
    }

    @Override
    protected int setActivityLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
