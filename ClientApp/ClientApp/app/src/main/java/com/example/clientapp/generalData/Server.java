package com.example.clientapp.generalData;

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
    TaxiApi headerApi;

    public Server() {
        Retrofit retrofit = createRetrofit();
        api = retrofit.create(TaxiApi.class);
        Retrofit headerRetrofit = createHeaderRetrofit();
        headerApi = headerRetrofit.create(TaxiApi.class);
    }
    //private final static String host = "redis";
    private final static String host = "10.0.2.2";
    private final static Integer port = 8000;
    private final static String link = "http://" + host + ":" + port + "/";


    private OkHttpClient createOkHttpClient(int flag) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //Добавляем интерсептор для добавления ключа к каждому запросу
        builder.addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                final Request original = chain.request();
                final HttpUrl origUrl = original.url();
                final HttpUrl url = origUrl.newBuilder()
                        .build();
                final Request.Builder reqBuilder = original.newBuilder().url(url);
                if(flag == 1) reqBuilder.header("Authorization", "Bearer " + App.getAccessToken());
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
                .client(createOkHttpClient(0))
                .build();
    }
    private Retrofit createHeaderRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(link)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient(1))
                .build();
    }

    public static int getPort() {
        return port;
    }
    public static String getHost() {
        return host;
    }

    public TaxiApi getApi() {
        return api;
    }
    public TaxiApi getHeaderApi() {
        return headerApi;
    }
}
