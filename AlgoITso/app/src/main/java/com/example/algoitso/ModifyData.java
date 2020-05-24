package com.example.algoitso;

import com.google.gson.annotations.SerializedName;

public class ModifyData {
    @SerializedName("userPwd")
    private String userPwd;

    @SerializedName("userPhone")
    private String userPhone;

    @SerializedName("userEmail")
    private String userEmail;

    public ModifyData(String userPwd, String userPhone, String userEmail) {
        this.userPwd = userPwd;
        this.userPhone = userPhone;
        this.userEmail = userEmail;

    }
    public String getModifyPwd(){
        return this.userPwd;
    }
    public String getModifyPhone(){
        return this.userPhone;
    }
}