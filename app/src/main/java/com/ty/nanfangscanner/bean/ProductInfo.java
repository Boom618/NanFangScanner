package com.ty.nanfangscanner.bean;

public class ProductInfo {
    private int id;
    private int status;
    private String name;
    private int brandId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "id=" + id +
                ", status=" + status +
                ", name='" + name + '\'' +
                ", brandId=" + brandId +
                '}';
    }
}
