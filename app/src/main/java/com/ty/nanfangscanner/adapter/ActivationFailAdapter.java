package com.ty.nanfangscanner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.bean.ActivationInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivationFailAdapter extends RecyclerView.Adapter<ActivationFailAdapter.ViewHolder> {

    private List<ActivationInfo> failList;

    public ActivationFailAdapter(List<ActivationInfo> failList) {
        this.failList = failList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_code_url)
        TextView tvCodeUrl;
        @BindView(R.id.tv_code_num)
        TextView tvCodeNum;
        @BindView(R.id.tv_fail_reason)
        TextView tvFailReason;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        return failList.size();
    }

    //绑定视图管理者
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ActivationInfo info = failList.get(position);
        holder.tvCodeUrl.setText(info.getActivationUrl());
        holder.tvCodeNum.setText(info.getActivationFailSeqnum());
        holder.tvFailReason.setText(info.getResultMsg());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activation_fail, null);
        return new ViewHolder(root);
    }

}