package com.ty.nanfangscanner.bean;

public class RegisterRecordInfo {

    /**
     * brandId : 14
     * productId : 849
     * productName : 真果粒
     * status : 0
     * activationCount : 0
     * seqNumCount : 0
     * registerTime : 2018-07-25T03:58:24.669+0000
     * codeUrlStart : http://tracing-cn.tcc.so/download/android/difarmStand.apk
     * codeUrlEnd : http://tracing-cn.tcc.so/download/android/mpets.apk
     * errorMsg : 非法码
     * enterpriseSeqnumStart : 554
     * enterpriseSeqnumEnd : 556
     * activationTime : 2018-07-25T04:29:15.567+0000
     */

    private int brandId;
    private int productId;
    private String productName;
    private int status;
    private int activationCount;
    private int seqNumCount;
    private String registerTime;
    private String codeUrlStart;
    private String codeUrlEnd;
    private String errorMsg;
    private int enterpriseSeqnumStart;
    private int enterpriseSeqnumEnd;
    private String activationTime;

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getCodeUrlStart() {
        return codeUrlStart;
    }

    public void setCodeUrlStart(String codeUrlStart) {
        this.codeUrlStart = codeUrlStart;
    }

    public String getCodeUrlEnd() {
        return codeUrlEnd;
    }

    public void setCodeUrlEnd(String codeUrlEnd) {
        this.codeUrlEnd = codeUrlEnd;
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

    public String getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(String activationTime) {
        this.activationTime = activationTime;
    }
}
