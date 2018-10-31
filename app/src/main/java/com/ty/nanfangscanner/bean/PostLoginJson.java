package com.ty.nanfangscanner.bean;

/**
 * Created by qunying.wangyuan on 2017/12/21.
 */

public class PostLoginJson {

    private String clientName;
    private String clientPassword;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPassword() {
        return clientPassword;
    }

    public void setClientPassword(String clientPassword) {
        this.clientPassword = clientPassword;
    }

    @Override
    public String toString() {
        return "PostLoginJson{" +
                "clientName='" + clientName + '\'' +
                ", clientPassword='" + clientPassword + '\'' +
                '}';
    }
}
