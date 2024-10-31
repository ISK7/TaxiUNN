package com.example.registrationtemplate.generalData;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.registrationtemplate.regPart.AuthorizationActivity;
import com.example.registrationtemplate.regPart.MainAppActivity;

/*
Стартовый класс приложения.
В зависимости от наличия регистрации в предыдущих сессиях
либо пересылает на основной экран либо начинает авторизацию
*/
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        //Общие для всего приложения настройки
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //проверка идёт сразу, чтобы лишний раз не загружать объекты
        if(sharedPreferences.getBoolean("isLogged", false))
            startMain();
        else
            startAutho();
    }
    private void startMain() {
        Intent intent = new Intent(this, MainAppActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void startAutho() {
        Intent intent = new Intent(this, AuthorizationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
