package com.ty.nanfangscanner.bean;

/**
 * Created by qunying.wangyuan on 2017/12/1.
 */

public class LoginInfo {

    /**
     * accessToken : eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJ1c2VybmFtZSI6IklTVm1lbmduaXVAc2FvLnNvIiwiaWQiOjEwNjYsImVzZUlkIjoxLCJ0b3BFc2VJZCI6MSwiZXNlQ29kZSI6InRvdXl1biIsInVzZXJUeXBlIjozLCJhdWQiOiIwOThmNmJjZDQ2MjFkMzczY2FkZTRlODMyNjI3YjRmNiIsImlzcyI6IklTVlNlcnZlciIsImV4cCI6MTUzMjAwMDAxMCwibmJmIjoxNTMxOTg4MDEwfQ.8wZuYjUaBZ42a1xaCfiowVo1EOas2PUhVCgPggbZrOc
     * tokenType : Bearer
     * expiresIn : 12000
     * userType : 3
     */

    private String accessToken;
    private String tokenType;
    private int expiresIn;
    private int userType;
    /**
     * code : 10050
     * message : UserName or Password error
     */

    private int code;
    private String message;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expiresIn=" + expiresIn +
                ", userType=" + userType +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
