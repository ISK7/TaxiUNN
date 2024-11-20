package com.example.registrationtemplate.requests;

import com.google.gson.annotations.SerializedName;

public class recover_change {
    @SerializedName("email")
    String email;
    @SerializedName("password")
    String password;

    public recover_change(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
