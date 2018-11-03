package com.ty.nanfangscanner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.activity.base.BaseActivity;
import com.ty.nanfangscanner.adapter.QRCodeAdapter;
import com.ty.nanfangscanner.utils.UIUtils;
import com.ty.nanfangscanner.view.ButtonView;
import com.ty.nanfangscanner.view.DividerItemDecoration;
import com.ty.nanfangscanner.view.WidgetDialog;
import com.wevey.selector.dialog.DialogInterface;
import com.wevey.selector.dialog.NormalAlertDialog;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author TY
 */
public class StartQrCodeActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.button_cancel)
    ButtonView ButtonCancel;
    @BindView(R.id.iv_next)
    ImageView ivNext;

    private ArrayList<String> qrCodeList;
    private QRCodeAdapter adapter;
    private String mSelectedBrand;
    private String mSelectedProduct;
    private int mSelectedBrandId;
    private int mSelectedProductId;


    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {


    }

    @Override
    protected int setActivityLayout() {
        return R.layout.activity_start_qr_code;
    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        mSelectedBrand = intent.getStringExtra("brand");
        mSelectedProduct = intent.getStringExtra("product");
        mSelectedBrandId = intent.getIntExtra("brandId", -1);
        mSelectedProductId = intent.getIntExtra("productId", -1);
        qrCodeList = new ArrayList<>();
    }

    @Override
    protected void initView() {

        tvTitle.setText("扫描结束码");
        ivBack.setOnClickListener(this);
        tvReset.setOnClickListener(this);
        ButtonCancel.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        tvBrand.setText("品牌：" + mSelectedBrand);
        tvProduct.setText("产品：" + mSelectedProduct);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(UIUtils.getContext());
        rvCode.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST,
                UIUtils.dip2px(1), UIUtils.getColor(R.color.split_line)));
        rvCode.setLayoutManager(mLayoutManager);
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
                        if (qrCodeList.size() < 20) {
                            qrCodeList.add(result);
                            adapter.notifyDataSetChanged();
                            tvCount.setText(qrCodeList.size() + "");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_reset:
                if (qrCodeList != null && qrCodeList.size() > 0) {
                    showClearConfirmDialog();
                }
                break;
            case R.id.button_cancel:
                finish();
                break;
            case R.id.iv_next:
                toEndCodeActivity();
                break;
            default:
                break;
        }
    }

    private void toEndCodeActivity() {
        if (qrCodeList == null || qrCodeList.size() == 0) {
            UIUtils.showToast("请扫描结束码");
        } else {
            //Intent intent = new Intent(StartQrCodeActivity.this, EndQrCodeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("brand", mSelectedBrand);
            bundle.putString("product", mSelectedProduct);
            bundle.putInt("brandId", mSelectedBrandId);
            bundle.putInt("productId", mSelectedProductId);
            bundle.putStringArrayList("endCodeList", qrCodeList);
//            intent.putExtra("product", mSelectedProduct);
//            intent.putExtra("brandId", mSelectedBrandId);
//            intent.putExtra("productId", mSelectedProductId);
//            intent.putStringArrayListExtra("endCodeList", (ArrayList<String>) qrCodeList);
//            startActivity(intent);
            gotoActivity(EndQrCodeActivity.class,true,bundle);
        }
    }

    private void showClearConfirmDialog() {

        WidgetDialog.show_title_content_left_right(this, "清空列表", "是否确认清空？",
                "是", "否", new DialogInterface.OnLeftAndRightClickListener<NormalAlertDialog>() {
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
                });

    }
}
