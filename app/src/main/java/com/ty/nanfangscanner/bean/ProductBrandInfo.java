package com.ty.nanfangscanner.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author TY
 */
public class ProductBrandInfo implements Parcelable {
    private String brandName;
    private String productName;
    private int brandId;
    private int productId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.brandName);
        dest.writeString(this.productName);
        dest.writeInt(this.brandId);
        dest.writeInt(this.productId);
    }

    public ProductBrandInfo() {
    }

    protected ProductBrandInfo(Parcel in) {
        this.brandName = in.readString();
        this.productName = in.readString();
        this.brandId = in.readInt();
        this.productId = in.readInt();
    }

    public static final Parcelable.Creator<ProductBrandInfo> CREATOR = new Parcelable.Creator<ProductBrandInfo>() {
        @Override
        public ProductBrandInfo createFromParcel(Parcel source) {
            return new ProductBrandInfo(source);
        }

        @Override
        public ProductBrandInfo[] newArray(int size) {
            return new ProductBrandInfo[size];
        }
    };

    @Override
    public String toString() {
        return "ProductBrandInfo{" +
                "brandName='" + brandName + '\'' +
                ", productName='" + productName + '\'' +
                '}';
    }
}
