package com.ty.nanfangscanner.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ty.nanfangscanner.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author TY
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setActivityLayout());
        mUnbinder = ButterKnife.bind(this);

        initView();
        onBaseCreate(savedInstanceState);

    }

    /**
     * onCreate view
     *
     * @param savedInstanceState
     */
    protected abstract void onBaseCreate(Bundle savedInstanceState);

    /**
     * 设置 layout
     *
     * @return
     */
    protected abstract int setActivityLayout();


    /**
     * 初始化 View
     */
    protected abstract void initView();

    /**
     * 初始化 Data
     */
    protected abstract void initData();


    @Override
    protected void onStart() {
        super.onStart();


        initData();
    }

    /**
     * 统一 Bar
     * @param intId 标题
     */
    protected void initToolBar(int intId) {
        // 左边返回
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 中间标题
        TextView topText = findViewById(R.id.tv_title);
        topText.setText(intId);

    }

    /**
     * Activity 跳转
     *
     * @param cls             当前 activity
     * @param bundle          数据
     * @param isCloseActivity 是否关闭当前页面
     */
    protected void gotoActivity(Class<?> cls, Boolean isCloseActivity, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        if (isCloseActivity) {
            finish();
        }
    }

    protected void gotoActivity(Class<?> cls, Boolean isCloseActivity) {
        gotoActivity(cls, isCloseActivity, null);
    }

    protected void gotoActivity(Class<?> cls) {
        gotoActivity(cls, false, null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
