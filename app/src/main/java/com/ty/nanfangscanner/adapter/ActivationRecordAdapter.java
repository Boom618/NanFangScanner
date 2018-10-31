package com.ty.nanfangscanner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.bean.ActivationRecordInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivationRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ActivationRecordInfo> infos;
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAIL = 1;

    public ActivationRecordAdapter(List<ActivationRecordInfo> infos) {
        this.infos = infos;
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    @Override
    public int getItemViewType(int position) {
        ActivationRecordInfo info = infos.get(position);
        if (info.getStatus()==1){
            return TYPE_SUCCESS;
        }else {
            return TYPE_FAIL;
        }
    }

    //绑定视图管理者
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ActivationRecordInfo info = infos.get(position);
        if (holder instanceof SuccessViewHolder){
            SuccessViewHolder successHolder= (SuccessViewHolder) holder;
            successHolder.tvStartCodeNum.setText("开始码序号："+info.getEnterpriseSeqnumStart());
            successHolder.tvEndCodeNum.setText("结束码序号："+info.getEnterpriseSeqnumEnd()+"");
            successHolder.tvActivationNum.setText("激 活 码 量："+info.getActivationCount());
            successHolder.tvSectionNum.setText("号 段 码 量："+info.getSeqNumCount());
        }else if (holder instanceof FailViewHolder){
            FailViewHolder failHolder= (FailViewHolder) holder;
            failHolder.tvCodeUrl.setText("开始码URL："+info.getCodeUrl());
            failHolder.tvFailReason.setText("失 败 原 因："+info.getErrorMsg());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root;
        if (viewType == TYPE_SUCCESS) {
            root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activation_success_record, parent,false);
            return new SuccessViewHolder(root);
        } else {
            root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activation_fail_record, parent,false);
            return new FailViewHolder(root);
        }
    }

    public class SuccessViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_start_code_num)
        TextView tvStartCodeNum;
        @BindView(R.id.tv_end_code_num)
        TextView tvEndCodeNum;
        @BindView(R.id.tv_section_num)
        TextView tvSectionNum;
        @BindView(R.id.tv_activation_num)
        TextView tvActivationNum;

        public SuccessViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class FailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_code_url)
        TextView tvCodeUrl;
        @BindView(R.id.tv_fail_reason)
        TextView tvFailReason;
        public FailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}