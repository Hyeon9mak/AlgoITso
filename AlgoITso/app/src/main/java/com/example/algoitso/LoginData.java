package com.example.algoitso;

import com.google.gson.annotations.SerializedName;

public class LoginData {
    @SerializedName("UserEmail")
    String userEmail;

    @SerializedName("UserPwd")
    String userPwd;

    public LoginData(String userEmail, String userPwd) {
        this.userEmail = userEmail;
        this.userPwd = userPwd;
    }
}
