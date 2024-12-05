package com.example.registrationtemplate.requests;

import com.google.gson.annotations.SerializedName;

//Класс для запроса на вход
//Вызывается при входе
public class login {
    @SerializedName("email")
    String email;
    @SerializedName("password")
    String password;

    public login(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
