package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;

//Активность для ввода пароля и его подтверждения
public class PasswordActivity extends AppCompatActivity {

    ImageButton back;
    EditText newPassword;
    EditText secondPassword;
    Button done;

    //Общие для всего приложения настройки
    SharedPreferences sharedPreferences;

    //Объект для изменения общих настроек
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password);
        initialization();
    }

    private String passwordToHash(String input) {
        String result = new String(input);
        return result;
    }

    private void back() {
        finish();
    }

    private boolean tryToSetPassword() {
        return true;
    }

    private void correctPassword() {
        editor.putString("password", passwordToHash(secondPassword.getText().toString()));
        editor.commit();

        startMain();
    }

    private void incorrectPassword() {
    }

    private void startMain() {
        Intent intent = new Intent(this, MainAppActivity.class);
        startActivity(intent);
    }

    private void initialization() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        back = findViewById(R.id.password_back_but);
        back.setOnClickListener(v -> back());

        newPassword = findViewById(R.id.password_enter_p);
        secondPassword = findViewById(R.id.password_confirm_p);

        done = findViewById(R.id.done_but_p);
        done.setOnClickListener(v -> {
            if(tryToSetPassword())
                correctPassword();
            else incorrectPassword();
        });
    }
}
