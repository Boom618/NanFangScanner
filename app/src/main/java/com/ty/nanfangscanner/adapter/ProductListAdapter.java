package com.ty.nanfangscanner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.bean.ProductBrandInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductListAdapter extends RecyclerView.Adapter implements Filterable {

    private OnItemClickListener mListener;
    private List<ProductBrandInfo> info;
    private List<ProductBrandInfo> infoCopy;
    private LayoutInflater mInflater;
    //接口实例

    public ProductListAdapter(Context mContext, List<ProductBrandInfo> mList) {
        this.info = mList;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_product, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        ProductBrandInfo productBrandInfo = info.get(position);
        itemHolder.tvProduct.setText(productBrandInfo.getProductName());
        String brandStr=TextUtils.isEmpty(productBrandInfo.getBrandName())?"":productBrandInfo.getBrandName();
        itemHolder.tvBrand.setText(brandStr);
        if (mListener != null) {
            itemHolder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    @Override
    public Filter getFilter() {
         return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                //子线程中调用
                FilterResults results = new FilterResults();//初始化过滤结果对象
                List<ProductBrandInfo> newValues = new ArrayList<>();
                if (infoCopy==null){
                    infoCopy=new ArrayList<>(info);
                }
                if (charSequence==null||charSequence.length()==0){
                    results.count=infoCopy.size();
                    results.values=infoCopy;
                }else {
                    charSequence=charSequence.toString().toLowerCase();
                    for (int i = 0; i < infoCopy.size(); i++) {
                        ProductBrandInfo info = infoCopy.get(i);
                        String productName = info.getProductName().toLowerCase();//工单号
                        if (productName.contains(charSequence) ) {
                            newValues.add(info);//满足筛选条件添加该条数据
                        }
                    }
                    results.count = newValues.size();
                    results.values = newValues;
                //    Log.e("条目", results.count + "");
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                //UI线程中调用
                info= (List<ProductBrandInfo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_brand)
        TextView tvBrand;
        @BindView(R.id.tv_product)
        TextView tvProduct;
        @BindView(R.id.ll_item)
        LinearLayout llItem;
        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

}
