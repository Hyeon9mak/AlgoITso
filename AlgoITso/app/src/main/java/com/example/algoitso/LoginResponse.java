package com.example.algoitso;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("info")
    private String[] info;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String[] getInfo() { return info; }
}
