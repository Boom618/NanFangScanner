package com.ty.nanfangscanner.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.activity.base.BaseActivity;
import com.ty.nanfangscanner.bean.ProductBrandInfo;
import com.ty.nanfangscanner.constant.ConstantUtil;
import com.ty.nanfangscanner.utils.UIUtils;
import com.ty.nanfangscanner.utils.Utils;
import com.ty.nanfangscanner.view.MyTextView;
import com.ty.nanfangscanner.view.WidgetDialog;
import com.wevey.selector.dialog.DialogInterface;
import com.wevey.selector.dialog.NormalSelectionDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 选择产品
 *
 * @author TY
 */
public class SelectProductActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_brand)
    MyTextView tvBrand;
    @BindView(R.id.tv_product)
    MyTextView tvProduct;
    @BindView(R.id.button_cancel)
    ImageView btnCancel;
    @BindView(R.id.button_next)
    ImageView btnNext;

    private List<ProductBrandInfo> productBrandInfos;
    private HashMap<Integer, String> brandMap;
    /**
     * key=产品名称，value=品牌名称
     */
    private HashMap<String, String> productBrandMap;
    /**
     * key=产品名称，value=产品Id
     */
    private HashMap<String, Integer> productIdMap;

    private List<String> productList;
    private List<String> brandList;
    private List<String> productListCopy = new ArrayList<>();
    // 选中的产品
    private String selectedProduct = "";
    // 选中的品牌
    private String selectedBrand = "";
    // 选中的产品的Id
    private int selectedProductId = -1;
    // 选中的品牌Id
    private int selectedBrandId = -1;

    String titleName ;
    String cancelButton ;
    String nullProject ;


    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {

    }

    @Override
    protected int setActivityLayout() {
        return R.layout.activity_select_product;
    }

    @Override
    protected void initData() {
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
            productBrandMap = new HashMap<>();
            productIdMap = new HashMap<>();
            for (ProductBrandInfo info : productBrandInfos) {
                productList.add(info.getProductName());
                productListCopy.clear();
                productListCopy.addAll(productList);
                productIdMap.put(info.getProductName(), info.getProductId());
                productBrandMap.put(info.getProductName(), info.getBrandName());
            }
        }
        brandList = new ArrayList<>();
        //brandList.add(nullProject);
        if (brandMap != null) {
            for (Map.Entry<Integer, String> entry : brandMap.entrySet()) {
                brandList.add(entry.getValue());
            }
        }
    }

    @Override
    protected void initView() {
//        btnCancel = findViewById(R.id.button_cancel);
//        btnNext = findViewById(R.id.button_next);

        titleName = getResources().getString(R.string.select_project);
        cancelButton = getResources().getString(R.string.cancel);
        nullProject = getResources().getString(R.string.null_project);

        tvTitle.setText(R.string.select_project);
        ivBack.setOnClickListener(this);
        tvBrand.setOnClickListener(this);
        tvProduct.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnNext.setOnClickListener(this);
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
            case R.id.button_cancel:
                finish();
                break;
            case R.id.button_next:
                toRegisterActivity();
                break;
            default:
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
            intent.putExtra("brandMap", brandMap);
            intent.putExtra("productBrandMap", productBrandMap);
            intent.putExtra("productIdMap", productIdMap);
            intent.putParcelableArrayListExtra("productBrandInfos", (ArrayList<? extends Parcelable>) productBrandInfos);
            startActivity(intent);
        } else {
            UIUtils.showToast(getResources().getString(R.string.select_pro_and_brand));
        }
    }

    /**
     * 选择产品
     *
     * @param datas
     */
    private void selectProduct(final List<String> datas) {

        WidgetDialog.show_title_button_list(this, titleName, cancelButton, datas, new DialogInterface.OnItemClickListener<NormalSelectionDialog>() {
                    @Override
                    public void onItemClick(NormalSelectionDialog dialog, View button, int position) {
                        dialog.dismiss();
                        if (!selectedProduct.equals(datas.get(position))) {
                            selectedProduct = datas.get(position);
                            // 重置产品Id
                            selectedProductId = productIdMap.get(selectedProduct);
                            tvProduct.setText(selectedProduct);

                            String brandName = productBrandMap.get(selectedProduct);
                            if (TextUtils.isEmpty(brandName)) {
                                selectedBrand = nullProject;
                                //重置品牌id
                                selectedBrandId = -1;
                            } else {
                                selectedBrand = brandName;
                                //重置品牌id
                                selectedBrandId = Utils.getKey(brandMap, selectedBrand);
                            }
                            tvBrand.setText(selectedBrand);
                            //筛选该品牌的产品
                            productListCopy.clear();
                            //没有品牌信息
                            if (selectedBrand.equals(nullProject)) {
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
                }
        );

    }

    private void selectBrand(final List<String> datas) {

        WidgetDialog.show_title_button_list(this, titleName, cancelButton, datas, new DialogInterface.OnItemClickListener<NormalSelectionDialog>() {
            @Override
            public void onItemClick(NormalSelectionDialog dialog, View button, int
                    position) {
                dialog.dismiss();
                if (!selectedBrand.equals(datas.get(position))) {
                    tvProduct.setText("");
                    selectedProduct = "";
                    //重置选中产品为-1
                    selectedProductId = -1;
                    selectedBrand = datas.get(position);
                    tvBrand.setText(selectedBrand);
                    //筛选品牌
                    if (productBrandInfos != null) {
                        productListCopy.clear();
                        //没有品牌信息
                        if (selectedBrand.equals(nullProject)) {
                            //重置品牌Id
                            selectedBrandId = -1;
                            productListCopy.addAll(productList);
                        } else {
                            //重置品牌Id
                            selectedBrandId = Utils.getKey(brandMap, selectedBrand);
                            for (ProductBrandInfo info : productBrandInfos) {
                                if (selectedBrand.equals(info.getBrandName())) {
                                    productListCopy.add(info.getProductName());
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
