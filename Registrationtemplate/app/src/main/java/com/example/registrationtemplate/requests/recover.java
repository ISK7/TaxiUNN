package com.example.registrationtemplate.requests;

import com.google.gson.annotations.SerializedName;

//Запрос на обновление пароля
//Вызывается после ввода e-mail на соответствующем экране
public class recover {

    @SerializedName("email")
    String email;

    public recover(String email) {
        this.email = email;
    }
}
