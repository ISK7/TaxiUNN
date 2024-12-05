package com.example.registrationtemplate.generalData;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.registrationtemplate.R;
import com.example.registrationtemplate.regPart.AuthorizationActivity;
import com.example.registrationtemplate.regPart.MainAppActivity;
import com.example.registrationtemplate.requests.refresh;
import com.example.registrationtemplate.responses.Ans_refresh;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
Стартовый класс приложения.
В зависимости от наличия регистрации в предыдущих сессиях
либо пересылает на основной экран либо начинает авторизацию
*/
public class App extends Application {
    private static Status status;
    private static String AccessToken;
    private static String RefreshToken;
    private static Server server;

    private static App instance;
    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        //Общие для всего приложения настройки
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        server = new Server();

        instance = this;

        setAccessToken("{{token}}");
        //проверка идёт сразу, чтобы лишний раз не загружать объекты
        if(sharedPreferences.getBoolean("isLogged", false)) {
            startMain();
        }
        else {
            startAutho();
        }
    }
    private void startMain() {
        Intent intent = new Intent(this, MainAppActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void startAutho() {
        Intent intent = new Intent(this, AuthorizationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    static String getAccessToken() {
        return AccessToken;
    }

    public static void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    static String getRefreshToken() {
        return RefreshToken;
    }

    public static void setRefreshToken(String refreshToken) {
        RefreshToken = refreshToken;
    }

    public static Server getServer() {
        return server;
    }
    public static void Refresh() {
        refresh refresh = new refresh(RefreshToken);

        Call<Ans_refresh> call = getServer().getApi().refreshToken(refresh);
        call.enqueue(new Callback<Ans_refresh>() {
            @Override
            public void onResponse(Call<Ans_refresh> call, Response<Ans_refresh> response) {
                if (response.isSuccessful()) {
                    // Обрабатываем успешный ответ, который вернется как SuccessResponse
                    Ans_refresh successResponse = response.body();
                    if (successResponse != null) {
                        // Выполнение логики с данными
                        Log.d("Success", "Message: " + successResponse.getAccess());
                        setAccessToken(successResponse.getAccess());
                    }
                } else {
                    // Обрабатываем ошибку
                    Log.d("Success", "Message: " + "Refresh field is required.");
                    //Доделать!
                }
            }

            @Override
            public void onFailure(Call<Ans_refresh> call, Throwable t) {
                // Ошибка сети или что-то другое
                Log.e("Error", t.getMessage());
            }
        });
    }

    public static void setStatus(Status s) {
        status = s;
    }

    public static Status getStatus() {
        return status;
    }
    public static boolean fieldsNotEmpty(EditText[] fields, TextView[] error_views) {
        boolean ans = true;
        for (int i = 0; i < fields.length; i++) {
            if (TextUtils.isEmpty(fields[i].getText().toString())) {
                error_views[i].setText(R.string.empty_field_er);
                error_views[i].setTextAppearance(R.style.CustomTextNormalRed);
                ans = false;
            } else {
                error_views[i].setText("");
            }
        }
        return ans;
    }

    private static int checkPassword(String password) {
        if (password.length() < 8) {
            return R.string.password_too_short;
        }
        return -1;
    }

    public static boolean isPasswordValid(TextView passwordView, TextView errorView) {
        int ans = checkPassword(passwordView.getText().toString());
        if(ans == -1) {
            errorView.setTextAppearance(R.style.CustomTextSmallGrey);
            errorView.setText(R.string.password_must);
            return true;
        }
        errorView.setTextAppearance(R.style.CustomTextNormalRed);
        errorView.setText(ans);
        return false;
    }

    public static String parseError(String error) {
        if(instance.getString(R.string.exist_account_er).equals(error)) {
            return instance.getString(R.string.exist_account);
        } else
        if(instance.getString(R.string.not_exist_account_er).equals(error)) {
            return instance.getString(R.string.not_exist_account);
        } else
        if(instance.getString(R.string.no_field_er).equals(error)) {
            return instance.getString(R.string.no_field);
        } else
        if(instance.getString(R.string.email_not_found_er).equals(error)) {
            return instance.getString(R.string.email_not_found);
        } else
        if(instance.getString(R.string.incorrect_code_er).equals(error)) {
            return instance.getString(R.string.incorrect_code);
        } else
        if(instance.getString(R.string.incorrect_password_er).equals(error)) {
            return instance.getString(R.string.incorrect_password);
        } else
        if(instance.getString(R.string.try_again_er).equals(error)) {
            return instance.getString(R.string.try_again);
        }
        if(instance.getString(R.string.not_valid_email_er).equals(error)) {
            return instance.getString(R.string.not_valid_email);
        }
        if(instance.getString(R.string.not_active_code_er).equals(error)) {
            return instance.getString(R.string.not_active_code);
        }
        return instance.getString(R.string.unsupported_er);
    }
}
