package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;
import com.example.registrationtemplate.generalData.App;
import com.example.registrationtemplate.generalData.Status;

//Активность для входа в аккаунт
public class LogInActivity extends AppCompatActivity {

    EditText login;
    EditText password;
    Button login_but;
    ImageButton back_but;
    TextView to_new_password_but;
    TextView to_reg_but;

    //Общие для всего приложения настройки
    SharedPreferences sharedPreferences;

    //Объект для изменения общих настроек
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        initialization();
    }

    //Перевод пароля из обычной строки в её хэш-аналог для безопасности
    private String passwordToHash(String input) {
        String result = new String(input);
        return result;
    }

    private String getName() {
        //имя нужно запрашивать у сервера!
        String res = sharedPreferences.getString("name",getString(R.string.name_not_found));
        return res;
    }

    private boolean tryToLogin() {

        return true;
    }
    private void incorrectLogin() {

    }
    private void correctLogin() {
        //Вносим новые данные в аккаует
        editor.putString("emailAddress",login.getText().toString());
        editor.putString("password", passwordToHash(password.getText().toString()));
        editor.putString("name", getName());
        editor.commit();

        startMain();
    }
    private void startMain() {
        App.setStatus(Status.Using);
        Intent intent = new Intent(this, MainAppActivity.class);
        startActivity(intent);
    }
    private void back() {
        finish();
    }

    //Если пользователь забыл пароль
    private void forgetPassword() {
        Intent intent = new Intent(this, NewPassActivity.class);
        startActivity(intent);
    }

    //Если пользователь хочет зарегестрироваться
    private void createAccount() {
        App.setStatus(Status.Registration);
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void initialization() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        login = findViewById(R.id.login_view_l);
        password = findViewById(R.id.password_view_l);

        login_but = findViewById(R.id.login_but_l);
        login_but.setOnClickListener(v -> {
            if (tryToLogin()) {
                correctLogin();
            }
            else incorrectLogin();
        });

        back_but = findViewById(R.id.login_back_but);
        back_but.setOnClickListener(v -> back());

        to_new_password_but = findViewById(R.id.forget_password_but);
        to_new_password_but.setOnClickListener(v -> forgetPassword());

        to_reg_but = findViewById(R.id.log_to_reg_but);
        to_reg_but.setOnClickListener(v -> createAccount());
    }

}