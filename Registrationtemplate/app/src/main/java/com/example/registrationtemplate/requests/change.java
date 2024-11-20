package com.example.registrationtemplate.requests;

import com.google.gson.annotations.SerializedName;

//запрос на проверку кода
//Вызывается при смене пароля
public class change {
    @SerializedName("email")
    public String email;
    @SerializedName("verification_code")
    public String verification_code;

    public change(String email, String verification_code) {
        this.email = email;
        this.verification_code = verification_code;
    }
}
