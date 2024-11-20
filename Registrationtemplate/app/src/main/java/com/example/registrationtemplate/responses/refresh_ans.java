package com.example.registrationtemplate.responses;

//Класс для ответа на запрос refresh
//Содержит токен access, необходимый для каждого запроса после регистрации

public class refresh_ans {
    String refresh_token;

    public refresh_ans(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }
}
