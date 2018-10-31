package com.ty.nanfangscanner.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class RegisterCheckInfo implements Parcelable {

    /**
     * codeUrlEnd : HTTP://WW.PO/Q/00EL6$XNSXZFFZK44RP:Y41
     * codeUrlStart : HTTP://WW.PO/Q/22+.P*S$GSCP7M9D4H5OVV0
     * resultMsg : 处理成功
     * seqnumEnd : 10000000000003054
     * seqnumStart : 10000000000003055
     * status : 1
     */

    private String codeUrlEnd;
    private String codeUrlStart;
    private String resultMsg;
    private String seqnumEnd;
    private String seqnumStart;
    private int status;
    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCodeUrlEnd() {
        return codeUrlEnd;
    }

    public void setCodeUrlEnd(String codeUrlEnd) {
        this.codeUrlEnd = codeUrlEnd;
    }

    public String getCodeUrlStart() {
        return codeUrlStart;
    }

    public void setCodeUrlStart(String codeUrlStart) {
        this.codeUrlStart = codeUrlStart;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getSeqnumEnd() {
        return seqnumEnd;
    }

    public void setSeqnumEnd(String seqnumEnd) {
        this.seqnumEnd = seqnumEnd;
    }

    public String getSeqnumStart() {
        return seqnumStart;
    }

    public void setSeqnumStart(String seqnumStart) {
        this.seqnumStart = seqnumStart;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RegisterCheckInfo{" +
                "codeUrlEnd='" + codeUrlEnd + '\'' +
                ", codeUrlStart='" + codeUrlStart + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                ", seqnumEnd='" + seqnumEnd + '\'' +
                ", seqnumStart='" + seqnumStart + '\'' +
                ", status=" + status +
                ", productName='" + productName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.codeUrlEnd);
        dest.writeString(this.codeUrlStart);
        dest.writeString(this.resultMsg);
        dest.writeString(this.seqnumEnd);
        dest.writeString(this.seqnumStart);
        dest.writeInt(this.status);
        dest.writeString(this.productName);
    }

    public RegisterCheckInfo() {
    }

    protected RegisterCheckInfo(Parcel in) {
        this.codeUrlEnd = in.readString();
        this.codeUrlStart = in.readString();
        this.resultMsg = in.readString();
        this.seqnumEnd = in.readString();
        this.seqnumStart = in.readString();
        this.status = in.readInt();
        this.productName = in.readString();
    }

    public static final Parcelable.Creator<RegisterCheckInfo> CREATOR = new Parcelable.Creator<RegisterCheckInfo>() {
        @Override
        public RegisterCheckInfo createFromParcel(Parcel source) {
            return new RegisterCheckInfo(source);
        }

        @Override
        public RegisterCheckInfo[] newArray(int size) {
            return new RegisterCheckInfo[size];
        }
    };
}
