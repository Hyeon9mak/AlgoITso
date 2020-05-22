package com.example.algoitso;

import com.google.gson.annotations.SerializedName;

public class JoinData {
    @SerializedName("UserName")
    private String userName;

    @SerializedName("UserEmail")
    private String userEmail;

    @SerializedName("UserPwd")
    private String userPwd;

    @SerializedName("UserPhone")
    private String userPhone;

    public JoinData(String userName, String userEmail, String userPwd, String userPhone) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPwd = userPwd;
        this.userPhone = userPhone;

    }
}
