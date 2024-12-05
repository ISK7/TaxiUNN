package com.example.registrationtemplate.responses;

import com.google.gson.annotations.SerializedName;

public class Ans_password_recovery_verify_er {
    @SerializedName("email")
    String[] email;
    @SerializedName("verification_code")
    String[] verification_code;
    public String[] getEmail() {
        return email;
    }
    public void setEmail(String[] email) {
        this.email = email;
    }
    public String[] getVerification_code() {
        return verification_code;
    }
    public void setVerification_code(String[] verification_code) {
        this.verification_code = verification_code;
    }
}
