package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;
import com.example.registrationtemplate.generalData.App;
import com.example.registrationtemplate.generalData.Status;
import com.example.registrationtemplate.requests.login;
import com.example.registrationtemplate.responses.Ans_login;
import com.example.registrationtemplate.responses.Ans_login_er;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Активность для входа в аккаунт
public class LogInActivity extends AppCompatActivity {

    EditText log_in;
    EditText password;
    Button login_but;
    Button back_but;
    TextView to_new_password_but;
    TextView to_reg_but;
    TextView login_er;
    TextView password_er;

    //Общие для всего приложения настройки
    SharedPreferences sharedPreferences;

    //Объект для изменения общих настроек
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        App.setStatus(Status.LOG_IN);
        initialization();
    }

    private boolean tryToLogin() {
        EditText[] fields = {log_in, password};
        TextView[] error_views = {login_er, password_er};
        if(!App.fieldsNotEmpty(fields, error_views))
            return false;
        login log = new login(log_in.getText().toString(),password.getText().toString());
        Call<Ans_login> call = App.getServer().getApi().logInAccount(log);

        call.enqueue(new Callback<Ans_login>() {
            @Override
            public void onResponse(@NonNull Call<Ans_login> call, @NonNull Response<Ans_login> response) {
                if (response.isSuccessful()) {
                    // Обрабатываем успешный ответ, который вернется как SuccessResponse
                    if (response != null) {
                        // Выполнение логики с данными
                        correctLogin(response);
                    }
                } else {
                    // Обрабатываем ошибку
                    incorrectLogin(response);
                }
            }

            @Override
            public void onFailure(Call<Ans_login> call, Throwable t) {
                // Ошибка сети или что-то другое
                Log.e("Error", t.getMessage());
                password_er.setText(R.string.server_error);
            }
        });
        return true;
    }
    private void correctLogin(Response<Ans_login> response) {
        Ans_login successResponse = response.body();
        Log.d("Success", "Message: " + successResponse.getRefresh());
        App.setRefreshToken(successResponse.getRefresh());
        App.setAccessToken(successResponse.getAccess());

        editor.putString("emailAddress", log_in.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.putString("name", getString(R.string.name_not_found));
        editor.commit();

        startMain();
    }
    private void incorrectLogin(Response<Ans_login> response) {
        Gson gson = new Gson();
        Ans_login_er errorResponse = null;

        try {
            // Парсим ошибку в зависимости от тела ответа
            String errorBody = response.errorBody().string();
            errorResponse = gson.fromJson(errorBody, Ans_login_er.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (errorResponse != null) {
            // Логируем или обрабатываем ошибку
            if(errorResponse.getEmail() != null) {
                String error = App.parseError(errorResponse.getEmail()[0]);
                Log.e("Error", "Error: " + errorResponse.getEmail()[0]);
                login_er.setText(error);
            }
            if(errorResponse.getPassword() != null) {
                String error = App.parseError(errorResponse.getPassword()[0]);
                Log.e("Error", "Description: " + errorResponse.getPassword()[0]);
                password_er.setText(error);
            }
        }
        else {
            login_er.setText(R.string.unsupported_er);
        }
    }
    private void startMain() {
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
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void initialization() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        log_in = findViewById(R.id.login_view_l);
        password = findViewById(R.id.password_view_l);

        login_er = findViewById(R.id.error_email_l);
        password_er = findViewById(R.id.error_pass_l);

        login_but = findViewById(R.id.login_but_l);
        login_but.setOnClickListener(v -> {
            tryToLogin();
        });

        back_but = findViewById(R.id.login_back_but);
        back_but.setOnClickListener(v -> back());

        to_new_password_but = findViewById(R.id.forget_password_but);
        to_new_password_but.setOnClickListener(v -> forgetPassword());

        to_reg_but = findViewById(R.id.log_to_reg_but);
        to_reg_but.setOnClickListener(v -> createAccount());
    }

}