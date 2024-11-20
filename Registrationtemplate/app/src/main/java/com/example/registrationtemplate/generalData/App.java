package com.example.registrationtemplate.generalData;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.registrationtemplate.regPart.AuthorizationActivity;
import com.example.registrationtemplate.regPart.MainAppActivity;
import com.example.registrationtemplate.requests.refresh;
import com.example.registrationtemplate.responses.refresh_ans;

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
    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        //Общие для всего приложения настройки
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
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

        Call<refresh_ans> call = getServer().getApi().refreshToken(refresh);
        call.enqueue(new Callback<refresh_ans>() {
            @Override
            public void onResponse(Call<refresh_ans> call, Response<refresh_ans> response) {
                if (response.isSuccessful()) {
                    // Обрабатываем успешный ответ, который вернется как SuccessResponse
                    refresh_ans successResponse = response.body();
                    if (successResponse != null) {
                        // Выполнение логики с данными
                        Log.d("Success", "Message: " + successResponse.getRefresh_token());
                        setAccessToken(successResponse.getRefresh_token());
                    }
                } else {
                    // Обрабатываем ошибку
                    Log.d("Success", "Message: " + "Refresh field is required.");
                    //Доделать!
                }
            }

            @Override
            public void onFailure(Call<refresh_ans> call, Throwable t) {
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
}
