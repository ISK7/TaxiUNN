package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;
import com.example.registrationtemplate.generalData.App;
import com.example.registrationtemplate.requests.recover;
import com.example.registrationtemplate.responses.default_success_ans;
import com.example.registrationtemplate.responses.error_login;
import com.example.registrationtemplate.responses.error_recovery;
import com.example.registrationtemplate.responses.login_ans;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Активность для подтверждения необходимости нового пароля
public class NewPassActivity extends AppCompatActivity {

    EditText e_mail;
    TextView e_mail_er;
    ImageButton back_but;
    Button sendCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_password);
        initialization();
    }

    private boolean tryToSend() {
        recover recover = new recover(e_mail.getText().toString());
        Call<default_success_ans> call = App.getServer().getApi().recoverPassword(recover);

        call.enqueue(new Callback<default_success_ans>() {
            @Override
            public void onResponse(Call<default_success_ans> call, Response<default_success_ans> response) {
                if (response.isSuccessful()) {
                    // Обрабатываем успешный ответ, который вернется как SuccessResponse
                    default_success_ans successResponse = response.body();
                    if (successResponse != null) {
                        // Выполнение логики с данными
                        Log.d("Success", "Message: " + successResponse.getMessage());
                        sendCode();
                    }
                } else {
                    // Обрабатываем ошибку
                    incorrectEmail(response);
                }
            }

            @Override
            public void onFailure(Call<default_success_ans> call, Throwable t) {
                Log.e("Error", t.getMessage());
                e_mail_er.setText(R.string.server_error);
            }
        });
        return true;
    }

    private void incorrectEmail(Response<default_success_ans> response) {
        Gson gson = new Gson();
        error_recovery errorResponse = null;

        try {
            // Парсим ошибку в зависимости от тела ответа
            String errorBody = response.errorBody().string();
            errorResponse = gson.fromJson(errorBody, error_recovery.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (errorResponse != null) {
            // Логируем или обрабатываем ошибку
            Log.e("Error", "Error: " + errorResponse.getFirstEmailErr());
            e_mail_er.setText(errorResponse.getFirstEmailErr());
        }
    }

    private void sendCode() {
        Intent intent = new Intent(this, CodeConfirmActivity.class);
        startActivity(intent);
    }
    private void back() {
        finish();
    }

    private void initialization() {
        e_mail = findViewById(R.id.log_in_view_n);

        e_mail_er = findViewById(R.id.error_email_n);

        sendCode = findViewById(R.id.send_but_n);
        sendCode.setOnClickListener(v -> {
            tryToSend();
        });

        back_but = findViewById(R.id.newp_back_but);
        back_but.setOnClickListener(v -> back());
    }
}
