package com.example.algoitso;

import com.google.gson.annotations.SerializedName;

public class JoinData {
    @SerializedName("userName")
    private String userName;

    @SerializedName("userEmail")
    private String userEmail;

    @SerializedName("userPwd")
    private String userPwd;

    @SerializedName("userPhone")
    private String userPhone;

    public JoinData(String userName, String userEmail, String userPwd, String userPhone) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPwd = userPwd;
        this.userPhone = userPhone;

    }
}
