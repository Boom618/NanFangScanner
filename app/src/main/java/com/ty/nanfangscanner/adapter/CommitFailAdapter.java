package com.ty.nanfangscanner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.bean.RegisterCheckInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommitFailAdapter extends RecyclerView.Adapter<CommitFailAdapter.ViewHolder> {


    private List<RegisterCheckInfo> failList;

    public CommitFailAdapter(List<RegisterCheckInfo> failList) {
        this.failList = failList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_start_code_url)
        TextView tvStartCodeUrl;
        @BindView(R.id.tv_start_code_num)
        TextView tvStartCodeNum;
        @BindView(R.id.tv_end_code_url)
        TextView tvEndCodeUrl;
        @BindView(R.id.tv_end_code_num)
        TextView tvEndCodeNum;
        @BindView(R.id.tv_product_name)
        TextView tvProductName;
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
        RegisterCheckInfo info = failList.get(position);
        holder.tvStartCodeUrl.setText(info.getCodeUrlStart());
        holder.tvStartCodeNum.setText(info.getSeqnumStart());
        holder.tvEndCodeUrl.setText(info.getCodeUrlEnd());
        holder.tvEndCodeNum.setText(info.getSeqnumEnd());
        holder.tvFailReason.setText(info.getResultMsg());
        holder.tvProductName.setText(info.getProductName());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commit_fail, null);
        return new ViewHolder(root);
    }

}