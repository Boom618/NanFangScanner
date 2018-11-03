package com.ty.nanfangscanner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ty.nanfangscanner.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 绑定跟踪标——耳标Adapter
 * @author TY
 */
public class QRCodeAdapter extends RecyclerView.Adapter<QRCodeAdapter.ViewHolder> {

    private List<String> list;

    public QRCodeAdapter(List<String> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_code)
        TextView tvCode;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //绑定视图管理者
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String codeStr = list.get(position);
        holder.tvCode.setText(codeStr);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.qr_code_item,null);
        return new ViewHolder(root);
    }

}