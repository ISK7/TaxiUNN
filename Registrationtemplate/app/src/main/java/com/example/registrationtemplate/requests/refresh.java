package com.example.registrationtemplate.requests;

import com.google.gson.annotations.SerializedName;

//Запрос на обновление токена.
//Вызывается для обновления токена access
public class refresh {
    @SerializedName("refresh")
    public String refresh;

    public refresh(String refresh) {
        this.refresh = refresh;
    }
}
