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
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.adapter.DialogProductAdapter;
import com.ty.nanfangscanner.adapter.SectionNumberAdapter;
import com.ty.nanfangscanner.bean.LoginInfo;
import com.ty.nanfangscanner.bean.PostLoginJson;
import com.ty.nanfangscanner.bean.PostRegisterCheckInfo;
import com.ty.nanfangscanner.bean.ProductBrandInfo;
import com.ty.nanfangscanner.bean.RegisterCheckInfo;
import com.ty.nanfangscanner.bean.SectionNumber;
import com.ty.nanfangscanner.constant.ConstantUtil;
import com.ty.nanfangscanner.net.HttpMethods;
import com.ty.nanfangscanner.net.ProgressSubscriber;
import com.ty.nanfangscanner.utils.FileUtils;
import com.ty.nanfangscanner.utils.TimeUtil;
import com.ty.nanfangscanner.utils.UIUtils;
import com.ty.nanfangscanner.utils.Utils;
import com.ty.nanfangscanner.view.DividerItemDecoration;
import com.ty.nanfangscanner.view.MyTextView;
import com.wevey.selector.dialog.DialogInterface;
import com.wevey.selector.dialog.NormalAlertDialog;
import com.wevey.selector.dialog.NormalSelectionDialog;
import com.zhouyou.http.exception.ApiException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_brand)
    TextView tvBrand;
    @BindView(R.id.tv_product)
    TextView tvProduct;
    @BindView(R.id.iv_update)
    ImageView ivUpdate;
    @BindView(R.id.iv_start)
    ImageView ivStart;
    @BindView(R.id.rv_produce)
    RecyclerView rvProduce;
    @BindView(R.id.iv_cancel)
    ImageView ivCancel;
    @BindView(R.id.iv_commit)
    ImageView ivCommit;

    private String mSelectedBrand;
    private String mSelectedProduct;
    private int mSelectedBrandId;
    private int mSelectedProductId;
    private List<ProductBrandInfo> productBrandInfos;
    private List<String> productList;
    private List<String> brandList;
    private List<String> productListCopy = new ArrayList<>();
    private HashMap<Integer, String> brandMap;
    private List<SectionNumber> sectionNumberList = new ArrayList<>();
    private SectionNumberAdapter adapter;
    private String authorization;

    private static final int REQUEST_CODE = 111;
    private static final int RESULT_CODE = 222;
    private HashMap<String, String> productBrandMap;
    private AlertDialog commitSuccessDialog;
    private SharedPreferences mSp;
    private String userName;
    private String password;
    private String tokenUpdateTime;
    private HashMap<String, Integer> productIdMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        mSelectedBrand = intent.getStringExtra("brand");
        mSelectedProduct = intent.getStringExtra("product");
        mSelectedBrandId = intent.getIntExtra("brandId", -1);
        mSelectedProductId = intent.getIntExtra("productId", -1);
        productList = intent.getStringArrayListExtra("productList");
        brandList = intent.getStringArrayListExtra("brandList");
        productBrandInfos = intent.getParcelableArrayListExtra("productBrandInfos");
        brandMap = (HashMap<Integer, String>) intent.getSerializableExtra("brandMap");
        productBrandMap = (HashMap<String, String>) intent.getSerializableExtra("productBrandMap");
        productIdMap = (HashMap<String, Integer>) intent.getSerializableExtra("productIdMap");
        mSp = getSharedPreferences(ConstantUtil.USER_SP_NAME, MODE_PRIVATE);
        String mTokenStr = mSp.getString(ConstantUtil.SP_TOKEN, "");
        String mTokenType = mSp.getString(ConstantUtil.SP_TOKEN_TYPE, "");
        userName = mSp.getString(ConstantUtil.SP_USER_NAME, "");
        password = mSp.getString(ConstantUtil.SP_USER_PASSWORD, "");
        tokenUpdateTime = mSp.getString(ConstantUtil.SP_TOKEN_UPDATE_TIME, "");
        authorization = mTokenType + " " + mTokenStr;

        if (productBrandInfos != null) {
            productListCopy.clear();
            if (mSelectedBrand.equals("无品牌")) {//没有品牌信息
                productListCopy.addAll(productList);
            } else {
                for (ProductBrandInfo info : productBrandInfos) {
                    if (mSelectedBrand.equals(info.getBrandName())) {
                        productListCopy.add(info.getProductName());
                    }
                }
            }
        }

        // productListCopy.addAll(productList);
        String jsonStr = FileUtils.readFile(ConstantUtil.REGISTER_CACHE);
        if (!TextUtils.isEmpty(jsonStr) && !"[]".equals(jsonStr)) {
            Gson gson = new Gson();
            List<SectionNumber> infos = gson.fromJson(jsonStr, new TypeToken<List<SectionNumber>>() {
            }.getType());
            if (infos != null) {
                showCacheDialog(infos);
            }
        }

        Log.e("TAG",mSelectedProduct+"==11111=="+mSelectedProductId);
    }

    private void initView() {
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        tvTitle.setText("号段组列表");
        ivBack.setOnClickListener(this);
        ivUpdate.setOnClickListener(this);
        ivStart.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        ivCommit.setOnClickListener(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(UIUtils.getContext());
        rvProduce.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST,
                UIUtils.dip2px(1), UIUtils.getColor(R.color.split_line)));
        rvProduce.setLayoutManager(mLayoutManager);
        tvBrand.setText("品牌：" + mSelectedBrand);
        tvProduct.setText("产品：" + mSelectedProduct);

        adapter = new SectionNumberAdapter(sectionNumberList);
        adapter.setOnDeleteClickListener(new SectionNumberAdapter.OnDeleteClickListener() {
            @Override
            public void onDelete(int position) {
                showDeleteDialog(position);
            }
        });
        rvProduce.setAdapter(adapter);
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
                    Map<String, Integer> productMap = new HashMap<>();//存放key:产品名称,value:产品数量
                    List<String> productNameList = new ArrayList<>();
                    for (RegisterCheckInfo info : registerCheckInfos) {
                        String productName = info.getProductName();
                        if (info.getStatus() == 1) {
                            successList.add(info);
                        } else {
                            failList.add(info);
                        }
                        ////初始化productMap
                        if (productNameList.contains(productName)) {
                            int newNum = productMap.get(productName) + 1;
                            productMap.put(productName, newNum);
                        } else {
                            productNameList.add(productName);
                            productMap.put(productName, 1);
                        }
                    }
                    sectionNumberList.clear();
                    adapter.notifyDataSetChanged();
                    FileUtils.deleteFoder(new File(ConstantUtil.FILE_DIR + ConstantUtil.REGISTER_CACHE + ".txt"));//删除缓存文件
                    showCommitSuccessDialog(commitTime, successList, failList, productMap);
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
                // UIUtils.showToast(message);
                showCommitFailDialog(message);
            }
        }, authorization, requestBody, ConstantUtil.COMPANY_CODE, ConstantUtil.TERMINALLID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (sectionNumberList.size() > 0) {
                    showCancelDialog();
                } else {
                    FileUtils.deleteFoder(new File(ConstantUtil.FILE_DIR + ConstantUtil.REGISTER_CACHE + ".txt"));//删除缓存文件
                    finish();
                }
                break;

            case R.id.iv_update:
                showUpdateDialog();
                break;

            case R.id.iv_start:
                toStartCodeActivity();
                break;

            case R.id.iv_cancel:
                if (sectionNumberList.size() > 0) {
                    showCancelDialog();
                } else {
                    FileUtils.deleteFoder(new File(ConstantUtil.FILE_DIR + ConstantUtil.REGISTER_CACHE + ".txt"));//删除缓存文件
                    finish();
                }
                break;

            case R.id.iv_commit:
                int timeInterval = TimeUtil.getTimeInterval(TimeUtil.getCurretTime(), tokenUpdateTime);
                if (timeInterval > 2) {
                    updateToken();
                } else {
                    initRegisterParam(sectionNumberList);
                }
                break;
        }
    }

    private void initRegisterParam(List<SectionNumber> list) {
        if (list.size() > 0) {
            List<PostRegisterCheckInfo> infos = new ArrayList<>();
            for (SectionNumber sn : list) {
                int brandId = sn.getBrandId();
                int productId = sn.getProductId();
                List<String> startCodeList = sn.getStartCodeList();
                List<String> endCodeList = sn.getEndCodeList();
                for (int i = 0; i < startCodeList.size(); i++) {
                    PostRegisterCheckInfo info = new PostRegisterCheckInfo();
                    info.setBrandId(brandId);
                    info.setProductId(productId);
                    info.setCodeUrlStart(startCodeList.get(i));
                    info.setCodeUrlEnd(endCodeList.get(i));
                    infos.add(info);
                }
            }
            String jsonStr = Utils.toJson(infos, 1);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonStr);
            doRegister(requestBody);
        }
    }

    private void toStartCodeActivity() {
        if (!TextUtils.isEmpty(mSelectedProduct)) {
            Intent intent = new Intent(RegisterActivity.this, StartQrCodeActivity.class);
            intent.putExtra("brand", mSelectedBrand);
            intent.putExtra("product", mSelectedProduct);
            intent.putExtra("brandId", mSelectedBrandId);
            intent.putExtra("productId", mSelectedProductId);
            startActivity(intent);
        } else {
            UIUtils.showToast("请选择产品/品牌");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SectionNumber sectionNumber = intent.getParcelableExtra("sectionNumber");
        if (sectionNumber != null) {
            sectionNumberList.add(sectionNumber);
            adapter.notifyDataSetChanged();
        }
    }

    private void showUpdateDialog() {
        View view = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.dialog_my_choose, null);
        final MyTextView mTvBrand = view.findViewById(R.id.tv_brand);
        final MyTextView mTvProduct = view.findViewById(R.id.tv_product);
        ImageView mIvCancel = view.findViewById(R.id.iv_cancel);
        ImageView mIvSave = view.findViewById(R.id.iv_save);

        mTvProduct.setText(mSelectedProduct);//默认项
        mTvBrand.setText(mSelectedBrand);//默认项

        final AlertDialog dialog = new AlertDialog.Builder(RegisterActivity.this).create();
        dialog.setCancelable(false);
        dialog.setView(view);
        dialog.show();
        mIvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //筛选该品牌的产品
                productListCopy.clear();
                if (mSelectedBrand.equals("无品牌")) {//没有品牌信息
                    productListCopy.addAll(productList);
                } else {
                    for (ProductBrandInfo info : productBrandInfos) {
                        if (mSelectedBrand.equals(info.getBrandName())) {
                            productListCopy.add(info.getProductName());
                        }
                    }
                }
            }
        });

        mTvBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (brandList != null) {
                    selectBrand(brandList, mTvBrand, mTvProduct);
                }
            }
        });
        mTvProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productList != null) {
                    selectProduct(productListCopy, mTvProduct, mTvBrand);
                }
            }
        });

        mIvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mTvProduct.getText().toString())) {
                    dialog.dismiss();
                    mSelectedBrand = mTvBrand.getText().toString();
                    mSelectedProduct = mTvProduct.getText().toString();
                    mSelectedProductId=productIdMap.get(mSelectedProduct);
                    mSelectedBrandId = Utils.getKey(brandMap, mSelectedBrand);
                    tvBrand.setText("品牌：" + mSelectedBrand);
                    tvProduct.setText("产品：" + mSelectedProduct);
                } else {
                    UIUtils.showToast("请选择产品/品牌");
                }
            }
        });
    }

    public void selectProduct(final List<String> datas, final TextView textViewProduct,
                              final TextView textViewBrand) {
        NormalSelectionDialog.Builder builder = new NormalSelectionDialog.Builder(this);
        builder.setlTitleVisible(true)   //设置是否显示标题
                .setTitleHeight(50)   //设置标题高度
                .setTitleText("选择产品")  //设置标题提示文本
                .setTitleTextSize(14) //设置标题字体大小 sp
                .setTitleTextColor(R.color.main_color) //设置标题文本颜色
                .setItemHeight(40)  //设置item的高度
                .setItemWidth(0.9f)  //屏幕宽度*0.9
                .setItemTextColor(R.color.black)  //设置item字体颜色
                .setItemTextSize(14)  //设置item字体大小
                .setCancleButtonText("取消")  //设置最底部“取消”按钮文本
                .setOnItemListener(new DialogInterface.OnItemClickListener<NormalSelectionDialog>() {

                    @Override
                    public void onItemClick(NormalSelectionDialog dialog, View button, int
                            position) {
                        dialog.dismiss();
                        String selectedProduct = datas.get(position);
                        textViewProduct.setText(selectedProduct);

                        String brandName = productBrandMap.get(selectedProduct);
                        if (TextUtils.isEmpty(brandName)) {
                            brandName = "无品牌";
                        }
                        textViewBrand.setText(brandName);

                        //筛选该品牌的产品
                        productListCopy.clear();
                        if (brandName.equals("无品牌")) {//没有品牌信息
                            productListCopy.addAll(productList);
                        } else {
                            for (ProductBrandInfo info : productBrandInfos) {
                                if (brandName.equals(info.getBrandName())) {
                                    productListCopy.add(info.getProductName());
                                }
                            }
                        }
                    }
                })
                .setCanceledOnTouchOutside(true)  //设置是否可点击其他地方取消dialog
                .build()
                .setDatas(datas)
                .show();
    }

    public void selectBrand(final List<String> datas, final TextView textViewBrand,
                            final TextView textViewProduct) {
        NormalSelectionDialog.Builder builder = new NormalSelectionDialog.Builder(this);
        builder.setlTitleVisible(true)   //设置是否显示标题
                .setTitleHeight(50)   //设置标题高度
                .setTitleText("选择品牌")  //设置标题提示文本
                .setTitleTextSize(14) //设置标题字体大小 sp
                .setTitleTextColor(R.color.main_color) //设置标题文本颜色
                .setItemHeight(40)  //设置item的高度
                .setItemWidth(0.9f)  //屏幕宽度*0.9
                .setItemTextColor(R.color.black)  //设置item字体颜色
                .setItemTextSize(14)  //设置item字体大小
                .setCancleButtonText("取消")  //设置最底部“取消”按钮文本
                .setOnItemListener(new DialogInterface.OnItemClickListener<NormalSelectionDialog>() {

                    @Override
                    public void onItemClick(NormalSelectionDialog dialog, View button, int
                            position) {
                        dialog.dismiss();
                        String selectedBrand = datas.get(position);
                        textViewProduct.setText("");
                        textViewBrand.setText(selectedBrand);
                        //筛选品牌
                        if (productBrandInfos != null) {
                            productListCopy.clear();
                            if (selectedBrand.equals("无品牌")) {//没有品牌信息
                                productListCopy.addAll(productList);
                            } else {
                                for (ProductBrandInfo info : productBrandInfos) {
                                    if (selectedBrand.equals(info.getBrandName())) {
                                        productListCopy.add(info.getProductName());
                                    }
                                }
                            }
                        }
                    }
                })
                .setCanceledOnTouchOutside(true)  //设置是否可点击其他地方取消dialog
                .build()
                .setDatas(datas)
                .show();
    }

    private void showCommitSuccessDialog(String commitTime, List<RegisterCheckInfo> successList,
                                         final List<RegisterCheckInfo> failList, Map<String, Integer> productMap) {
        View view = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.dialog_register_success_count, null);
        TextView tvCommitTime = view.findViewById(R.id.tv_commit_time);
        TextView tvSuccessNum = view.findViewById(R.id.tv_success_num);
        TextView tvFailNum = view.findViewById(R.id.tv_fail_num);
        TextView tvFailDetail = view.findViewById(R.id.tv_fail_detail);
        ImageView ivExit = view.findViewById(R.id.iv_cancel);
        RecyclerView recyclerView = view.findViewById(R.id.rv_product);
        tvCommitTime.setText(commitTime);
        tvSuccessNum.setText(successList.size() + "");
        tvFailNum.setText(failList.size() + "");
        tvFailDetail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        List<Map.Entry<String, Integer>> list = new ArrayList<>(productMap.entrySet());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(UIUtils.getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST,
                UIUtils.dip2px(1), UIUtils.getColor(R.color.split_line)));
        recyclerView.setLayoutManager(mLayoutManager);
        DialogProductAdapter dialogProductAdapter = new DialogProductAdapter(list);
        recyclerView.setAdapter(dialogProductAdapter);
        if (failList.size() == 0) {
            tvFailDetail.setVisibility(View.INVISIBLE);
        }

        commitSuccessDialog = new AlertDialog.Builder(RegisterActivity.this).create();
        commitSuccessDialog.setCancelable(false);
        commitSuccessDialog.setView(view);
        commitSuccessDialog.show();

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitSuccessDialog.dismiss();
                startActivity(new Intent(RegisterActivity.this, SelectProductActivity.class));
            }
        });

        tvFailDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (failList.size() == 0) {
                    UIUtils.showToast("没有失败记录");
                } else {
                    Intent intent = new Intent(RegisterActivity.this, CommitFailActivity.class);
                    intent.putParcelableArrayListExtra("failList", (ArrayList<? extends Parcelable>) failList);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });
    }

    private void showCommitFailDialog(String message) {
        View view = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.dialog_register_fail, null);
        ImageView ivExit = view.findViewById(R.id.iv_cancel);
        TextView tvMessage = view.findViewById(R.id.tv1);
        tvMessage.setText(message);
        final AlertDialog dialog = new AlertDialog.Builder(RegisterActivity.this).create();
        dialog.setCancelable(false);
        dialog.setView(view);
        dialog.show();

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showCacheDialog(final List<SectionNumber> infos) {
        View view = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.dialog_cachel, null);
        ImageView ivIs = view.findViewById(R.id.iv_is);
        ImageView ivNo = view.findViewById(R.id.iv_no);
        final AlertDialog dialog = new AlertDialog.Builder(RegisterActivity.this).create();
        dialog.setCancelable(false);
        dialog.setView(view);
        dialog.show();

        ivIs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                initRegisterParam(infos);
            }
        });

        ivNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sectionNumberList.addAll(infos);
                if (adapter == null) {
                    adapter = new SectionNumberAdapter(sectionNumberList);
                    rvProduce.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void showCancelDialog() {
        NormalAlertDialog dialog2 = new NormalAlertDialog.Builder(RegisterActivity.this)
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
                        String jsonStr = Utils.toJson(sectionNumberList, 1);
                        FileUtils.writeFile(ConstantUtil.REGISTER_CACHE, jsonStr);
                        finish();
                    }
                }).build();
        dialog2.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (sectionNumberList.size() > 0) {
                showCancelDialog();
            } else {
                finish();
            }
            return false; // 事件继续向下传播
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {//退出当前界面
            if (commitSuccessDialog != null && commitSuccessDialog.isShowing()) {
                commitSuccessDialog.dismiss();
            }
        }
    }

    private void showDeleteDialog(final int position) {
        NormalAlertDialog dialog2 = new NormalAlertDialog.Builder(RegisterActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("温馨提示")
                .setTitleTextColor(R.color.black_light)
                .setContentText("是否删除数据？")
                .setContentTextColor(R.color.black_light)
                .setLeftButtonText("确认")
                .setLeftButtonTextColor(R.color.gray)
                .setRightButtonText("取消")
                .setRightButtonTextColor(R.color.black_light)
                .setOnclickListener(new DialogInterface.OnLeftAndRightClickListener<NormalAlertDialog>() {
                    @Override
                    public void clickLeftButton(NormalAlertDialog dialog, View view) {
                        dialog.dismiss();
                        if (adapter != null) {
                            adapter.deleteItem(position);
                        }
                    }

                    @Override
                    public void clickRightButton(NormalAlertDialog dialog, View view) {
                        dialog.dismiss();
                    }
                }).build();
        dialog2.show();
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
                    initRegisterParam(sectionNumberList);
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
