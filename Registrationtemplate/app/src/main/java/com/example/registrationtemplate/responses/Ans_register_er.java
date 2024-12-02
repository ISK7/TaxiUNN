package com.example.registrationtemplate.responses;

import com.google.gson.annotations.SerializedName;

public class Ans_register_er {
    @SerializedName("email")
    String[] email;
    @SerializedName("password")
    String[] password;
    @SerializedName("full_name")
    String[] full_name;

    public String[] getEmail() {
        return email;
    }
    public void setEmail(String[] email) {
        this.email = email;
    }
    public String[] getPassword() {
        return password;
    }
    public void setPassword(String[] password) {
        this.password = password;
    }
    public String[] getFull_name() {
        return full_name;
    }
    public void setFull_name(String[] full_name) {
        this.full_name = full_name;
    }
}
