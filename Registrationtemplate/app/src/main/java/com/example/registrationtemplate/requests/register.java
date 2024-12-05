package com.example.registrationtemplate.requests;

import com.google.gson.annotations.SerializedName;

//Класс для запроса на регистрацию пользователя.
//Вызывается при согласии на регистрацию
public class register {
    @SerializedName("email")
    String email;
    @SerializedName("password")
    String password;
    @SerializedName("full_name")
    String full_name;

    public register(String email, String password, String full_name) {
        this.email = email;
        this.password = password;
        this.full_name = full_name;
    }
}
