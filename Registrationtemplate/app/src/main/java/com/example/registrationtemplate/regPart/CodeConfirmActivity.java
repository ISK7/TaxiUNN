package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;
import com.example.registrationtemplate.generalData.App;
import com.example.registrationtemplate.generalData.Status;
import com.example.registrationtemplate.requests.reg_verify;
import com.example.registrationtemplate.requests.change;
import com.example.registrationtemplate.responses.default_success_ans;
import com.example.registrationtemplate.responses.error_login;
import com.example.registrationtemplate.responses.error_recovery;
import com.example.registrationtemplate.responses.error_verify;
import com.example.registrationtemplate.responses.login_ans;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Активность для проверки кода подтверждения
public class CodeConfirmActivity extends AppCompatActivity {

    ImageButton back;
    Button confirm;
    TextView send_again;

    //Кастомная view
    CodeInputView code;
    TextView code_er;

    //Общие для всего приложения настройки
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_code);
        initialization();
    }

    private void back() {
        finish();
    }

    private void sendAgain() {
        tryToConfirm();
    }

    private void enqueueCall(Call<default_success_ans> call) {
        call.enqueue(new Callback<default_success_ans>() {
            @Override
            public void onResponse(@NonNull Call<default_success_ans> call, @NonNull Response<default_success_ans> response) {
                if (response.isSuccessful()) {
                    // Обрабатываем успешный ответ, который вернется как SuccessResponse
                    default_success_ans successResponse = response.body();
                    if (successResponse != null) {
                        // Выполнение логики с данными
                        Log.d("Success", "Message: " + successResponse.getMessage());
                        startPassword();
                    }
                } else {
                    // Обрабатываем ошибку
                    incorrectCode(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<default_success_ans> call, @NonNull Throwable t) {
                // Ошибка сети или что-то другое
                Log.e("Error", Objects.requireNonNull(t.getMessage()));
                code_er.setText(R.string.server_error);
            }
        });
    }
    private boolean tryToConfirm() {
        String email = sharedPreferences.getString("emailAddress","lost_email");
        String code_str = code.getCode();
        if(App.getStatus() == Status.REGISTRATION) {
            reg_verify act = new reg_verify(email,code_str);
            Call<default_success_ans> call = App.getServer().getApi().activateAccount(act);
            enqueueCall(call);
        }
        else {
            change change = new change(email, code_str);
            Call<default_success_ans> call = App.getServer().getApi().changePassword(change);
            enqueueCall(call);
        }
        return true;
    }

    private void incorrectCode(Response<default_success_ans> response) {
        Gson gson = new Gson();
        if(App.getStatus() == Status.REGISTRATION) {
            error_verify errorResponse = null;
            try {
                // Парсим ошибку в зависимости от тела ответа
                String errorBody = response.errorBody().string();
                errorResponse = gson.fromJson(errorBody, error_verify.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (errorResponse != null) {
                // Логируем или обрабатываем ошибку
                String erResponse = errorResponse.getFirstEmailErr();
                if (erResponse.equals("")) {
                    erResponse = errorResponse.getFirstCodeErr();
                }
                Log.e("Error", "Description: " + erResponse);
                code_er.setText(errorResponse.getFirstCodeErr());
            }
        }
    }

    private void startPassword() {
        if(App.getStatus() == Status.REGISTRATION) {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, PasswordActivity.class);
            startActivity(intent);
        }
    }

    private void initialization() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        back = findViewById(R.id.code_back_but);
        back.setOnClickListener(v -> back());

        code = findViewById(R.id.code_c);
        code_er = findViewById(R.id.error_code_c);

        send_again = findViewById(R.id.send_again_c);
        send_again.setOnClickListener(v -> sendAgain());

        confirm = findViewById(R.id.confirm_but_c);
        confirm.setOnClickListener(v -> {
            tryToConfirm();
        });
    }
}
