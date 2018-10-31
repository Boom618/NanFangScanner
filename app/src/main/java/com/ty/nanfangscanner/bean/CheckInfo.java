package com.ty.nanfangscanner.bean;

public class CheckInfo {

    /**
     * brandName : 未知品牌
     * productName : 未知产品
     * registrationTime : 2018/07/24 14:38:25
     * activationTime : 2018/07/24 14:39:09
     * activationStatus : 已激活
     * activationCount : 2
     * seqNumCount : 2
     * status : 1
     * enterpriseSeqnumStart : 554
     * enterpriseSeqnumEnd : 555
     * productFactoryName : 未知工厂
     * factoryName : 蒙牛印刷厂
     * resultMsg : 处理成功
     */

    private String brandName;
    private String productName;
    private String registrationTime;
    private String activationTime;
    private String activationStatus;
    private int activationCount;
    private int seqNumCount;
    private int status;
    private int enterpriseSeqnumStart;
    private int enterpriseSeqnumEnd;
    private String productFactoryName;
    private String factoryName;
    private String resultMsg;

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

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(String activationTime) {
        this.activationTime = activationTime;
    }

    public String getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(String activationStatus) {
        this.activationStatus = activationStatus;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getProductFactoryName() {
        return productFactoryName;
    }

    public void setProductFactoryName(String productFactoryName) {
        this.productFactoryName = productFactoryName;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
