package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;
import com.example.registrationtemplate.generalData.App;
import com.example.registrationtemplate.generalData.Status;
import com.example.registrationtemplate.requests.recover;
import com.example.registrationtemplate.requests.recover_verify;
import com.example.registrationtemplate.requests.reg_verify;
import com.example.registrationtemplate.responses.Ans_password_recovery;
import com.example.registrationtemplate.responses.Ans_password_recovery_er;
import com.example.registrationtemplate.responses.Ans_password_recovery_verify;
import com.example.registrationtemplate.responses.Ans_password_recovery_verify_er;
import com.example.registrationtemplate.responses.Ans_register_verify;
import com.example.registrationtemplate.responses.Ans_register_verify_er;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Активность для проверки кода подтверждения
public class CodeConfirmActivity extends AppCompatActivity {

    Button back;
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
        String email = sharedPreferences.getString("temporaryEmail",null);
        if (email == null) {
            Log.e("Error", "Temporary Email is null!");
            code_er.setText(getText(R.string.unsupported_er));
            return;
        }

        recover recover = new recover(email);
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
                    }
                } else {
                    // Обрабатываем ошибку
                    incorrectEmail(response);
                }
            }

            @Override
            public void onFailure(Call<Ans_password_recovery> call, Throwable t) {
                Log.e("Error", t.getMessage());
                code_er.setText(R.string.server_error);
            }
        });
    }

    private void enqueueRegCall(Call<Ans_register_verify> call) {
        call.enqueue(new Callback<Ans_register_verify>() {
            @Override
            public void onResponse(@NonNull Call<Ans_register_verify> call, @NonNull Response<Ans_register_verify> response) {
                if (response.isSuccessful()) {
                    // Обрабатываем успешный ответ, который вернется как SuccessResponse
                    Ans_register_verify successResponse = response.body();
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
            public void onFailure(@NonNull Call<Ans_register_verify> call, @NonNull Throwable t) {
                // Ошибка сети или что-то другое
                Log.e("Error", Objects.requireNonNull(t.getMessage()));
                code_er.setText(R.string.server_error);
            }
        });
    }

    private void enqueueRecCall(Call<Ans_password_recovery_verify> call) {
        call.enqueue(new Callback<Ans_password_recovery_verify>() {
            @Override
            public void onResponse(@NonNull Call<Ans_password_recovery_verify> call, @NonNull Response<Ans_password_recovery_verify> response) {
                if (response.isSuccessful()) {
                    // Обрабатываем успешный ответ, который вернется как SuccessResponse
                    Ans_password_recovery_verify successResponse = response.body();
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
            public void onFailure(@NonNull Call<Ans_password_recovery_verify> call, @NonNull Throwable t) {
                // Ошибка сети или что-то другое
                Log.e("Error", Objects.requireNonNull(t.getMessage()));
                code_er.setText(R.string.server_error);
            }
        });
    }


    private boolean tryToConfirm() {
        String email = sharedPreferences.getString("emailAddress","lost_email");
        String code_str = code.getCode();
        if (code_str.length() != 5) {
            code_er.setText(R.string.no_code_er);
            return false;
        }
        if(App.getStatus() == Status.REGISTRATION) {
            reg_verify act = new reg_verify(email,code_str);
            Call<Ans_register_verify> call = App.getServer().getApi().activateAccount(act);
            enqueueRegCall(call);
        }
        else {
            recover_verify ver = new recover_verify (email, code_str);
            Call<Ans_password_recovery_verify> call = App.getServer().getApi().recoverVerify(ver);
            enqueueRecCall(call);
        }
        return true;
    }


    private void incorrectCode(Object response) {
        Gson gson = new Gson();
        if(App.getStatus() == Status.REGISTRATION) {
            Ans_register_verify_er errorResponse = null;
            try {
                // Парсим ошибку в зависимости от тела ответа
                String errorBody = ((Response<Ans_register_verify_er>)response).errorBody().string();
                errorResponse = gson.fromJson(errorBody, Ans_register_verify_er.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (errorResponse != null) {
                // Логируем или обрабатываем ошибку
                String erResponse = "";
                if(errorResponse.getEmail() != null) {
                    erResponse += errorResponse.getEmail()[0] + "   ";
                    code_er.setText(errorResponse.getEmail()[0]);
                }
                if(errorResponse.getVerification_code() != null) {
                    erResponse += errorResponse.getVerification_code()[0];
                    code_er.setText(errorResponse.getVerification_code()[0]);
                }
                Log.e("Error", "Description: " + erResponse);
            }
            else {
                code_er.setText("error not parsed");
            }
        }
        else {
            Ans_password_recovery_verify_er errorResponse = null;
            try {
                // Парсим ошибку в зависимости от тела ответа
                String errorBody = ((Response<Ans_password_recovery_verify_er>)response).errorBody().string();
                errorResponse = gson.fromJson(errorBody, Ans_password_recovery_verify_er.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (errorResponse != null) {
                // Логируем или обрабатываем ошибку
                String erResponse = "";
                if(errorResponse.getEmail() != null) {
                    erResponse += errorResponse.getEmail()[0] + "   ";
                    code_er.setText(errorResponse.getEmail()[0]);
                }
                if(errorResponse.getVerification_code() != null) {
                    erResponse += errorResponse.getVerification_code()[0];
                    code_er.setText(errorResponse.getVerification_code()[0]);
                }
                Log.e("Error", "Description: " + erResponse);
            }
            else {
                code_er.setText("error not parsed");
            }
        }
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
                Log.e("Error", "Error: " + errorResponse.getEmail()[0]);
                code_er.setText(errorResponse.getEmail()[0]);
            }
        }
        else {
            code_er.setText("error not parsed");
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
