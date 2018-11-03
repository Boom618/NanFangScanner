package com.ty.nanfangscanner.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.adapter.QRCodeAdapter;
import com.ty.nanfangscanner.bean.ActivationInfo;
import com.ty.nanfangscanner.bean.LoginInfo;
import com.ty.nanfangscanner.bean.PostLoginJson;
import com.ty.nanfangscanner.constant.ConstantUtil;
import com.ty.nanfangscanner.net.HttpMethods;
import com.ty.nanfangscanner.net.ProgressSubscriber;
import com.ty.nanfangscanner.utils.FileUtils;
import com.ty.nanfangscanner.utils.TimeUtil;
import com.ty.nanfangscanner.utils.UIUtils;
import com.ty.nanfangscanner.utils.Utils;
import com.ty.nanfangscanner.view.DividerItemDecoration;
import com.wevey.selector.dialog.DialogInterface;
import com.wevey.selector.dialog.NormalAlertDialog;
import com.zhouyou.http.exception.ApiException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 激活 Activity
 *
 * @author TY
 */
public class ActiveActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.iv_reset)
    TextView tvReset;
    @BindView(R.id.rv_code)
    RecyclerView rvCode;
    @BindView(R.id.iv_cancel)
    ImageView ivCancel;
    @BindView(R.id.iv_submit)
    ImageView ivSubmit;

    private List<String> qrCodeList;
    private QRCodeAdapter adapter;
    private String authorization;
    private static final int REQUEST_CODE = 111;
    private static final int RESULT_CODE = 222;
    private String userName;
    private String password;
    private String tokenUpdateTime;
    private SharedPreferences mSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        mSp = getSharedPreferences(ConstantUtil.USER_SP_NAME, MODE_PRIVATE);
        String mTokenStr = mSp.getString(ConstantUtil.SP_TOKEN, "");
        String mTokenType = mSp.getString(ConstantUtil.SP_TOKEN_TYPE, "");
        authorization = mTokenType + " " + mTokenStr;

        userName = mSp.getString(ConstantUtil.SP_USER_NAME, "");
        password = mSp.getString(ConstantUtil.SP_USER_PASSWORD, "");
        tokenUpdateTime = mSp.getString(ConstantUtil.SP_TOKEN_UPDATE_TIME, "");

        String jsonStr = FileUtils.readFile(ConstantUtil.ACTIVATION_CACHE);
        if (!TextUtils.isEmpty(jsonStr)) {
            Gson gson = new Gson();
            List<String> infos = gson.fromJson(jsonStr, new TypeToken<List<String>>() {
            }.getType());
            if (infos != null) {
                qrCodeList.addAll(infos);
                showCacheDialog(infos);
                tvCount.setText(infos.size() + "");
            }
        }
    }

    private void initView() {
        setContentView(R.layout.activity_active);
        ButterKnife.bind(this);
        tvTitle.setText("激活");
        ivBack.setOnClickListener(this);
        tvReset.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        ivSubmit.setOnClickListener(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(UIUtils.getContext());
        rvCode.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST,
                UIUtils.dip2px(1), UIUtils.getColor(R.color.split_line)));
        rvCode.setLayoutManager(mLayoutManager);
        qrCodeList = new ArrayList<>();
        adapter = new QRCodeAdapter(qrCodeList);
        rvCode.setAdapter(adapter);
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
                if (!TextUtils.isEmpty(result)) {
                    if (!qrCodeList.contains(result)) {
                        qrCodeList.add(result);
                        adapter.notifyDataSetChanged();
                        tvCount.setText(qrCodeList.size() + "");
                    } else {
                        UIUtils.showToast("该码已扫过");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void doActivation(RequestBody requestBody) {
        HttpMethods.getInstance().doActivation(new ProgressSubscriber<List<ActivationInfo>>(this, true) {
            @Override
            public void onNext(List<ActivationInfo> registerCheckInfos) {
                super.onNext(registerCheckInfos);
                // 设置日期格式
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // new Date()为获取当前系统时间
                String commitTime = df.format(new Date());
                if (registerCheckInfos != null) {
                    List<ActivationInfo> successList = new ArrayList<>();
                    List<ActivationInfo> failList = new ArrayList<>();
                    for (ActivationInfo info : registerCheckInfos) {
                        if (info.getStatus() == 1) {
                            successList.add(info);
                        } else {
                            failList.add(info);
                        }
                    }
                    //删除缓存文件
                    FileUtils.deleteFoder(new File(ConstantUtil.FILE_DIR + ConstantUtil.ACTIVATION_CACHE + ".txt"));
                    qrCodeList.clear();
                    adapter.notifyDataSetChanged();
                    tvCount.setText("0");
                    showCommitSuccessDialog(commitTime, successList, failList);
                }
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                String message;
                if (e.getCode() == 401) {
                    message = "权限拒绝，请联系管理员";
                } else if (e.getCode() == 1009) {
                    message = "当前网络不佳，请前往网络较好的场所重新发起提交！";
                } else {
                    message = e.getCode() + ":" + e.getMessage();
                }
                if (adapter == null) {
                    adapter = new QRCodeAdapter(qrCodeList);
                    rvCode.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                showCommitFailDialog(message);
            }
        }, authorization, requestBody, ConstantUtil.COMPANY_CODE, ConstantUtil.TERMINALLID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (qrCodeList != null && qrCodeList.size() > 0) {
                    showCancelDialog();
                } else {
                    //删除缓存文件
                    FileUtils.deleteFoder(new File(ConstantUtil.FILE_DIR + ConstantUtil.ACTIVATION_CACHE + ".txt"));
                    finish();
                }
                break;

            case R.id.iv_reset:
                if (qrCodeList != null && qrCodeList.size() > 0) {
                    showClearConfirmDialog();
                }
                break;

            case R.id.iv_cancel:
                if (qrCodeList != null && qrCodeList.size() > 0) {
                    showCancelDialog();
                } else {
                    // 删除缓存文件
                    FileUtils.deleteFoder(new File(ConstantUtil.FILE_DIR + ConstantUtil.ACTIVATION_CACHE + ".txt"));
                    finish();
                }
                break;

            case R.id.iv_submit:
                int timeInterval = TimeUtil.getTimeInterval(TimeUtil.getCurretTime(), tokenUpdateTime);
                if (timeInterval > 2) {
                    updateToken();
                } else {
                    initActivationParam(qrCodeList);
                }
                break;
            default:
                break;
        }
    }

    private void initActivationParam(List<String> list) {
        if (list == null || list.size() == 0) {
            UIUtils.showToast("请扫描二维码");
        } else {
            String jsonStr = Utils.toJson(list, 1);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonStr);
            doActivation(requestBody);
        }
    }

    private void showCommitSuccessDialog(String commitTime, List<ActivationInfo> successList, final List<ActivationInfo> failList) {
        View view = LayoutInflater.from(ActiveActivity.this).inflate(R.layout.dialog_activation_success, null);
        TextView tvCommitTime = view.findViewById(R.id.tv_commit_time);
        TextView tvSuccessNum = view.findViewById(R.id.tv_success_num);
        TextView tvFailNum = view.findViewById(R.id.tv_fail_num);
        TextView tvFailDetail = view.findViewById(R.id.tv_fail_detail);
        TextView tvSubmit = view.findViewById(R.id.tv_submit_num);
        ImageView ivExit = view.findViewById(R.id.iv_cancel);

        tvCommitTime.setText(commitTime);
        tvSuccessNum.setText(successList.size() + "");
        tvFailNum.setText(failList.size() + "");
        tvFailDetail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        int count = successList.size() + failList.size();
        tvSubmit.setText(count + "");

        if (failList.size() == 0) {
            tvFailDetail.setVisibility(View.INVISIBLE);
        }
        final AlertDialog dialog = new AlertDialog.Builder(ActiveActivity.this).create();
        dialog.setCancelable(false);
        dialog.setView(view);
        dialog.show();

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvFailDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (failList.size() == 0) {
                    UIUtils.showToast("没有失败记录");
                } else {
                    Intent intent = new Intent(ActiveActivity.this, ActivationFailActivity.class);
                    intent.putParcelableArrayListExtra("failList", (ArrayList<? extends Parcelable>) failList);
                    startActivityForResult(intent, 111);
                }
            }
        });
    }

    private void showCommitFailDialog(String message) {
        View view = LayoutInflater.from(ActiveActivity.this).inflate(R.layout.dialog_register_fail, null);
        ImageView ivExit = view.findViewById(R.id.iv_cancel);
        TextView tvMessage = view.findViewById(R.id.tv1);
        tvMessage.setText(message);
        final AlertDialog dialog = new AlertDialog.Builder(ActiveActivity.this).create();
        dialog.setCancelable(false);
        dialog.setView(view);
        dialog.show();

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //  String jsonStr = Utils.toJson(qrCodeList, 1);
                //  FileUtils.writeFile(ConstantUtil.ACTIVATION_CACHE,jsonStr);
            }
        });
    }

    private void showCacheDialog(final List<String> infos) {
        View view = LayoutInflater.from(ActiveActivity.this).inflate(R.layout.dialog_cachel, null);
        ImageView ivIs = view.findViewById(R.id.iv_is);
        ImageView ivNo = view.findViewById(R.id.iv_no);
        final AlertDialog dialog = new AlertDialog.Builder(ActiveActivity.this).create();
        dialog.setCancelable(false);
        dialog.setView(view);
        dialog.show();

        ivIs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                initActivationParam(infos);
            }
        });

        ivNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (adapter == null) {
                    adapter = new QRCodeAdapter(qrCodeList);
                    rvCode.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void showCancelDialog() {
        NormalAlertDialog dialog2 = new NormalAlertDialog.Builder(ActiveActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("温馨提示")
                .setTitleTextColor(R.color.black_light)
                .setContentText("是否保存数据？")
                .setContentTextColor(R.color.black_light)
                .setLeftButtonText("不保存")
                .setLeftButtonTextColor(R.color.gray)
                .setRightButtonText("保存")
                .setRightButtonTextColor(R.color.black_light)
                .setOnclickListener(new DialogInterface.OnLeftAndRightClickListener<NormalAlertDialog>() {
                    @Override
                    public void clickLeftButton(NormalAlertDialog dialog, View view) {
                        dialog.dismiss();
                        finish();
                    }

                    @Override
                    public void clickRightButton(NormalAlertDialog dialog, View view) {
                        dialog.dismiss();
                        String jsonStr = Utils.toJson(qrCodeList, 1);
                        FileUtils.writeFile(ConstantUtil.ACTIVATION_CACHE, jsonStr);
                        finish();
                    }
                }).build();
        dialog2.show();
    }

    private void showClearConfirmDialog() {
        NormalAlertDialog dialog2 = new NormalAlertDialog.Builder(ActiveActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("清空列表")
                .setTitleTextColor(R.color.black_light)
                .setContentText("是否确认清空？")
                .setContentTextColor(R.color.black_light)
                .setLeftButtonText("是")
                .setLeftButtonTextColor(R.color.gray)
                .setRightButtonText("否")
                .setRightButtonTextColor(R.color.black_light)
                .setOnclickListener(new DialogInterface.OnLeftAndRightClickListener<NormalAlertDialog>() {
                    @Override
                    public void clickLeftButton(NormalAlertDialog dialog, View view) {
                        dialog.dismiss();
                        qrCodeList.clear();
                        adapter.notifyDataSetChanged();
                        tvCount.setText(qrCodeList.size() + "");
                    }

                    @Override
                    public void clickRightButton(NormalAlertDialog dialog, View view) {
                        dialog.dismiss();
                    }
                }).build();
        dialog2.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (qrCodeList != null && qrCodeList.size() > 0) {
                showCancelDialog();
            } else {
                FileUtils.deleteFoder(new File(ConstantUtil.FILE_DIR + ConstantUtil.ACTIVATION_CACHE + ".txt"));
                finish();
            }
            // 事件继续向下传播
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 退出当前界面
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            finish();
        }
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
                            //token更新的时间
                            .putString(ConstantUtil.SP_TOKEN_UPDATE_TIME, TimeUtil.getCurretTime())
                            .apply();
                    authorization = loginInfo.getTokenType() + " " + loginInfo.getAccessToken();
                    initActivationParam(qrCodeList);
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
