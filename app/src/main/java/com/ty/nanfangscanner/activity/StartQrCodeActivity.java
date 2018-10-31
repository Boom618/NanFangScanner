package com.ty.nanfangscanner.activity;

import android.content.Intent;
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
import android.widget.TextView;

import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.adapter.QRCodeAdapter;
import com.ty.nanfangscanner.utils.UIUtils;
import com.ty.nanfangscanner.view.DividerItemDecoration;
import com.wevey.selector.dialog.DialogInterface;
import com.wevey.selector.dialog.NormalAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartQrCodeActivity extends AppCompatActivity implements View.OnClickListener {

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
    @BindView(R.id.iv_cancel)
    ImageView ivCancel;
    @BindView(R.id.iv_next)
    ImageView ivNext;

    private List<String> qrCodeList;
    private QRCodeAdapter adapter;
    private String mSelectedBrand;
    private String mSelectedProduct;
    private int mSelectedBrandId;
    private int mSelectedProductId;

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
        mSelectedBrandId = intent.getIntExtra("brandId",-1);
        mSelectedProductId = intent.getIntExtra("productId",-1);
        qrCodeList=new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_start_qr_code);
        ButterKnife.bind(this);
        tvTitle.setText("扫描结束码");
        ivBack.setOnClickListener(this);
        tvReset.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
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
         public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

         @Override
         public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             String result = charSequence.toString().replace("\n","");
             etCode.getText().clear();
             if (!TextUtils.isEmpty(result)){
                 if (!qrCodeList.contains(result)){
                     if (qrCodeList.size()<20){
                         qrCodeList.add(result);
                         adapter.notifyDataSetChanged();
                         tvCount.setText(qrCodeList.size()+"");
                     }else {
                         UIUtils.showToast("扫描数量已达最大值");
                     }
                 }else {
                     UIUtils.showToast("该码已扫过");
                 }
             }
         }

         @Override
         public void afterTextChanged(Editable editable) { }
     });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_reset:
                if (qrCodeList!=null&&qrCodeList.size()>0){
                    showClearConfirmDialog();
                }
                break;

            case R.id.iv_cancel:
                finish();
                break;

            case R.id.iv_next:
                toEndCodeActivity();
                break;
        }
    }

    private void toEndCodeActivity() {
        if (qrCodeList==null||qrCodeList.size()==0){
            UIUtils.showToast("请扫描结束码");
        }else {
            Intent intent=new Intent(StartQrCodeActivity.this,EndQrCodeActivity.class);
            intent.putExtra("brand", mSelectedBrand);
            intent.putExtra("product", mSelectedProduct);
            intent.putExtra("brandId", mSelectedBrandId);
            intent.putExtra("productId", mSelectedProductId);
            intent.putStringArrayListExtra("endCodeList", (ArrayList<String>) qrCodeList);
            startActivity(intent);
            finish();
        }
    }

    private void showClearConfirmDialog() {
        NormalAlertDialog dialog2 = new NormalAlertDialog.Builder(StartQrCodeActivity.this)
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
                        tvCount.setText(qrCodeList.size()+"");
                    }

                    @Override
                    public void clickRightButton(NormalAlertDialog dialog, View view) {
                        dialog.dismiss();
                    }
                }).build();
        dialog2.show();
    }
}
