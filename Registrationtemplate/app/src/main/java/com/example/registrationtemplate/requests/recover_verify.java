package com.example.registrationtemplate.requests;

import com.google.gson.annotations.SerializedName;

public class recover_verify {
    @SerializedName("email")
    public String email;
    @SerializedName("verification_code")
    public String verification_code;

    public recover_verify(String email, String verification_code) {
        this.email = email;
        this.verification_code = verification_code;
    }
}
