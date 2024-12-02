package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;
import com.example.registrationtemplate.generalData.App;
import com.example.registrationtemplate.generalData.Status;
import com.example.registrationtemplate.requests.register;
import com.example.registrationtemplate.responses.Ans_register;
import com.example.registrationtemplate.responses.Ans_register_er;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Активность для регистрации нового аккаунта
public class RegistrationActivity extends AppCompatActivity {

    Button back;
    EditText first_password, second_password;
    EditText login;
    EditText name;
    TextView first_password_er, second_password_er;
    TextView login_er;
    TextView name_er;
    Button reg_but;
    TextView reg_to_log_but;
    CheckBox politics_check;
    TextView politics_view;

    //Общие для всего приложения настройки
    SharedPreferences sharedPreferences;
    //Объект для изменения общих настроек
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        App.setStatus(Status.REGISTRATION);
        initialization();
    }


    private boolean tryToReg() {
        EditText[] fields = {login, name, first_password, second_password};
        TextView[] error_views = {login_er, name_er, first_password_er, second_password_er};
        if(!App.fieldsNotEmpty(fields, error_views))
            return false;
        if(!first_password.getText().toString().equals(second_password.getText().toString())) {
            second_password_er.setText(R.string.passwords_not_equal);
            return false;
        }

        String req_login = login.getText().toString();
        String req_password = second_password.getText().toString();
        String req_name = name.getText().toString();

        register reg = new register(req_login,
                req_password,
                req_name);

        Call<Ans_register> call = App.getServer().getApi().registerClient(reg);

        call.enqueue(new Callback<Ans_register>() {
            @Override
            public void onResponse(Call<Ans_register> call, Response<Ans_register> response) {
                if(response.isSuccessful()) {
                    correctReg();
                }
                else {
                    incorrectReg(response);
                }
            }

            @Override
            public void onFailure(Call<Ans_register> call, Throwable t) {
                Log.e("Error", t.getMessage());
                second_password_er.setText(R.string.server_error);
            }
        });
        return true;
    }
    private void incorrectReg(Response<Ans_register> response) {
        Gson gson = new Gson();
        Ans_register_er errorResponse = null;

        try {
            // Парсим ошибку в зависимости от тела ответа
            String errorBody = response.errorBody().string();
            errorResponse = gson.fromJson(errorBody, Ans_register_er.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (errorResponse != null) {
            // Логируем или обрабатываем ошибку
            if(errorResponse.getEmail() != null) {
                Log.e("Error", "Error: " + errorResponse.getEmail()[0]);
                login_er.setText(errorResponse.getEmail()[0]);
            }
            if(errorResponse.getPassword() != null) {
                Log.e("Error", "Description: " + errorResponse.getPassword()[0]);
                second_password_er.setText(errorResponse.getPassword()[0]);
            }
        }
        else {
            login_er.setText("error not parsed");
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

        politics_view = findViewById(R.id.politics_text_link_r);
        politics_view.setClickable(true);
        politics_view.setMovementMethod(LinkMovementMethod.getInstance());

        reg_to_log_but = findViewById(R.id.reg_to_log_but);
        reg_to_log_but.setOnClickListener(v -> startLog());
    }
}