package com.ty.nanfangscanner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ty.nanfangscanner.R;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 绑定跟踪标——耳标Adapter
 */
public class DialogProductAdapter extends RecyclerView.Adapter<DialogProductAdapter.ViewHolder> {

    private List<Map.Entry<String, Integer>> list;

    public DialogProductAdapter(List<Map.Entry<String, Integer>> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.tv_product_num)
        TextView tvProductNum;
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
        Map.Entry<String, Integer> info = list.get(position);
        holder.tvProductName.setText(info.getKey()+"：");
        holder.tvProductNum.setText(info.getValue()+"");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_product, null);
        return new ViewHolder(root);
    }

}