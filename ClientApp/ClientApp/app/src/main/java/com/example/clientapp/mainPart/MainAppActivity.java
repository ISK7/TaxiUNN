package com.example.clientapp.mainPart;

import static android.os.SystemClock.sleep;
import static org.osmdroid.tileprovider.util.StorageUtils.getStorage;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.clientapp.BuildConfig;
import com.example.clientapp.R;
import com.example.clientapp.generalData.App;
import com.example.clientapp.generalData.GeoCoding;
import com.example.clientapp.generalData.Status;
import com.example.clientapp.regPart.AuthorizationActivity;
import com.example.clientapp.requests.price_list;
import com.example.clientapp.responses.Ans_prices;
import com.example.clientapp.responses.price;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.osmdroid.config.Configuration;
import org.osmdroid.config.IConfigurationProvider;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainAppActivity extends AppCompatActivity {

    Button logout_but;
    MapView map;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    //Общие для всего приложения настройки
    SharedPreferences sharedPreferences;

    //Объект для изменения общих настроек
    SharedPreferences.Editor regEditor;

    String from, to, carName, carNumber, offerT, goingT, endT,
            name;
    float cost;
    GeoPoint from_p, to_p;

    int anchor, low_anchor;
    Fragment currentFragment;
    double latitude = 56.28716633464728;
    double longitude = 44.026283895867685;
    double latitude_t = 56.29716633464728;
    double longitude_t = 44.036283895867685;
    Marker marker; int markerInd;
    Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IConfigurationProvider provider = Configuration.getInstance();
        provider.setUserAgentValue(BuildConfig.APPLICATION_ID);
        provider.setOsmdroidBasePath(getStorage());
        provider.setOsmdroidTileCache(getStorage());
        setContentView(R.layout.map_screen);

        App.setStatus(Status.USING);
        init();
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.this_permission_is_required))
                    .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                        ActivityCompat.requestPermissions(MainAppActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        ActivityCompat.requestPermissions(MainAppActivity.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    })
                    .setNegativeButton(getString(R.string.close), (dialog, which) -> {
                        dialog.dismiss();
                        System.exit(0);
                    })
                    .show();
        }
    }
    private void tryGeo() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                Log.e("GeoSuccess", latitude + " : " + longitude);
                            } else {
                                Log.e("GeoFailure", "Can't find location");
                            }
                        }
                    });
        } catch (SecurityException ex) {
            Log.e("GeoError", ex.getMessage());
        }
    }
    private void init() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        App.setMain(this);
        regEditor = sharedPreferences.edit();
        regEditor.putBoolean("isLogged", true);
        regEditor.commit();

        mainHandler = new Handler(Looper.getMainLooper());

        logout_but = findViewById(R.id.log_out_but);
        logout_but.setOnClickListener(v -> {
            regEditor.putBoolean("isLogged", false);
            regEditor.commit();
            //do smth
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(fragment)
                            .commit();
                }
            }

            Intent intent = new Intent(v.getContext(), AuthorizationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        anchor = R.id.fragment_anchor;
        low_anchor = R.id.lower_fragment_anchor;

        map = findViewById(R.id.map);
        map.setMultiTouchControls(true);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getController().setZoom(15);
        marker = new Marker(map);
        marker.setIcon(getResources().getDrawable(R.drawable.geo));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    // Обработка обновленного местоположения
                }
            }
        };

        requestPermissions();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                while(ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    sleep(10);
                }
            }
        }).start();

        toOffer();
    }

    private void centerAtUser(double lat, double lon) {
        map.getController().setCenter(new GeoPoint(lat, lon));
    }
    private void setLocation(double lat, double lon) {
        GeoPoint currentLocation = new GeoPoint(lat, lon);
        if(map.getMapCenter().equals(new GeoPoint(0, 0))) {
            map.getController().setCenter(currentLocation);
        }
        // Добавляем маркер на карту
        marker.setPosition(currentLocation);
        if(map.getOverlays().contains(marker)) {
            map.getOverlays().set(markerInd,marker);
        }
        else {
            map.getOverlays().add(marker);
            markerInd = map.getOverlays().indexOf(marker);
        }
    }
    void tryOffer(String from, String to, String name, float cost) {
        App.setStatus(Status.WAITING);
        this.from = from;
        this.to = to;
        this.cost = cost;
        this.name = name;
        from_p = GeoCoding.getCoordinatesByPlaceName(from, this, 0);
        sleep(50);
        to_p = GeoCoding.getCoordinatesByPlaceName(to, this, 1);

        while(from_p == null || to_p == null) {
            sleep(10);
        }

        makeOffer();
    }
    public void geoFailure(int flag) {
        switch (flag) {
            case 1 : {
                ((OfferFragment)currentFragment).setToEr(getString(R.string.write_correct_location));
                return;
            }
            case 0 : {
                ((OfferFragment)currentFragment).setFromEr(getString(R.string.write_correct_location));
                return;
            }
        }
    }

    public void geoSucsess(int flag, GeoPoint point) {
        switch (flag) {
            case 0 : {from_p = point; break;}
            case 1 : {to_p = point; break;}
        }
    }
    public void makeOffer(){
        if(to_p == null) {
            geoFailure(1);
            return;
            //to_p = new GeoPoint(latitude + 0.001,longitude + 0.001);
        }
        if(from_p == null) {
            geoFailure(0);
            return;
            //from_p = new GeoPoint(latitude, longitude);
        }
//
//        latitude = from_p.getLatitude();
//        longitude = from_p.getLongitude();
//        App.sendOrder((float) from_p.getLongitude(), (float) from_p.getLatitude(),
//                (float) to_p.getLongitude(), (float) to_p.getLatitude(),
//                name, cost);
        App.sendOrder((float) longitude, (float) latitude,
                (float) longitude_t, (float) latitude_t,
                name, cost);
        setLocation(latitude, longitude);

        DriverSearchFragment fr = new DriverSearchFragment();
        fr.setMain(this);
        replaceLowerFragment(fr);
    }

    void cancelOffer() {
        App.setStatus(Status.USING);
        from = "";
        to = "";
        cost = 0;

        App.sendCancel();
        toOffer();
    }
    void canselRideThenAwait() {
        //
        App.sendCancel();
        toOffer();
    }
    public void driverNotFound() {
        MainAppActivity act = this;
        DriverNotFoundFragment fr = new DriverNotFoundFragment();
        fr.setMain(act);
        replaceFragment(fr);
    }
    public void driverFound(String name, String number) {
        MainAppActivity act = this;
        DriverFoundFragment fr = new DriverFoundFragment();
        replaceFragment(fr);
        fr.setVariables(act, getFrom(), getTo(), name, number);
    }
    public void driverAwaits() {
        MainAppActivity act = this;
        DriverIsWaitingFragment fr = new DriverIsWaitingFragment();
        replaceLowerFragment(fr);
        fr.setVariables(act, getFrom(), getTo(), getCarName(), getCarNumber());
    }
    public void onRoad() {
        MainAppActivity act = this;
        OnRoadFragment fr = new OnRoadFragment();
        replaceFragment(fr);
        fr.setVariables(act, getFrom(), getTo(), getCarName(), getCarNumber(), getOfferT(), getGoingT());
    }
    public void roadEnd() {
        MainAppActivity act = this;
        RoadIsEndFragment fr = new RoadIsEndFragment();
        replaceFragment(fr);
        fr.setVariable(act, getOfferT(), getGoingT(), getEndT());
    }
    void toOffer() {
        App.beginConnection();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                boolean flag = false;
                while(!flag) {
                    flag = get_prices((float) longitude, (float) latitude, (float) longitude_t, (float) latitude_t);
                    sleep(50);
                }
            }
        });
        thread.start();
        OfferFragment fr = new OfferFragment();
        fr.SetMain(this);
        replaceLowerFragment(fr);
    }

    String getFrom(){
        return from;
    }
    String getTo(){
        return to;
    }
    String getCarName() {return carName;}
    String getCarNumber() {return carNumber;}

    public void setOfferT(String offerT) {
        this.offerT = offerT;
    }

    public void setGoingT(String goingT) {
        this.goingT = goingT;
    }

    public void setEndT(String endT) {
        this.endT = endT;
    }

    public String getOfferT() {
        return offerT;
    }

    public String getGoingT() {
        return goingT;
    }

    public String getEndT() {
        return endT;
    }

    float getCost(){
        return cost;
    }
    private void replaceFragment(Fragment fragment) {
        if(currentFragment != null)
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
        currentFragment = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(anchor, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    private void replaceLowerFragment(Fragment fragment) {
        if(currentFragment != null)
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
        currentFragment = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(low_anchor, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    boolean get_prices(float long_f, float lat_f, float long_t, float lat_t) {
        price_list price_req = new price_list(long_f, lat_f, long_t, lat_t);
        Call<Ans_prices> call = App.getServer().getHeaderApi().getPrices(price_req);
        boolean[] ans = {true};
        call.enqueue(new Callback<Ans_prices>() {
            @Override
            public void onResponse(@NonNull Call<Ans_prices> call, @NonNull Response<Ans_prices> response) {
                if (response.isSuccessful()) {
                    // Обрабатываем успешный ответ, который вернется как SuccessResponse
                    if (response != null) {
                        // Выполнение логики с данными
                        price[] prices = response.body().getPrice_list();
                        removeNoTariff();
                        for(price p: prices) {
                            addTariff(p.getPrice(), p.getName());
                        }
                    }
                } else {
                    // Обрабатываем ошибку
                    ans[0] = false;
                }
            }

            @Override
            public void onFailure(Call<Ans_prices> call, Throwable t) {
                // Ошибка сети или что-то другое
                Log.e("Error", t.getMessage());
                addTariff(0.0f, getString(R.string.no_tariffs));
            }
        });
        return ans[0];
    }

    void removeNoTariff() {
        ((OfferFragment)currentFragment).delNoTariffSign();
    }
    void addTariff(float price, String name) {
        ((OfferFragment)currentFragment).addTariff(price,name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
    @Override
    protected void onResume() {
        super.onResume();
        map.onResume(); // Это необходимо для корректной работы карты
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause(); // Это необходимо для корректной работы карты
    }
}