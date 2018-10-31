package com.ty.nanfangscanner.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.adapter.ActivationFailAdapter;
import com.ty.nanfangscanner.bean.ActivationInfo;
import com.ty.nanfangscanner.utils.UIUtils;
import com.ty.nanfangscanner.view.SpacesItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivationFailActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_commit_fail)
    RecyclerView rvCommitFail;
    @BindView(R.id.iv_pre)
    ImageView ivPre;
    @BindView(R.id.iv_cancel)
    ImageView ivCancel;
    private static final int RESULT_CODE=222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_fail);
        ButterKnife.bind(this);
        tvTitle.setText("激活失败记录");
        ivBack.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        ivPre.setOnClickListener(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(UIUtils.getContext());
        rvCommitFail.addItemDecoration(new SpacesItemDecoration(8));
        rvCommitFail.setLayoutManager(mLayoutManager);

        List<ActivationInfo> failList = getIntent().getParcelableArrayListExtra("failList");
        if (failList != null) {
            ActivationFailAdapter adapter = new ActivationFailAdapter(failList);
            rvCommitFail.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_pre:
                finish();
                break;

            case R.id.iv_cancel:
                setResult(RESULT_CODE);
                finish();
                break;
        }
    }
}
