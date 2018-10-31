package com.ty.nanfangscanner.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SectionNumber implements Parcelable {


    private List<String> startCodeList;
    private List<String> endCodeList;
    private String brandName;
    private int brandId;
    private String productName;
    private int productId;
    private int count;//号段数量
    private String saveTime;

    public List<String> getStartCodeList() {
        return startCodeList;
    }

    public void setStartCodeList(List<String> startCodeList) {
        this.startCodeList = startCodeList;
    }

    public List<String> getEndCodeList() {
        return endCodeList;
    }

    public void setEndCodeList(List<String> endCodeList) {
        this.endCodeList = endCodeList;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.startCodeList);
        dest.writeStringList(this.endCodeList);
        dest.writeString(this.brandName);
        dest.writeInt(this.brandId);
        dest.writeString(this.productName);
        dest.writeInt(this.productId);
        dest.writeInt(this.count);
        dest.writeString(this.saveTime);
    }

    public SectionNumber() {
    }

    protected SectionNumber(Parcel in) {
        this.startCodeList = in.createStringArrayList();
        this.endCodeList = in.createStringArrayList();
        this.brandName = in.readString();
        this.brandId = in.readInt();
        this.productName = in.readString();
        this.productId = in.readInt();
        this.count = in.readInt();
        this.saveTime = in.readString();
    }

    public static final Parcelable.Creator<SectionNumber> CREATOR = new Parcelable.Creator<SectionNumber>() {
        @Override
        public SectionNumber createFromParcel(Parcel source) {
            return new SectionNumber(source);
        }

        @Override
        public SectionNumber[] newArray(int size) {
            return new SectionNumber[size];
        }
    };
}
