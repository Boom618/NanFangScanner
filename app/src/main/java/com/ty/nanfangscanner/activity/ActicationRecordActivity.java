package com.ty.nanfangscanner.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.adapter.ActivationRecordAdapter;
import com.ty.nanfangscanner.bean.ActivationRecordInfo;
import com.ty.nanfangscanner.bean.LoginInfo;
import com.ty.nanfangscanner.bean.PostLoginJson;
import com.ty.nanfangscanner.bean.PostRecordJson;
import com.ty.nanfangscanner.constant.ConstantUtil;
import com.ty.nanfangscanner.net.HttpMethods;
import com.ty.nanfangscanner.net.ProgressSubscriber;
import com.ty.nanfangscanner.utils.TimeUtil;
import com.ty.nanfangscanner.utils.UIUtils;
import com.ty.nanfangscanner.utils.Utils;
import com.ty.nanfangscanner.view.CustomDatePicker;
import com.ty.nanfangscanner.view.MyTextView;
import com.ty.nanfangscanner.view.SpacesItemDecoration;
import com.zhouyou.http.exception.ApiException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ActicationRecordActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_start_time)
    MyTextView tvStartTime;
    @BindView(R.id.tv_end_time)
    MyTextView tvEndTime;
    @BindView(R.id.iv_query)
    ImageView ivQuery;
    @BindView(R.id.iv_exit)
    ImageView ivExit;
    @BindView(R.id.rv_record)
    RecyclerView rvRecord;
    @BindView(R.id.tv_null)
    TextView tvNull;
    private String authorization;
    List<ActivationRecordInfo> list = new ArrayList<>();
    private ActivationRecordAdapter adapter;
    private SharedPreferences mSp;
    private String userName;
    private String password;
    private String tokenUpdateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actication_record);
        ButterKnife.bind(this);
        tvTitle.setText("激活记录");
        ivBack.setOnClickListener(this);
        ivQuery.setOnClickListener(this);
        ivExit.setOnClickListener(this);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(UIUtils.getContext());
        rvRecord.addItemDecoration(new SpacesItemDecoration(8));
        rvRecord.setLayoutManager(mLayoutManager);
        adapter = new ActivationRecordAdapter(list);
        rvRecord.setAdapter(adapter);
        mSp = getSharedPreferences(ConstantUtil.USER_SP_NAME, MODE_PRIVATE);
        String mTokenStr = mSp.getString(ConstantUtil.SP_TOKEN, "");
        String mTokenType = mSp.getString(ConstantUtil.SP_TOKEN_TYPE, "");
        userName = mSp.getString(ConstantUtil.SP_USER_NAME, "");
        password = mSp.getString(ConstantUtil.SP_USER_PASSWORD, "");
        tokenUpdateTime = mSp.getString(ConstantUtil.SP_TOKEN_UPDATE_TIME, "");
        authorization = mTokenType + " " + mTokenStr;
    }

    private void getActivationRecord(RequestBody requestBody) {
        HttpMethods.getInstance().getActivationRecord(new ProgressSubscriber<List<ActivationRecordInfo>>(this, true) {
            @Override
            public void onNext(List<ActivationRecordInfo> rtivationRecordInfos) {
                super.onNext(rtivationRecordInfos);
                if (rtivationRecordInfos != null) {
                    tvNull.setVisibility(View.GONE);
                    rvRecord.setVisibility(View.VISIBLE);
                    list.clear();
                    list.addAll(rtivationRecordInfos);
                    adapter.notifyDataSetChanged();
                } else {
                    tvNull.setVisibility(View.VISIBLE);
                    rvRecord.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                String message;
                if (e.getCode()==401){
                    message="权限拒绝，请联系管理员";
                }else if (e.getCode()==1009){
                    message="网络异常";
                } else{
                    message=e.getCode()+":"+e.getMessage();
                }
                UIUtils.showToast(message);
            }
        }, authorization, requestBody, ConstantUtil.COMPANY_CODE, ConstantUtil.TERMINALLID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_exit:
                finish();
                break;

            case R.id.tv_start_time:
                showTimeDialog(tvStartTime);
                break;

            case R.id.tv_end_time:
                showTimeDialog(tvEndTime);
                break;

            case R.id.iv_query:
                int timeInterval = TimeUtil.getTimeInterval(TimeUtil.getCurretTime(), tokenUpdateTime);
                if (timeInterval>2){
                    updateToken();
                }else {
                    initParam();
                }
                break;
        }
    }

    private void initParam() {
        String startTime = tvStartTime.getText().toString();
        String endTime = tvEndTime.getText().toString();
        if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
            startTime = getTime(startTime);
            endTime = getTime(endTime);
        }else {
            startTime = "";
            endTime = "";
        }
        PostRecordJson info = new PostRecordJson();
        info.setStartTime(startTime);
        info.setEndTime(endTime);
        String jsonStr = Utils.toJson(info, 1);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonStr);
        getActivationRecord(requestBody);
    }

    private void showTimeDialog(final MyTextView tvDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        Calendar calendar = Calendar.getInstance();
        final int seconds = calendar.get(Calendar.SECOND);    // 秒
        CustomDatePicker datePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                String s;
                if (seconds < 10) {
                    s = "0" + seconds;
                } else {
                    s = seconds + "";
                }
                tvDate.setText(time + ":" + s);
            }
        }, "2010-01-01 00:00", "2999-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        datePicker.showSpecificTime(true); // 显示时和分
        datePicker.setIsLoop(false); // 允许循环滚动
        datePicker.show(now);
    }

    private String getTime(String time) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt1;
        try {
            dt1 = sdf.parse(time);
            result = dt1.getTime() + "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void updateToken() {
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
                            .putString(ConstantUtil.SP_TOKEN_UPDATE_TIME, TimeUtil.getCurretTime())//token更新的时间
                            .apply();
                    authorization = loginInfo.getTokenType() + " " + loginInfo.getAccessToken();
                    initParam();
                }else {
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
