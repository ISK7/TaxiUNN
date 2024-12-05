package com.example.registrationtemplate.generalData;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Класс для работы с сервером
public class Server {

    TaxiApi api;

    public Server() {
        Retrofit retrofit = createRetrofit();
        api = retrofit.create(TaxiApi.class);
    }
    //private final static String host = "redis";
    private final static String host = "10.0.2.2";
    private final static Integer port = 8000;
    private final static String link = "http://" + host + ":" + port + "/";


    private OkHttpClient createOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //Добавляем интерсептор для добавления ключа к каждому запросу
        builder.addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                final Request original = chain.request();
                final HttpUrl origUrl = original.url();
                final HttpUrl url = origUrl.newBuilder()
                        .addQueryParameter("api_key", App.getAccessToken())
                        .build();
                final Request.Builder reqBuilder = original.newBuilder().url(url);
                final Request request = reqBuilder.build();
                return chain.proceed(request);
            }
        });

        //Это для вывода логов
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);

        return builder.build();
    }

    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(link)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build();
    }

    public static int getPort() {
        return port;
    }

    public TaxiApi getApi() {
        return api;
    }
}
