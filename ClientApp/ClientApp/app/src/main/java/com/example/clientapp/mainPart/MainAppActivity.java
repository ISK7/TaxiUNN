package com.example.clientapp.mainPart;

import static org.osmdroid.tileprovider.util.StorageUtils.getStorage;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.clientapp.BuildConfig;
import com.example.clientapp.R;
import com.example.clientapp.generalData.App;
import com.example.clientapp.generalData.Status;
import com.example.clientapp.regPart.AuthorizationActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.osmdroid.config.Configuration;
import org.osmdroid.config.IConfigurationProvider;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MainAppActivity extends AppCompatActivity {

    Button logout_but;
    MapView map;
    private FusedLocationProviderClient fusedLocationClient;

    //Общие для всего приложения настройки
    SharedPreferences sharedPreferences;

    //Объект для изменения общих настроек
    SharedPreferences.Editor regEditor;

    String from, to, carName, carNumber, offerT, goingT, endT;
    int cost;

    int anchor;
    Fragment currentFragment;

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

    private void init() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        regEditor = sharedPreferences.edit();
        regEditor.putBoolean("isLogged", true);
        regEditor.commit();

        App.Refresh();
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

        map = findViewById(R.id.map);
        map.setMultiTouchControls(true);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Получаем координаты текущего местоположения
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            // Центрируем карту на текущем местоположении
                            GeoPoint currentLocation = new GeoPoint(latitude, longitude);
                            map.getController().setCenter(currentLocation);
                            map.getController().setZoom(15);

                            // Добавляем маркер на карту
                            Marker marker = new Marker(map);
                            marker.setPosition(currentLocation);
                            marker.setIcon(getResources().getDrawable(R.drawable.geo));
                            map.getOverlays().add(marker);
                        }
                    }
                });

        toOffer();
    }

    void makeOffer(String from, String to, int cost){
        App.setStatus(Status.WAITING);
        this.from = from;
        this.to = to;
        this.cost = cost;
        //
        DriverSearchFragment fr = new DriverSearchFragment();
        fr.setMain(this);
        replaceFragment(fr);
    }

    void cancelOffer() {
        App.setStatus(Status.NO_STATUS);
        from = "";
        to = "";
        cost = 0;
        //
        toOffer();
    }
    void canselRideThenAwait() {
        //
        toOffer();
    }
    void driverNotFound() {
        DriverNotFoundFragment fr = new DriverNotFoundFragment();
        fr.setMain(this);
        replaceFragment(fr);
    }
    void driverFound() {
        DriverFoundFragment fr = new DriverFoundFragment();
        fr.setVariables(this,getFrom(), getTo());
        replaceFragment(fr);
    }
    void driverAwaits() {
        DriverIsWaitingFragment fr = new DriverIsWaitingFragment();
        fr.setVariables(this, getFrom(), getTo(), getCarName(), getCarNumber());
        replaceFragment(fr);
    }
    void onRoad() {
        OnRoadFragment fr = new OnRoadFragment();
        fr.setVariables(this, getFrom(), getTo(), getCarName(), getCarNumber(),getOfferT(), getGoingT());
        replaceFragment(fr);
    }
    void roadEnd() {
        RoadIsEndFragment fr = new RoadIsEndFragment();
        fr.setVariable(this, getOfferT(), getGoingT(), getEndT());
        replaceFragment(fr);
    }
    void toOffer() {
        OfferFragment fr = new OfferFragment();
        fr.SetMain(this);
        replaceFragment(fr);
    }

    String getFrom(){
        return from;
    }
    String getTo(){
        return to;
    }
    String getCarName() {return carName;}
    String getCarNumber() {return carNumber;}

    public String getOfferT() {
        return offerT;
    }

    public String getGoingT() {
        return goingT;
    }

    public String getEndT() {
        return endT;
    }

    int getCost(){
        return cost;
    }
    private void replaceFragment(Fragment fragment) {
        currentFragment = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(anchor, fragment)
                .addToBackStack(null)
                .commit();
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