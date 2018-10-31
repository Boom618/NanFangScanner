package com.ty.nanfangscanner.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.bean.ProductBrandInfo;
import com.ty.nanfangscanner.constant.ConstantUtil;
import com.ty.nanfangscanner.utils.UIUtils;
import com.ty.nanfangscanner.utils.Utils;
import com.ty.nanfangscanner.view.MyTextView;
import com.wevey.selector.dialog.DialogInterface;
import com.wevey.selector.dialog.NormalSelectionDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectProductActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_brand)
    MyTextView tvBrand;
    @BindView(R.id.tv_product)
    MyTextView tvProduct;
    @BindView(R.id.btn_cancel)
    ImageView ivCancel;
    @BindView(R.id.btn_next)
    ImageView ivNext;

    private List<ProductBrandInfo> productBrandInfos;
    private HashMap<Integer, String> brandMap;
    private HashMap<String, String> productBrandMap;//key=产品名称，value=品牌名称
    private HashMap<String, Integer> productIdMap;//key=产品名称，value=产品Id
    private List<String> productList;
    private List<String> brandList;
    private List<String> productListCopy = new ArrayList<>();

    private String selectedProduct = "";//选中的产品
    private String selectedBrand = "";//选中的品牌
    private int selectedProductId = -1;//选中的产品的Id
    private int selectedBrandId = -1;//选中的品牌Id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantUtil.PRODUCT_SP_FILE, MODE_PRIVATE);
        String productJsonString = sharedPreferences.getString(ConstantUtil.SP_PRODUCT, "");
        String brandJsonString = sharedPreferences.getString(ConstantUtil.SP_BRAND, "");
        Gson gson = new Gson();
        if (!TextUtils.isEmpty(productJsonString)) {
            productBrandInfos = gson.fromJson(productJsonString, new TypeToken<List<ProductBrandInfo>>() {
            }.getType());
        }
        if (!TextUtils.isEmpty(brandJsonString)) {
            brandMap = gson.fromJson(brandJsonString, new TypeToken<HashMap<Integer, String>>() {
            }.getType());
        }


        if (productBrandInfos != null) {
            productList = new ArrayList<>();
            productBrandMap=new HashMap<>();
            productIdMap=new HashMap<>();
            for (ProductBrandInfo info : productBrandInfos) {
                productList.add(info.getProductName());
                productListCopy.clear();
                productListCopy.addAll(productList);
                productIdMap.put(info.getProductName(),info.getProductId());
                productBrandMap.put(info.getProductName(),info.getBrandName());
            }
        }
        brandList = new ArrayList<>();
        brandList.add("无品牌");
        if (brandMap != null) {
            for (Map.Entry<Integer, String> entry : brandMap.entrySet()) {
                brandList.add(entry.getValue());
            }
        }
    }

    private void initView() {
        setContentView(R.layout.activity_select_product);
        ButterKnife.bind(this);
        tvTitle.setText("选择产品");
        ivBack.setOnClickListener(this);
        tvBrand.setOnClickListener(this);
        tvProduct.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        ivNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_brand:
                if (brandList != null) {
                    selectBrand(brandList);
                }
                break;

            case R.id.tv_product:
                if (productList != null) {
                    selectProduct(productListCopy);
                }
                break;

            case R.id.btn_cancel:
                finish();
                break;

            case R.id.btn_next:
                toRegisterActivity();
                break;
        }
    }

    private void toRegisterActivity() {
        if (!TextUtils.isEmpty(selectedProduct)) {
            Intent intent = new Intent(SelectProductActivity.this, RegisterActivity.class);
            intent.putExtra("brand", selectedBrand);
            intent.putExtra("product", selectedProduct);
            intent.putExtra("brandId", selectedBrandId);
            intent.putExtra("productId", selectedProductId);
            intent.putStringArrayListExtra("productList", (ArrayList<String>) productList);
            intent.putStringArrayListExtra("brandList", (ArrayList<String>) brandList);
            intent.putExtra("brandMap",brandMap);
            intent.putExtra("productBrandMap",productBrandMap);
            intent.putExtra("productIdMap",productIdMap);
            intent.putParcelableArrayListExtra("productBrandInfos", (ArrayList<? extends Parcelable>) productBrandInfos);
            startActivity(intent);
        } else {
            UIUtils.showToast("请选择产品/品牌");
        }
    }

    public void selectProduct(final List<String> datas) {
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
                        if (!selectedProduct.equals(datas.get(position))){
                            selectedProduct = datas.get(position);
                            selectedProductId=productIdMap.get(selectedProduct);//重置产品Id
                            tvProduct.setText(selectedProduct);

                            String brandName = productBrandMap.get(selectedProduct);
                            if (TextUtils.isEmpty(brandName)) {
                                selectedBrand = "无品牌";
                                selectedBrandId=-1;//重置品牌id
                            } else {
                                selectedBrand = brandName;
                                selectedBrandId=Utils.getKey(brandMap,selectedBrand);//重置品牌id
                            }
                            tvBrand.setText(selectedBrand);
                            //筛选该品牌的产品
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

    public void selectBrand(final List<String> datas) {
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
                        if (!selectedBrand.equals(datas.get(position))) {
                            tvProduct.setText("");
                            selectedProduct="";
                            selectedProductId=-1;//重置选中产品为-1
                            selectedBrand = datas.get(position);
                            tvBrand.setText(selectedBrand);
                            //筛选品牌
                            if (productBrandInfos != null) {
                                productListCopy.clear();
                                if (selectedBrand.equals("无品牌")) {//没有品牌信息
                                    selectedBrandId=-1;//重置品牌Id
                                    productListCopy.addAll(productList);
                                } else {
                                    selectedBrandId= Utils.getKey(brandMap,selectedBrand);//重置品牌Id
                                    for (ProductBrandInfo info : productBrandInfos) {
                                        if (selectedBrand.equals(info.getBrandName())) {
                                            productListCopy.add(info.getProductName());
                                        }
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
}
