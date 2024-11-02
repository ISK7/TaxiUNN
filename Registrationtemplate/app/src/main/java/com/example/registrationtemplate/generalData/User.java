package com.example.registrationtemplate.generalData;

//Класс пользователя - содержит всю информацию о нём. Создан для получения данных с сервера
public class User {
    String name;
    String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
