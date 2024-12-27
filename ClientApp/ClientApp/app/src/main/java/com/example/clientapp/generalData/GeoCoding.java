package com.example.clientapp.generalData;

import android.util.Log;

import com.example.clientapp.mainPart.MainAppActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GeoCoding {

    //private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search?q=%s&format=json&addressdetails=1";

//    private static final String URL = "https://api.geocod.io/v1.7/geocode";
//    private static final String ioAPIkey = "eedd6df9f5693d6d9c57c56f37fdde66e76663c";
    private static final String URL = "https://us1.locationiq.com/v1/search?key=";
    private static final String iqAPIkey = "pk.86175167e0b7b02d8554bbea1d11509d";

    public static GeoPoint getCoordinatesByPlaceName(String placeName, MainAppActivity parent, int flag) {
        final GeoPoint[] ans = new GeoPoint[1];
        // Формирование URL с названием места
        //String url = String.format(NOMINATIM_URL, placeName.replace(" ", "+"));
        String url = URL + iqAPIkey + "&q=" + placeName + "&format=json";

        // Создание клиента для HTTP-запросов
        OkHttpClient client = new OkHttpClient();

        // Создание запроса
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Выполнение запроса
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                parent.geoFailure(flag);
                e.printStackTrace(); // Обработка ошибок
                Log.e("GeoCodingFailure", e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws java.io.IOException {
                if (response.isSuccessful()) {
                    try {
                        // Парсим JSON-ответ
                        String responseBody = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseBody);
                        if (jsonArray.length() > 0) {
                            JSONObject firstResult = jsonArray.getJSONObject(0);
                            double latitude = 0.0;
                            double longitude = 0.0;
                            if(firstResult.has("lat"))
                                latitude = firstResult.getDouble("lat");
                            if(firstResult.has("lon"))
                                longitude = firstResult.getDouble("lon");

                            // Выводим координаты или используем их для отображения на карте
                            System.out.println("Latitude: " + latitude);
                            System.out.println("Longitude: " + longitude);

                            // Здесь можно отобразить на карте через OSMDroid:
                            GeoPoint geoPoint = new GeoPoint(latitude, longitude);
                            parent.geoSucsess(flag ,geoPoint);
                            ans[0] = geoPoint;
                        }
                    } catch (Exception e) {
                        Log.e("GeoCodingError", e.getMessage());
                    }
                }
                else {
                    Log.e("GeoCodingUnsuccessful", response.body().string());
                }
            }
        });
        if(ans[0] == null) ans[0] = new GeoPoint(-1.0, -1.0);
        return ans[0];
    }
}
