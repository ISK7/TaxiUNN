package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;

/*
Стартовая активность проекта.
В зависимости от наличия регистрации в предыдущих сессиях
либо пересылает на основной экран либо начинает авторизацию
*/
public class AuthorizationActivity extends AppCompatActivity {


    Button yesBut;
    Button logBut;

    //Общие для всего приложения настройки
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authoriz_offer);

        initialize();
    }

    private boolean isAuthorized() {
        return sharedPreferences.getBoolean("isLogged", false);
    }

    private void startReg() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void startLog() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
    private void startMain() {
        Intent intent = new Intent(this, MainAppActivity.class);
        startActivity(intent);
    }

    private void initialize() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //проверка идёт сразу, чтобы лишний раз не загружать объекты
        if(isAuthorized()) startMain();

        logBut = findViewById(R.id.go_to_log_but);
        logBut.setOnClickListener(v -> startLog());

        yesBut = findViewById(R.id.go_to_reg_but);
        yesBut.setOnClickListener(v -> startReg());
    }
}