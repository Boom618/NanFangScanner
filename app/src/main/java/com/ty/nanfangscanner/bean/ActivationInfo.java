package com.ty.nanfangscanner.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ActivationInfo implements Parcelable {

    /**
     * status : 0
     * activationUrl : HTTP://WW.PO/Q/BBKG$MW98PK:$0YG4:XJSXT
     * activationFailSeqnum : 10000000000003160
     * resultMsg : 未登记
     */


    private int status;
    private String activationUrl;
    private String activationFailSeqnum;
    private String resultMsg;
    /**
     * seqnumStart : 554
     * seqnumEnd : 554
     */

    private String seqnumStart;
    private String seqnumEnd;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getActivationUrl() {
        return activationUrl;
    }

    public void setActivationUrl(String activationUrl) {
        this.activationUrl = activationUrl;
    }

    public String getActivationFailSeqnum() {
        return activationFailSeqnum;
    }

    public void setActivationFailSeqnum(String activationFailSeqnum) {
        this.activationFailSeqnum = activationFailSeqnum;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getSeqnumStart() {
        return seqnumStart;
    }

    public void setSeqnumStart(String seqnumStart) {
        this.seqnumStart = seqnumStart;
    }

    public String getSeqnumEnd() {
        return seqnumEnd;
    }

    public void setSeqnumEnd(String seqnumEnd) {
        this.seqnumEnd = seqnumEnd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.activationUrl);
        dest.writeString(this.activationFailSeqnum);
        dest.writeString(this.resultMsg);
        dest.writeString(this.seqnumStart);
        dest.writeString(this.seqnumEnd);
    }

    public ActivationInfo() {
    }

    protected ActivationInfo(Parcel in) {
        this.status = in.readInt();
        this.activationUrl = in.readString();
        this.activationFailSeqnum = in.readString();
        this.resultMsg = in.readString();
        this.seqnumStart = in.readString();
        this.seqnumEnd = in.readString();
    }

    public static final Parcelable.Creator<ActivationInfo> CREATOR = new Parcelable.Creator<ActivationInfo>() {
        @Override
        public ActivationInfo createFromParcel(Parcel source) {
            return new ActivationInfo(source);
        }

        @Override
        public ActivationInfo[] newArray(int size) {
            return new ActivationInfo[size];
        }
    };
}
