package com.ty.nanfangscanner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.bean.RegisterRecordInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<RegisterRecordInfo> infos;
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAIL = 1;

    public RegisterRecordAdapter(List<RegisterRecordInfo> infos) {
        this.infos = infos;
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    @Override
    public int getItemViewType(int position) {
        RegisterRecordInfo info = infos.get(position);
        if (info.getStatus() == 1) {
            return TYPE_SUCCESS;
        } else {
            return TYPE_FAIL;
        }
    }

    //绑定视图管理者
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        RegisterRecordInfo info = infos.get(position);
        if (holder instanceof SuccessViewHolder) {
            SuccessViewHolder successHolder = (SuccessViewHolder) holder;
            successHolder.tvStartCodeNum.setText("开始码序号：" + info.getEnterpriseSeqnumStart());
            successHolder.tvEndCodeNum.setText("结束码序号：" + info.getEnterpriseSeqnumEnd() + "");
            successHolder.tvProduct.setText("产 品 名 称：" + info.getProductName());
        } else if (holder instanceof FailViewHolder) {
            FailViewHolder failHolder = (FailViewHolder) holder;
            failHolder.tvStartCodeUrl.setText("开始码URL："+info.getCodeUrlStart());
            failHolder.tvEndCodeUrl.setText("结束码URL："+info.getCodeUrlEnd());
            failHolder.tvFailReason.setText("失 败 原 因：" + info.getErrorMsg());
            failHolder.tvProduct.setText("产 品 名 称：" + info.getProductName());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root;
        if (viewType == TYPE_SUCCESS) {
            root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_register_success_record, parent, false);
            return new SuccessViewHolder(root);
        } else {
            root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_register_fail_record, parent, false);
            return new FailViewHolder(root);
        }
    }

    public class SuccessViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_start_code_num)
        TextView tvStartCodeNum;
        @BindView(R.id.tv_end_code_num)
        TextView tvEndCodeNum;
        @BindView(R.id.tv_product)
        TextView tvProduct;

        public SuccessViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class FailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_start_code_url)
        TextView tvStartCodeUrl;
        @BindView(R.id.tv_end_code_url)
        TextView tvEndCodeUrl;
        @BindView(R.id.tv_product)
        TextView tvProduct;
        @BindView(R.id.tv_fail_reason)
        TextView tvFailReason;

        public FailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}