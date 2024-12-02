package com.example.registrationtemplate.responses;

import com.google.gson.annotations.SerializedName;

public class Ans_password_recovery_er {

    @SerializedName("email")
    String[] email;
    public String[] getEmail() {
        return email;
    }
    public void setEmail(String[] email) {
        this.email = email;
    }
}
