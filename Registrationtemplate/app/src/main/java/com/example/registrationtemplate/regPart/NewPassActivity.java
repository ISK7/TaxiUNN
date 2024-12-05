package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;
import com.example.registrationtemplate.generalData.App;
import com.example.registrationtemplate.requests.recover;
import com.example.registrationtemplate.responses.Ans_password_recovery;
import com.example.registrationtemplate.responses.Ans_password_recovery_er;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Активность для подтверждения необходимости нового пароля
public class NewPassActivity extends AppCompatActivity {

    EditText e_mail;
    TextView e_mail_er;
    Button back_but;
    Button sendCode;
    //Общие для всего приложения настройки
    SharedPreferences sharedPreferences;

    //Объект для изменения общих настроек
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_password);
        initialization();
    }

    private boolean tryToSend() {
        EditText[] fields = {e_mail};
        TextView[] error_views = {e_mail_er};
        if(!App.fieldsNotEmpty(fields, error_views))
            return false;

        editor.putString("temporaryEmail", e_mail.getText().toString());
        editor.commit();

        recover recover = new recover(e_mail.getText().toString());
        Call<Ans_password_recovery> call = App.getServer().getApi().recoverPassword(recover);

        call.enqueue(new Callback<Ans_password_recovery>() {
            @Override
            public void onResponse(Call<Ans_password_recovery> call, Response<Ans_password_recovery> response) {
                if (response.isSuccessful()) {
                    // Обрабатываем успешный ответ, который вернется как SuccessResponse
                    Ans_password_recovery successResponse = response.body();
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
            public void onFailure(Call<Ans_password_recovery> call, Throwable t) {
                Log.e("Error", t.getMessage());
                e_mail_er.setText(R.string.server_error);
            }
        });
        return true;
    }

    private void incorrectEmail(Response<Ans_password_recovery> response) {
        Gson gson = new Gson();
        Ans_password_recovery_er errorResponse = null;

        try {
            // Парсим ошибку в зависимости от тела ответа
            String errorBody = response.errorBody().string();
            errorResponse = gson.fromJson(errorBody, Ans_password_recovery_er.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (errorResponse != null) {
            if(errorResponse.getEmail() != null) {
                // Логируем или обрабатываем ошибку
                String error = App.parseError(errorResponse.getEmail()[0]);
                Log.e("Error", "Error: " + errorResponse.getEmail()[0]);
                e_mail_er.setText(error);
            }
        }
        else {
            e_mail_er.setText(R.string.unsupported_er);
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

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
    }
}
