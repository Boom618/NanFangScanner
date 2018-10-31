package com.ty.nanfangscanner.bean;

public class BrandInfo {

    /**
     * id : 14
     * status : 1
     * createdDate : 2018-03-09T02:56:09.203+0000
     * createdBy : 1
     * lastModifiedDate : 2018-03-09T02:56:09.203+0000
     * lastModifiedBy : 1
     * sourceType : 1
     * eseId : 1
     * topEseId : 1
     * code : mengniu
     * name : 蒙牛慢燃
     */

    private int id;
    private int status;
    private String code;
    private String name;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BrandInfo{" +
                "id=" + id +
                ", status=" + status +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
