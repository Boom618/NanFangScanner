package com.ty.nanfangscanner.bean;

public class PostRegisterCheckInfo {

    private int brandId;
    private String codeUrlEnd;
    private String codeUrlStart;
    private int productId;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
