package com.ty.nanfangscanner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.bean.SectionNumber;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SectionNumberAdapter extends RecyclerView.Adapter<SectionNumberAdapter.ViewHolder> {


    private List<SectionNumber> infos;
    private OnDeleteClickListener listener;
    public SectionNumberAdapter(List<SectionNumber> infos) {
        this.infos = infos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_save_time)
        TextView tvSaveTime;
        @BindView(R.id.tv_section_num)
        TextView tvSectionNum;
        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.iv_del)
        ImageView ivDel;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    //绑定视图管理者
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SectionNumber info = infos.get(position);
        holder.tvSaveTime.setText(info.getSaveTime());
        holder.tvSectionNum.setText(info.getCount() + "");
        holder.tvProductName.setText(info.getProductName());
        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onDelete(position);
                }
               // deleteItem(position);
            }
        });
    }

    public void deleteItem(int pos) {
        if (pos==infos.size()){
            return;
        }
        try {
            infos.remove(pos);
            notifyItemRemoved(pos);
            //刷新列表，否则回出现删除错位
            if (pos!=infos.size()){
                notifyItemRangeChanged(pos,infos.size()-pos);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section_number, null);
        return new ViewHolder(root);
    }

    public interface OnDeleteClickListener{
        void onDelete(int position);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener){
        this.listener=listener;
    }

}