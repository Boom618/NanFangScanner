package com.ty.nanfangscanner.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.bean.CheckInfo;
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

/**
 * @author TY
 */
public class CheckActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_register_time)
    TextView tvRegisterTime;
    @BindView(R.id.tv_register_product)
    TextView tvRegisterProduct;
    @BindView(R.id.tv_activation_time)
    TextView tvActivationTime;
    @BindView(R.id.tv_activation_status)
    TextView tvActivationStatus;
    @BindView(R.id.tv_section_num)
    TextView tvSectionNum;
    @BindView(R.id.tv_activation_num)
    TextView tvActivationNum;
    @BindView(R.id.tv_print_factory)
    TextView tvPrintFactory;
    @BindView(R.id.tv_start_code_num)
    TextView tvStartCodeNum;
    @BindView(R.id.tv_end_code_num)
    TextView tvEndCodeNum;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    private String authorization;
    private SharedPreferences mSp;
    private String userName;
    private String password;
    private String tokenUpdateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        ButterKnife.bind(this);
        tvTitle.setText("抽查检验");
        ivBack.setOnClickListener(this);
        etCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    etCode.requestFocus();//获取焦点
                }
            }
        });
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String result = charSequence.toString().replace("\n", "");
                etCode.getText().clear();
                if (!TextUtils.isEmpty(result) && result.length() > 5) {
                    int timeInterval = TimeUtil.getTimeInterval(TimeUtil.getCurretTime(), tokenUpdateTime);
                    if (timeInterval > 2) {
                        updateToken(result);
                    } else {
                        doCheck(result);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mSp = getSharedPreferences(ConstantUtil.USER_SP_NAME, MODE_PRIVATE);
        String mTokenStr = mSp.getString(ConstantUtil.SP_TOKEN, "");
        String mTokenType = mSp.getString(ConstantUtil.SP_TOKEN_TYPE, "");
        userName = mSp.getString(ConstantUtil.SP_USER_NAME, "");
        password = mSp.getString(ConstantUtil.SP_USER_PASSWORD, "");
        tokenUpdateTime = mSp.getString(ConstantUtil.SP_TOKEN_UPDATE_TIME, "");
        authorization = mTokenType + " " + mTokenStr;
    }

    private void doCheck(String codeUrl) {
        HttpMethods.getInstance().doCheck(new ProgressSubscriber<CheckInfo>(this, true) {
            @Override
            public void onNext(CheckInfo checkInfo) {
                super.onNext(checkInfo);
                if (checkInfo != null) {
                    llContent.setVisibility(View.VISIBLE);
                    String enterpriseSeqnumStart = checkInfo.getEnterpriseSeqnumStart() + "";
                    String enterpriseSeqnumEnd = checkInfo.getEnterpriseSeqnumEnd() + "";
                    String registrationTime = checkInfo.getRegistrationTime();
                    String productName = checkInfo.getProductName();
                    String activationTime = checkInfo.getActivationTime();
                    String activationStatus = checkInfo.getActivationStatus();
                    String activationCount = checkInfo.getActivationCount() + "";
                    String seqNumCount = checkInfo.getSeqNumCount() + "";
                    String factoryName = checkInfo.getFactoryName();

                    if (TextUtils.isEmpty(enterpriseSeqnumStart) || "0".equals(enterpriseSeqnumStart)) {
                        enterpriseSeqnumStart = "";
                    }
                    if (TextUtils.isEmpty(enterpriseSeqnumEnd) || "0".equals(enterpriseSeqnumEnd)) {
                        enterpriseSeqnumEnd = "";
                    }
                    if (TextUtils.isEmpty(registrationTime)) {
                        registrationTime = "";
                    }
                    if (TextUtils.isEmpty(productName)) {
                        productName = "";
                    }
                    if (TextUtils.isEmpty(activationTime)) {
                        activationTime = "";
                    }
                    if (TextUtils.isEmpty(activationStatus)) {
                        activationStatus = "";
                    }
                    if (TextUtils.isEmpty(activationCount) || "0".equals(activationCount)) {
                        activationCount = "";
                    }
                    if (TextUtils.isEmpty(seqNumCount) || "0".equals(seqNumCount)) {
                        seqNumCount = "";
                    }
                    if (TextUtils.isEmpty(factoryName)) {
                        factoryName = "";
                    }
                    tvStartCodeNum.setText("起始码号：" + enterpriseSeqnumStart);
                    tvEndCodeNum.setText("结束码号：" + enterpriseSeqnumEnd);
                    tvRegisterTime.setText("登记时间：" + registrationTime);
                    tvRegisterProduct.setText("登记产品：" + productName);
                    tvActivationTime.setText("激活时间：" + activationTime);
                    tvActivationStatus.setText("激活状态：" + activationStatus);
                    tvSectionNum.setText("号段码量：" + seqNumCount);
                    tvActivationNum.setText("激活码量：" + activationCount);
                    tvPrintFactory.setText("打印工厂：" + factoryName);
                }

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                String message;
                if (e.getCode() == 401) {
                    message = "权限拒绝，请联系管理员";
                } else if (e.getCode() == 1009) {
                    message = "网络异常";
                } else {
                    message = e.getCode() + ":" + e.getMessage();
                }
                UIUtils.showToast(message);
            }
        }, authorization, ConstantUtil.COMPANY_CODE, codeUrl, ConstantUtil.TERMINALLID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void updateToken(final String codeUrl) {
        PostLoginJson loginJson = new PostLoginJson();
        loginJson.setClientName(userName);
        loginJson.setClientPassword(password);
        String jsonStr = Utils.toJson(loginJson, 1);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonStr);
        HttpMethods.getInstance().login(new ProgressSubscriber<LoginInfo>(this, true) {
            @Override
            public void onNext(LoginInfo loginInfo) {
                super.onNext(loginInfo);
                if (loginInfo != null) {
                    mSp.edit().putString(ConstantUtil.SP_TOKEN, loginInfo.getAccessToken())
                            .putString(ConstantUtil.SP_TOKEN_TYPE, loginInfo.getTokenType())
                            //token更新的时间
                            .putString(ConstantUtil.SP_TOKEN_UPDATE_TIME, TimeUtil.getCurretTime())
                            .apply();
                    authorization = loginInfo.getTokenType() + " " + loginInfo.getAccessToken();
                    doCheck(codeUrl);
                } else {
                    UIUtils.showToast("Token刷新失败,请保存数据,重新登录提交");
                }

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                UIUtils.showToast("Token刷新失败,请保存数据,重新登录提交");
            }
        }, requestBody);
    }
}
