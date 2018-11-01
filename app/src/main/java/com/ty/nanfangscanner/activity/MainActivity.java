package com.ty.nanfangscanner.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.activity.base.BaseActivity;
import com.ty.nanfangscanner.utils.Utils;

import butterknife.BindView;

/**
 * @author TY
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

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
    protected void onBaseCreate(Bundle savedInstanceState) {


    }

    @Override
    protected int setActivityLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        tvVersion.setText(Utils.getVersion());
        ivSync.setOnClickListener(this);
        llActive.setOnClickListener(this);
        llRegister.setOnClickListener(this);
        llCheck.setOnClickListener(this);
        llRecord.setOnClickListener(this);
        PgyUpdateManager.register(this);
    }

    @Override
    protected void initData() {
        checkUpdate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_synchronization:
                gotoActivity(SynchronizationActivity.class);
                break;

            case R.id.ll_register:
                gotoActivity(SelectProductActivity.class);
                break;

            case R.id.ll_active:
                gotoActivity(ActiveActivity.class);
                break;

            case R.id.ll_check:
                gotoActivity(CheckActivity.class);
                break;

            case R.id.ll_record:
                gotoActivity(RecordActivity.class);
                break;
            default:
                break;
        }
    }

    private void checkUpdate() {
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
                                startDownloadTask(MainActivity.this,appBean.getDownloadURL());
                            }
                        }).show();
            }

            @Override
            public void onNoUpdateAvailable() {
            }
        });
    }
}
