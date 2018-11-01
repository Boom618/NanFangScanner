package com.ty.nanfangscanner.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.adapter.QRCodeAdapter;
import com.ty.nanfangscanner.bean.LoginInfo;
import com.ty.nanfangscanner.bean.PostLoginJson;
import com.ty.nanfangscanner.bean.PostRegisterCheckInfo;
import com.ty.nanfangscanner.bean.RegisterCheckInfo;
import com.ty.nanfangscanner.bean.SectionNumber;
import com.ty.nanfangscanner.constant.ConstantUtil;
import com.ty.nanfangscanner.net.HttpMethods;
import com.ty.nanfangscanner.net.ProgressSubscriber;
import com.ty.nanfangscanner.utils.TimeUtil;
import com.ty.nanfangscanner.utils.UIUtils;
import com.ty.nanfangscanner.utils.Utils;
import com.ty.nanfangscanner.view.DividerItemDecoration;
import com.wevey.selector.dialog.DialogInterface;
import com.wevey.selector.dialog.NormalAlertDialog;
import com.zhouyou.http.exception.ApiException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author TY
 */
public class EndQrCodeActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_brand)
    TextView tvBrand;
    @BindView(R.id.tv_product)
    TextView tvProduct;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.iv_reset)
    TextView tvReset;
    @BindView(R.id.rv_code)
    RecyclerView rvCode;
    @BindView(R.id.iv_check)
    ImageView ivCheck;
    @BindView(R.id.iv_cancel)
    ImageView ivCancel;
    @BindView(R.id.iv_save)
    ImageView ivSave;
    @BindView(R.id.iv_commit)
    ImageView ivCommit;

    private int mSelectedBrandId;
    private int mSelectedProductId;
    private String mSelectedBrand;
    private String mSelectedProduct;
    private QRCodeAdapter adapter;
    private List<String> endCodeList;//结束码段
    private List<String> startCodeList;//起始码段
    private String authorization;

    private static final int REQUEST_CODE = 111;
    private static final int RESULT_CODE = 222;

    private static final int CHECK = 1;
    private static final int REGISTER = 2;

    private SharedPreferences mSp;
    private String userName;
    private String password;
    private String tokenUpdateTime;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHECK:
                    doRegisterCheck(doCheck());
                    break;
                case REGISTER:
                    doRegister(doCheck());
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
//        Intent intent = getIntent();
//        mSelectedBrand = intent.getStringExtra("brand");
//        mSelectedProduct = intent.getStringExtra("product");
//        mSelectedBrandId = intent.getIntExtra("brandId", -1);
//        mSelectedProductId = intent.getIntExtra("productId", -1);
//        endCodeList = intent.getStringArrayListExtra("endCodeList");
        Bundle bundle = getIntent().getExtras();
        mSelectedBrand = bundle.getString("brand");
        mSelectedProduct = bundle.getString("product");
        mSelectedBrandId = bundle.getInt("brandId");
        mSelectedProductId = bundle.getInt("productId");
        endCodeList = bundle.getStringArrayList("endCodeList");

        mSp = getSharedPreferences(ConstantUtil.USER_SP_NAME, MODE_PRIVATE);
        String mTokenStr = mSp.getString(ConstantUtil.SP_TOKEN, "");
        String mTokenType = mSp.getString(ConstantUtil.SP_TOKEN_TYPE, "");
        userName = mSp.getString(ConstantUtil.SP_USER_NAME, "");
        password = mSp.getString(ConstantUtil.SP_USER_PASSWORD, "");
        tokenUpdateTime = mSp.getString(ConstantUtil.SP_TOKEN_UPDATE_TIME, "");
        authorization = mTokenType + " " + mTokenStr;
        startCodeList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_end_qr_code);
        ButterKnife.bind(this);
        tvTitle.setText("扫描起始码");
        ivBack.setOnClickListener(this);
        tvReset.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        ivSave.setOnClickListener(this);
        ivCommit.setOnClickListener(this);
        ivCheck.setOnClickListener(this);
        tvBrand.setText("品牌：" + mSelectedBrand);
        tvProduct.setText("产品：" + mSelectedProduct);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(UIUtils.getContext());
        rvCode.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST,
                UIUtils.dip2px(1), UIUtils.getColor(R.color.split_line)));
        rvCode.setLayoutManager(mLayoutManager);
        adapter = new QRCodeAdapter(startCodeList);
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
                    if (!startCodeList.contains(result)) {
                        if (startCodeList.size() < 20) {
                            startCodeList.add(result);
                            adapter.notifyDataSetChanged();
                            tvCount.setText(startCodeList.size() + "");
                        } else {
                            UIUtils.showToast("扫描数量已达最大值");
                        }
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

    private void doRegisterCheck(RequestBody requestBody) {
        HttpMethods.getInstance().doRegisterCheck(new ProgressSubscriber<List<RegisterCheckInfo>>(this, true) {
            @Override
            public void onNext(List<RegisterCheckInfo> registerCheckInfos) {
                super.onNext(registerCheckInfos);
                if (registerCheckInfos != null) {
                    boolean isValid = true;//是否所有的码段都合法
                    for (RegisterCheckInfo info : registerCheckInfos) {
                        if (info.getStatus() == 0) {
                            Toast.makeText(UIUtils.getContext(), info.getResultMsg(), Toast.LENGTH_LONG).show();
                            isValid = false;
                            return;
                        }
                    }
                    if (isValid) {
                        Toast.makeText(UIUtils.getContext(), "该号段组合法,可以提交", Toast.LENGTH_LONG).show();
                    }
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
        }, authorization, requestBody, ConstantUtil.COMPANY_CODE, ConstantUtil.TERMINALLID);
    }

    private void doRegister(RequestBody requestBody) {
        HttpMethods.getInstance().doRegister(new ProgressSubscriber<List<RegisterCheckInfo>>(this, true) {
            @Override
            public void onNext(List<RegisterCheckInfo> registerCheckInfos) {
                super.onNext(registerCheckInfos);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String commitTime = df.format(new Date());// new Date()为获取当前系统时间
                if (registerCheckInfos != null) {
                    List<RegisterCheckInfo> successList = new ArrayList<>();
                    List<RegisterCheckInfo> failList = new ArrayList<>();
                    for (RegisterCheckInfo info : registerCheckInfos) {
                        if (info.getStatus() == 1) {
                            successList.add(info);
                        } else {
                            failList.add(info);
                        }
                    }
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
                //UIUtils.showToast(message);
                showCommitFailDialog(message);
            }
        }, authorization, requestBody, ConstantUtil.COMPANY_CODE, ConstantUtil.TERMINALLID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (endCodeList != null && endCodeList.size() > 0) {
                    if (endCodeList.size() == startCodeList.size()) {
                        showCancelDialog();
                    } else {
                        UIUtils.showToast("起始码段和结束码段数量不一致");
                    }
                } else {
                    finish();
                }
                break;

            case R.id.iv_reset:
                if (startCodeList != null && startCodeList.size() > 0) {
                    showClearConfirmDialog();
                }
                break;

            case R.id.iv_cancel:
                if (endCodeList != null && endCodeList.size() > 0) {
                    if (endCodeList.size() == startCodeList.size()) {
                        showCancelDialog();
                    } else {
                        UIUtils.showToast("起始码段和结束码段数量不一致");
                    }
                } else {
                    finish();
                }
                break;

            case R.id.iv_check:
                if (endCodeList != null && endCodeList.size() == startCodeList.size()) {
                    int timeInterval = TimeUtil.getTimeInterval(TimeUtil.getCurretTime(), tokenUpdateTime);
                    if (timeInterval > 2) {
                        updateToken(CHECK);
                    } else {
                        doRegisterCheck(doCheck());
                    }
                } else {
                    UIUtils.showToast("起始码段和结束码段数量不一致");
                }
                break;

            case R.id.iv_commit:
                if (endCodeList != null && endCodeList.size() == startCodeList.size()) {
                    int timeInterval = TimeUtil.getTimeInterval(TimeUtil.getCurretTime(), tokenUpdateTime);
                    if (timeInterval > 2) {
                        updateToken(REGISTER);
                    } else {
                        doRegister(doCheck());
                    }
                } else {
                    UIUtils.showToast("起始码段和结束码段数量不一致");
                }
                break;

            case R.id.iv_save:
                if (endCodeList != null && endCodeList.size() == startCodeList.size()) {
                    doSave();
                } else {
                    UIUtils.showToast("起始码段和结束码段数量不一致");
                }
                break;
            default:
                break;

        }
    }

    private void doSave() {
        SectionNumber sectionNumber = new SectionNumber();
        sectionNumber.setBrandName(mSelectedBrand);
        sectionNumber.setBrandId(mSelectedBrandId);
        sectionNumber.setProductName(mSelectedProduct);
        sectionNumber.setProductId(mSelectedProductId);
        sectionNumber.setStartCodeList(startCodeList);
        sectionNumber.setEndCodeList(endCodeList);
        sectionNumber.setCount(startCodeList.size());
        //设置日期格式 "yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat df = new SimpleDateFormat(Utils.DATE_SIMPLE_H_M_S);
        // new Date()为获取当前系统时间
        String saveTime = df.format(new Date());
        sectionNumber.setSaveTime(saveTime);

        Intent intent = new Intent(EndQrCodeActivity.this, RegisterActivity.class);
        intent.putExtra("sectionNumber", sectionNumber);
        startActivity(intent);
        finish();
    }

    private RequestBody doCheck() {
        RequestBody requestBody = null;
        List<PostRegisterCheckInfo> infos = new ArrayList<>();
        for (int i = 0; i < startCodeList.size(); i++) {
            PostRegisterCheckInfo info = new PostRegisterCheckInfo();
            info.setBrandId(mSelectedBrandId);
            info.setProductId(mSelectedProductId);
            info.setCodeUrlStart(startCodeList.get(i));
            info.setCodeUrlEnd(endCodeList.get(i));
            infos.add(info);
        }
        String jsonStr = Utils.toJson(infos, 1);
        requestBody = RequestBody.create(MediaType.parse("application/json"), jsonStr);

        return requestBody;
    }

    private void showCommitSuccessDialog(String commitTime, List<RegisterCheckInfo> successList, final List<RegisterCheckInfo> failList) {
        View view = LayoutInflater.from(EndQrCodeActivity.this).inflate(R.layout.dialog_register_success, null);
        TextView tvCommitTime = view.findViewById(R.id.tv_commit_time);
        TextView tvSuccessNum = view.findViewById(R.id.tv_success_num);
        TextView tvFailNum = view.findViewById(R.id.tv_fail_num);
        TextView tvProductName = view.findViewById(R.id.tv_product_name);
        TextView tvProductNum = view.findViewById(R.id.tv_product_num);
        TextView tvFailDetail = view.findViewById(R.id.tv_fail_detail);
        ImageView ivExit = view.findViewById(R.id.iv_cancel);
        tvCommitTime.setText(commitTime);
        tvSuccessNum.setText(successList.size() + "");
        tvFailNum.setText(failList.size() + "");
        tvProductName.setText(mSelectedProduct + "：");
        tvFailDetail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        int count = successList.size() + failList.size();
        tvProductNum.setText(count + "");

        if (failList.size() == 0) {
            tvFailDetail.setVisibility(View.INVISIBLE);
        }
        final AlertDialog dialog = new AlertDialog.Builder(EndQrCodeActivity.this).create();
        dialog.setCancelable(false);
        dialog.setView(view);
        dialog.show();

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(EndQrCodeActivity.this, SelectProductActivity.class));
            }
        });

        tvFailDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (failList.size() == 0) {
                    UIUtils.showToast("没有失败记录");
                } else {
                    Intent intent = new Intent(EndQrCodeActivity.this, CommitFailActivity.class);
                    intent.putParcelableArrayListExtra("failList", (ArrayList<? extends Parcelable>) failList);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });
    }

    private void showCommitFailDialog(String message) {
        View view = LayoutInflater.from(EndQrCodeActivity.this).inflate(R.layout.dialog_register_fail, null);
        ImageView ivExit = view.findViewById(R.id.iv_cancel);
        TextView tvMessage = view.findViewById(R.id.tv1);
        tvMessage.setText(message);
        final AlertDialog dialog = new AlertDialog.Builder(EndQrCodeActivity.this).create();
        dialog.setCancelable(false);
        dialog.setView(view);
        dialog.show();

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                UIUtils.showToast("请点击保存按钮，保存到号段列表");
            }
        });
    }

    private void showClearConfirmDialog() {
        NormalAlertDialog dialog2 = new NormalAlertDialog.Builder(EndQrCodeActivity.this)
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
                        startCodeList.clear();
                        adapter.notifyDataSetChanged();
                        tvCount.setText(startCodeList.size() + "");
                    }

                    @Override
                    public void clickRightButton(NormalAlertDialog dialog, View view) {
                        dialog.dismiss();
                    }
                }).build();
        dialog2.show();
    }

    private void showCancelDialog() {
        NormalAlertDialog dialog2 = new NormalAlertDialog.Builder(EndQrCodeActivity.this)
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
                        doSave();
                    }
                }).build();
        dialog2.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (endCodeList != null) {
                if (endCodeList.size() == startCodeList.size()) {
                    showCancelDialog();
                } else {
                    UIUtils.showToast("起始码段和结束码段数量不一致");
                }
            } else {
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

    private void updateToken(final int code) {
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

                    //发送消息提示token刷新完成
                    Message message = Message.obtain();
                    message.what = code;
                    mHandler.sendMessage(message);

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
