package com.ty.nanfangscanner.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.adapter.ProductListAdapter;
import com.ty.nanfangscanner.bean.BrandInfo;
import com.ty.nanfangscanner.bean.LoginInfo;
import com.ty.nanfangscanner.bean.PostLoginJson;
import com.ty.nanfangscanner.bean.ProductBrandInfo;
import com.ty.nanfangscanner.bean.ProductInfo;
import com.ty.nanfangscanner.constant.ConstantUtil;
import com.ty.nanfangscanner.net.HttpMethods;
import com.ty.nanfangscanner.net.ProgressSubscriber;
import com.ty.nanfangscanner.utils.TimeUtil;
import com.ty.nanfangscanner.utils.UIUtils;
import com.ty.nanfangscanner.utils.Utils;
import com.ty.nanfangscanner.view.DividerItemDecoration;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SynchronizationActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.rv_produce)
    RecyclerView rvProduce;
    @BindView(R.id.tv_null)
    TextView tvNull;
    @BindView(R.id.ll_more)
    RelativeLayout llMore;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.pb_load)
    ProgressBar pbLoad;

    private HashMap<Integer, String> brandMap = new HashMap<>();//存放品牌信息：k-id,v-name
    private List<ProductBrandInfo> productBrandInfoList = new ArrayList<>();
    private String authorization;
    private ProductListAdapter adapter;
    private SharedPreferences sharedPreferences;
    private String userName;
    private String password;
    private String tokenUpdateTime;
    private SharedPreferences mSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        doFilter();
    }

    private void initData() {
        mSp = getSharedPreferences(ConstantUtil.USER_SP_NAME, MODE_PRIVATE);
        String mTokenStr = mSp.getString(ConstantUtil.SP_TOKEN, "");
        String mTokenType = mSp.getString(ConstantUtil.SP_TOKEN_TYPE, "");
        userName = mSp.getString(ConstantUtil.SP_USER_NAME, "");
        password = mSp.getString(ConstantUtil.SP_USER_PASSWORD, "");
        tokenUpdateTime = mSp.getString(ConstantUtil.SP_TOKEN_UPDATE_TIME, "");
        authorization = mTokenType + " " + mTokenStr;

        sharedPreferences = getSharedPreferences(ConstantUtil.PRODUCT_SP_FILE, MODE_PRIVATE);
        String productJsonString = sharedPreferences.getString(ConstantUtil.SP_PRODUCT, "");
        Gson gson = new Gson();
        if (!TextUtils.isEmpty(productJsonString)) {
            List<ProductBrandInfo> infos = gson.fromJson(productJsonString,
                    new TypeToken<List<ProductBrandInfo>>() {
                    }.getType());
            if (infos != null) {
                productBrandInfoList.addAll(infos);
            }
        }
    }

    private void initView() {
        setContentView(R.layout.activity_synchronization);
        ButterKnife.bind(this);
        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        llMore.setOnClickListener(this);
        tvTitle.setText("同步");
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(UIUtils.getContext());
        rvProduce.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST,
                UIUtils.dip2px(1), UIUtils.getColor(R.color.split_line)));
        rvProduce.setLayoutManager(mLayoutManager);
        adapter = new ProductListAdapter(UIUtils.getContext(), productBrandInfoList);
        rvProduce.setAdapter(adapter);
        if (productBrandInfoList.size() == 0) {
            tvNull.setVisibility(View.VISIBLE);
        }
        tvCount.setText("共"+productBrandInfoList.size()+"条数据");
    }

    private void getBrandList() {
        HttpMethods.getInstance().getBrands(new ProgressSubscriber<List<BrandInfo>>(this, true) {
            @Override
            public void onNext(List<BrandInfo> brandInfos) {
                super.onNext(brandInfos);
                if (brandInfos != null) {
                    for (BrandInfo info : brandInfos) {
                        brandMap.put(info.getId(), info.getName());
                    }
                }
                getProductList();
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
        }, authorization, ConstantUtil.COMPANY_CODE, ConstantUtil.TERMINALLID);
    }

    private void getProductList() {
        etSearch.getText().clear();
        HttpMethods.getInstance().getProducts(new ProgressSubscriber<List<ProductInfo>>(this, true) {
            @Override
            public void onNext(List<ProductInfo> productInfos) {
                super.onNext(productInfos);
                if (productInfos != null) {
                    productBrandInfoList.clear();
                    for (ProductInfo info : productInfos) {
                        ProductBrandInfo productBrandInfo = new ProductBrandInfo();
                        productBrandInfo.setProductName(info.getName());
                        productBrandInfo.setBrandName(brandMap.get(info.getBrandId()));
                        productBrandInfo.setBrandId(info.getBrandId());
                        productBrandInfo.setProductId(info.getId());
                        productBrandInfoList.add(productBrandInfo);
                    }
                }
                if (productBrandInfoList.size() == 0) {
                    tvNull.setVisibility(View.VISIBLE);
                } else {
                    tvNull.setVisibility(View.GONE);
                    tvCount.setText("共" + productBrandInfoList.size() + "条数据");
                }
                adapter.notifyDataSetChanged();
                doCache();
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
        }, authorization, ConstantUtil.COMPANY_CODE, ConstantUtil.TERMINALLID);
    }

    private void doCache() {
        pbLoad.setVisibility(View.VISIBLE);
        String productJsonString = Utils.toJson(productBrandInfoList, 1);
        String brandJsonString = Utils.toJson(brandMap, 1);
        sharedPreferences.edit().putString(ConstantUtil.SP_PRODUCT, productJsonString)
                .putString(ConstantUtil.SP_BRAND, brandJsonString)
                .apply();
        pbLoad.setVisibility(View.GONE);
        UIUtils.showToast("同步完成");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.ll_more:
                int timeInterval = TimeUtil.getTimeInterval(TimeUtil.getCurretTime(), tokenUpdateTime);
                if (timeInterval>2){
                    updateToken();
                }else {
                    getBrandList();
                }
                break;
        }
    }

    private void doFilter() {
        etSearch.getText().clear();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (adapter != null) {
                    adapter.getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
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
                    getBrandList();
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
