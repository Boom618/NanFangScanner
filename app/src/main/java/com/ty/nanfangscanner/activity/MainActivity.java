package com.ty.nanfangscanner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ll_register)
    LinearLayout llRegister;
    @BindView(R.id.ll_active)
    LinearLayout llActive;
    @BindView(R.id.ll_check)
    LinearLayout llCheck;
    @BindView(R.id.ll_record)
    LinearLayout llRecord;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.btn_synchronization)
    ImageView ivSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tvVersion.setText(Utils.getVersion());
        ivSync.setOnClickListener(this);
        llActive.setOnClickListener(this);
        llRegister.setOnClickListener(this);
        llCheck.setOnClickListener(this);
        llRecord.setOnClickListener(this);
        PgyUpdateManager.register(this);
        checkUpdate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_synchronization:
                startActivity(new Intent(MainActivity.this, SynchronizationActivity.class));
                break;

            case R.id.ll_register:
                startActivity(new Intent(MainActivity.this, SelectProductActivity.class));
                break;

            case R.id.ll_active:
                startActivity(new Intent(MainActivity.this, ActiveActivity.class));
                break;

            case R.id.ll_check:
                startActivity(new Intent(MainActivity.this, CheckActivity.class));
                break;

            case R.id.ll_record:
                startActivity(new Intent(MainActivity.this, RecordActivity.class));
                break;
        }
    }

    private void checkUpdate(){
        PgyUpdateManager.register(MainActivity.this, new UpdateManagerListener() {

            @Override
            public void onUpdateAvailable(final String result) {

                final AppBean appBean = getAppBeanFromString(result);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("软件更新")
                        .setMessage("请及时更新软件，以免影响使用")
                        .setNegativeButton("确定", new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(android.content.DialogInterface dialogInterface, int i) {
                                startDownloadTask(
                                        MainActivity.this,
                                        appBean.getDownloadURL());
                            }
                        }).show();
            }

            @Override
            public void onNoUpdateAvailable() {
            }
        });
    }
}
