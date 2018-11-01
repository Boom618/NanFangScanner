package com.ty.nanfangscanner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ty.nanfangscanner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author TY
 */
public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_register)
    LinearLayout llRegister;
    @BindView(R.id.ll_active)
    LinearLayout llActive;
    @BindView(R.id.iv_cancel)
    ImageView ivCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        tvTitle.setText("记录查询");
        ivBack.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        llActive.setOnClickListener(this);
        llRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_cancel:
                finish();
                break;

            case R.id.ll_active:
                startActivity(new Intent(RecordActivity.this, ActivationRecordActivity.class));
                break;

            case R.id.ll_register:
                startActivity(new Intent(RecordActivity.this, RegisterRecordActivity.class));
                break;
            default:
                break;
        }
    }
}
