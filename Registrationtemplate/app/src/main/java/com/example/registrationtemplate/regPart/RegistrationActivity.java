package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;
import com.example.registrationtemplate.generalData.App;
import com.example.registrationtemplate.generalData.Status;
import com.example.registrationtemplate.requests.register;
import com.example.registrationtemplate.responses.default_success_ans;
import com.example.registrationtemplate.responses.error_login;
import com.example.registrationtemplate.responses.error_reg;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Активность для регистрации нового аккаунта
public class RegistrationActivity extends AppCompatActivity {

    ImageButton back;
    EditText first_password, second_password;
    EditText login;
    EditText name;
    TextView first_password_er, second_password_er;
    TextView login_er;
    TextView name_er;
    Button reg_but;
    TextView reg_to_log_but;

    //Общие для всего приложения настройки
    SharedPreferences sharedPreferences;
    //Объект для изменения общих настроек
    SharedPreferences.Editor editor;
    CheckBox politics_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        App.setStatus(Status.REGISTRATION);
        initialization();
    }

//    private boolean fieldsNotEmpty() {
//        EditText[] fields = {login, name, first_password, second_password};
//        TextView[] error_views = {login_er, name_er, first_password_er, second_password_er};
//    }
    private boolean tryToReg() {
        //if(!fieldsNotEmpty())
        //    return false;
        register reg = new register(login.getText().toString(),
                second_password.getText().toString(),
                name.getText().toString());

        Call<default_success_ans> call = App.getServer().getApi().registerClient(reg);

        call.enqueue(new Callback<default_success_ans>() {
            @Override
            public void onResponse(Call<default_success_ans> call, Response<default_success_ans> response) {
                if(response.isSuccessful()) {
                    correctReg();
                }
                else {
                    incorrectReg(response);
                }
            }

            @Override
            public void onFailure(Call<default_success_ans> call, Throwable t) {
                Log.e("Error", t.getMessage());
                second_password_er.setText(R.string.server_error);
            }
        });
        return true;
    }
    private void incorrectReg(Response<default_success_ans> response) {
        Gson gson = new Gson();
        error_reg errorResponse = null;

        try {
            // Парсим ошибку в зависимости от тела ответа
            String errorBody = response.errorBody().string();
            errorResponse = gson.fromJson(errorBody, error_reg.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (errorResponse != null) {
            // Логируем или обрабатываем ошибку
            Log.e("Error", "Error: " + errorResponse.getFirstEmailErr());
            login_er.setText(errorResponse.getFirstEmailErr());
            Log.e("Error", "Description: " + errorResponse.getFirstPasswordErr());
            second_password_er.setText(errorResponse.getFirstPasswordErr());
        }
    }
    private void correctReg() {
        editor.putString("emailAddress",login.getText().toString());
        editor.putString("name", name.getText().toString());
        editor.apply();

        trySendCode();
    }

    //Переход на следующий этап регистрации
    private void trySendCode() {
            Intent intent = new Intent(this, CodeConfirmActivity.class);
            startActivity(intent);
    }

    //Если пользователь хочет войти в существующий аккаунт
    private void startLog() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    private void back() {
        finish();
    }

    //Обрабатывает изменение чекбокса, проверяющего согласие с политикой конфиденциальности
    private void checkChange() {
        reg_but.setEnabled(!reg_but.isEnabled());
        if(reg_but.isEnabled())
            reg_but.setBackground(getDrawable(R.drawable.custom_but_blue));
        else
            reg_but.setBackground(getDrawable(R.drawable.custom_but_blue_inactive));
    }

    private void initialization() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        back = findViewById(R.id.reg_back_but);
        back.setOnClickListener(v -> back());

        first_password = findViewById(R.id.password_enter_r);
        second_password = findViewById(R.id.password_confirm_r);
        login = findViewById(R.id.login_view_r);
        name = findViewById(R.id.name_view_r);

        first_password_er = findViewById(R.id.password_first_error_r);
        second_password_er = findViewById(R.id.password_second_error_r);
        login_er = findViewById(R.id.log_in_error_r);
        name_er = findViewById(R.id.name_error_r);

        reg_but = findViewById(R.id.reg_but_r);
        reg_but.setOnClickListener(v -> {
            tryToReg();
        });

        politics_check = findViewById(R.id.politics_check_box_r);
        politics_check.setOnClickListener(v -> checkChange());

        reg_to_log_but = findViewById(R.id.reg_to_log_but);
        reg_to_log_but.setOnClickListener(v -> startLog());
    }
}