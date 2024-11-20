package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;
import com.example.registrationtemplate.generalData.App;
import com.example.registrationtemplate.generalData.Status;
import com.example.registrationtemplate.requests.login;
import com.example.registrationtemplate.responses.error_login;
import com.example.registrationtemplate.responses.login_ans;
import com.example.registrationtemplate.responses.refresh_ans;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Активность для входа в аккаунт
public class LogInActivity extends AppCompatActivity {

    login_ans  log_ans;

    EditText log_in;
    EditText password;
    Button login_but;
    ImageButton back_but;
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
        EditText[] fields = {log_in, password};
        TextView[] error_views = {login_er, password_er};
        if(!App.fieldsNotEmpty(fields, error_views))
            return false;
        login log = new login(log_in.getText().toString(),password.getText().toString());
        Call<login_ans> call = App.getServer().getApi().logInAccount(log);

        call.enqueue(new Callback<login_ans>() {
            @Override
            public void onResponse(Call<login_ans> call, Response<login_ans> response) {
                if (response.isSuccessful()) {
                    // Обрабатываем успешный ответ, который вернется как SuccessResponse
                    login_ans successResponse = response.body();
                    if (successResponse != null) {
                        // Выполнение логики с данными
                        Log.d("Success", "Message: " + successResponse.getRefresh());
                        App.setRefreshToken(successResponse.getRefresh());
                        App.setAccessToken(successResponse.getAccess());
                        correctLogin();
                    }
                } else {
                    // Обрабатываем ошибку
                    incorrectLogin(response);
                }
            }

            @Override
            public void onFailure(Call<login_ans> call, Throwable t) {
                // Ошибка сети или что-то другое
                Log.e("Error", t.getMessage());
                password_er.setText(R.string.server_error);
            }
        });
        return true;
    }
    private void incorrectLogin(Response<login_ans> response) {
        Gson gson = new Gson();
        error_login errorResponse = null;

        try {
            // Парсим ошибку в зависимости от тела ответа
            String errorBody = response.errorBody().string();
            errorResponse = gson.fromJson(errorBody, error_login.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (errorResponse != null) {
            // Логируем или обрабатываем ошибку
            Log.e("Error", "Error: " + errorResponse.getFirstEmailErr());
            login_er.setText(errorResponse.getFirstEmailErr());
            Log.e("Error", "Description: " + errorResponse.getFirstPasswordErr());
            password_er.setText(errorResponse.getFirstPasswordErr());
        }
    }
    private void correctLogin() {
        //Вносим новые данные в аккаует
        editor.putString("emailAddress", log_in.getText().toString());
        editor.putString("password", passwordToHash(password.getText().toString()));
        editor.putString("name", getName());
        editor.commit();

        App.setAccessToken(log_ans.getAccess());
        App.setRefreshToken(log_ans.getRefresh());

        startMain();
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