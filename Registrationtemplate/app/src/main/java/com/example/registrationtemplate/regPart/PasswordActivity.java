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
import com.example.registrationtemplate.requests.recover_change;
import com.example.registrationtemplate.responses.Ans_password_recovery_change;
import com.example.registrationtemplate.responses.Ans_password_recovery_change_er;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Активность для ввода пароля и его подтверждения
public class PasswordActivity extends AppCompatActivity {

    Button back;
    EditText newPassword;
    EditText secondPassword;
    TextView newPassword_er;
    TextView secondPassword_er;
    Button done;
    TextView header;

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
        startLog();
    }
    private void startLog() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    private void enqueueCall(Call<Ans_password_recovery_change> call) {
        call.enqueue(new Callback<Ans_password_recovery_change>() {
            @Override
            public void onResponse(@NonNull Call<Ans_password_recovery_change> call, @NonNull Response<Ans_password_recovery_change> response) {
                if (response.isSuccessful()) {
                    // Обрабатываем успешный ответ, который вернется как SuccessResponse
                    Ans_password_recovery_change successResponse = response.body();
                    if (successResponse != null) {
                        // Выполнение логики с данными
                        Log.d("Success", "Message: " + successResponse.getMessage());
                        correctPassword();
                    }
                } else {
                    // Обрабатываем ошибку
                    incorrectPassword(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Ans_password_recovery_change> call, @NonNull Throwable t) {
                // Ошибка сети или что-то другое
                Log.e("Error", Objects.requireNonNull(t.getMessage()));
                secondPassword_er.setText(R.string.server_error);
            }
        });
    }
    private boolean tryToSetPassword() {
        EditText[] fields = {newPassword, secondPassword};
        TextView[] error_views = {newPassword_er, secondPassword_er};
        if(!App.fieldsNotEmpty(fields, error_views))
            return false;
        if(!newPassword.getText().toString().equals(secondPassword.getText().toString())) {
            secondPassword_er.setText(R.string.passwords_not_equal);
            return false;
        }
        String email = sharedPreferences.getString("emailAddress","lost_email");
        String name = sharedPreferences.getString("name",getString(R.string.name_not_found));
        String password = secondPassword.getText().toString();

        recover_change rec = new recover_change(email, password);
        Call<Ans_password_recovery_change> call = App.getServer().getApi().recoverChange(rec);
        enqueueCall(call);

        return true;
    }

    private void correctPassword() {
        editor.putString("password", passwordToHash(secondPassword.getText().toString()));
        editor.commit();

        startMain();
    }

    private void incorrectPassword(Response<Ans_password_recovery_change> response) {
        Gson gson = new Gson();
        Ans_password_recovery_change_er errorResponse = null;

        try {
            // Парсим ошибку в зависимости от тела ответа
            String errorBody = response.errorBody().string();
            errorResponse = gson.fromJson(errorBody, Ans_password_recovery_change_er.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (errorResponse != null) {
            // Логируем или обрабатываем ошибку
            if (errorResponse.getEmail() != null) {
                Log.e("Error", "Description: " + errorResponse.getEmail()[0]);
                secondPassword_er.setText(errorResponse.getEmail()[0]);
            }
            if (errorResponse.getPassword() != null) {
                Log.e("Error", "Description: " + errorResponse.getPassword()[0]);
                secondPassword_er.setText(errorResponse.getPassword()[0]);
            }
        }
        else {
            newPassword_er.setText("error not parsed");
        }
    }

    private void startMain() {
        Intent intent = new Intent(this, MainAppActivity.class);
        startActivity(intent);
    }

    private void initialization() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        Status s = App.getStatus();

        header = findViewById(R.id.password_title_p);
        if(s == Status.LOG_IN)
        {
            header.setText(R.string.set_new_password_head);
        }
        else {
            header.setText(R.string.set_password_head);
        }

        back = findViewById(R.id.password_back_but);
        back.setOnClickListener(v -> back());

        newPassword = findViewById(R.id.password_enter_p);
        secondPassword = findViewById(R.id.password_confirm_p);

        newPassword_er = findViewById(R.id.password_first_error_p);
        secondPassword_er = findViewById(R.id.password_second_error_p);

        done = findViewById(R.id.done_but_p);
        done.setOnClickListener(v -> {
            if(passwordsIsEqual())
                tryToSetPassword();
            else
                secondPassword_er.setText(R.string.passwords_not_equal);
        });
    }

    private boolean passwordsIsEqual() {
        return newPassword.getText().toString().equals(secondPassword.getText().toString());
    }
}
