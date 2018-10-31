package com.ty.nanfangscanner.bean;

public class ActivationRecordInfo {

    /**
     * brandId : -1
     * productId : 0
     * status : 0
     * activationCount : 0
     * seqNumCount : 0
     * codeUrl : HTTP://WW.PO/Q/QQ.$5J+A9+YPZ20RICDOZU-
     * activationTime : 2018-07-25T04:29:15.623+0000
     * errorMsg : 未完成登记
     * enterpriseSeqnumStart : 554
     * enterpriseSeqnumEnd : 556
     */

    private int brandId;
    private int productId;
    private int status;
    private int activationCount;
    private int seqNumCount;
    private String codeUrl;
    private String activationTime;
    private String errorMsg;
    private int enterpriseSeqnumStart;
    private int enterpriseSeqnumEnd;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getActivationCount() {
        return activationCount;
    }

    public void setActivationCount(int activationCount) {
        this.activationCount = activationCount;
    }

    public int getSeqNumCount() {
        return seqNumCount;
    }

    public void setSeqNumCount(int seqNumCount) {
        this.seqNumCount = seqNumCount;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(String activationTime) {
        this.activationTime = activationTime;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getEnterpriseSeqnumStart() {
        return enterpriseSeqnumStart;
    }

    public void setEnterpriseSeqnumStart(int enterpriseSeqnumStart) {
        this.enterpriseSeqnumStart = enterpriseSeqnumStart;
    }

    public int getEnterpriseSeqnumEnd() {
        return enterpriseSeqnumEnd;
    }

    public void setEnterpriseSeqnumEnd(int enterpriseSeqnumEnd) {
        this.enterpriseSeqnumEnd = enterpriseSeqnumEnd;
    }
}
