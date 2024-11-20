package com.example.registrationtemplate.responses;

//Класс для ответа на запрос login
//Содержит access токен, необходимый для каждого запроса, и refresh токен, который необходим, чтобы его обновлять
public class login_ans {
    String access;
    String refresh;

    public login_ans(String access, String refresh) {
        this.access = access;
        this.refresh = refresh;
    }

    public String getAccess() {
        return access;
    }

    public String getRefresh() {
        return refresh;
    }
}
