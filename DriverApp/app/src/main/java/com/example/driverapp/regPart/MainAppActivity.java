package com.example.driverapp.regPart;

import static android.os.SystemClock.sleep;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.driverapp.R;
import com.example.driverapp.generalData.App;
import com.example.driverapp.generalData.Status;
import com.example.driverapp.generalData.WebSocketManager;
import com.example.driverapp.sockets.Cancel;
import com.example.driverapp.sockets.Driver_on_site;
import com.example.driverapp.sockets.Driver_on_site_to;
import com.example.driverapp.sockets.Driver_on_the_way;
import com.example.driverapp.sockets.Error_resp;
import com.example.driverapp.sockets.Find_order;
import com.example.driverapp.sockets.Trip_beginning;
import com.example.driverapp.sockets.Trip_ending;
import com.example.driverapp.sockets.possible_order;
import com.example.driverapp.sockets.possible_order_from;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.net.URISyntaxException;

public class MainAppActivity extends AppCompatActivity {

    Button logout_but;

    //Общие для всего приложения настройки
    SharedPreferences sharedPreferences;
    private FusedLocationProviderClient fusedLocationClient;

    //Объект для изменения общих настроек
    SharedPreferences.Editor regEditor;
    TextView order, on_way, on_site, begin, end;
    Button find, accept, decline, on_site_b, begin_b, end_b;
    WebSocketManager socket;
    double longitude = 56.295015, latitude = 44.046304;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_app);

        App.setMain(this);

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
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
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
    public void setInfo(String msg) {
        Gson gson = new Gson();
        Log.e("msg",msg);
        TextView[] neededView = {order};
        String[] data = {""};
        if(msg.contains(possible_order_from.getMessageType())) {
            neededView[0] = order;
            possible_order_from po = gson.fromJson(msg, possible_order_from.class);
            data[0] = po.getFrom()[0] + " " + po.getFrom()[0] + "\n"
                    + po.getTo()[0] + " " + po.getTo()[1] + "\n"
                    + po.getFare();
        } else if (msg.contains(Driver_on_the_way.getMessage_type())) {
            neededView[0] = on_way;
            Driver_on_the_way dw = gson.fromJson(msg, Driver_on_the_way.class);
            Log.e("onWay", msg);
            data[0] = dw.getStartTime();
        } else if (msg.contains(Driver_on_site.getMessage_type())) {
            neededView[0] = on_site;
            Driver_on_site dos = gson.fromJson(msg, Driver_on_site.class);
            data[0] = "on site";
        } else if (msg.contains(Trip_beginning.getMessage_type())) {
            neededView[0] = begin;
            Trip_beginning tb = gson.fromJson(msg, Trip_beginning.class);
            data[0] = tb.getBeginTime();
        } else if (msg.contains(Trip_ending.getMessage_type())) {
            neededView[0] = end;
            Trip_ending te = gson.fromJson(msg, Trip_ending.class);
            data[0] = te.getEndTime();
        } else if (msg.contains(Cancel.getMessage_type())) {
            neededView[0] = order;
            data[0] = "canceled";
            socket.disconnect();
        } else if (msg.contains(Error_resp.getMessage_type())) {
            neededView[0] = order;
            data[0] = "Error";
            socket.disconnect();
        } else {
            neededView[0] = order;
            data[0] = "Can't parse";
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                neededView[0].setText(data[0]);
            }
        });
    }

    private void init() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        regEditor = sharedPreferences.edit();
        regEditor.putBoolean("isLogged",true);
        regEditor.commit();

        App.Refresh();
        logout_but = findViewById(R.id.log_out_but);
        logout_but.setOnClickListener(v -> {
            regEditor.putBoolean("isLogged",false);
            regEditor.commit();
            //do smth

            Intent intent = new Intent(v.getContext(), AuthorizationActivity.class);
            startActivity(intent);
        });

        order = findViewById(R.id.order_info);
        on_way = findViewById(R.id.driver_on_the_way_info);
        on_site = findViewById(R.id.driver_on_site_info);
        begin = findViewById(R.id.trip_begining_info);
        end = findViewById(R.id.trip_end_info);

        find = findViewById(R.id.find_order_but);
        accept = findViewById(R.id.accept_order_but);
        decline = findViewById(R.id.decline_order_but);
        on_site_b = findViewById(R.id.driver_on_site_but);
        begin_b = findViewById(R.id.trip_begining_but);
        end_b = findViewById(R.id.trip_end_but);

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        find.setOnClickListener(v -> findButton());
        accept.setOnClickListener(v -> acceptButton());
        decline.setOnClickListener(v -> declineButton());
        on_site_b.setOnClickListener(v -> onSiteButton());
        begin_b.setOnClickListener(v -> beginButton());
        end_b.setOnClickListener(v -> endButton());
    }

    private void endButton() {
        Gson gson = new Gson();
        Trip_ending te = new Trip_ending();
        socket.sendMessage(gson.toJson(te));
        socket.disconnect();
    }

    private void beginButton() {
        Gson gson = new Gson();
        Trip_beginning tb = new Trip_beginning();
        socket.sendMessage(gson.toJson(tb));

    }

    private void onSiteButton() {
        Gson gson = new Gson();
        Driver_on_site dos = new Driver_on_site();
        socket.sendMessage(gson.toJson(dos));
    }

    private void declineButton() {
        Gson gson = new Gson();
        possible_order po = new possible_order(false);
        socket.sendMessage(gson.toJson(po));
        on_way.setText("disconnected");
        socket.disconnect();
    }

    private void acceptButton() {
        Gson gson = new Gson();
        possible_order po = new possible_order(true);
        socket.sendMessage(gson.toJson(po));
    }

    private void findButton() {
        Gson gson = new Gson();
        try {
            socket = new WebSocketManager();
            socket.connect();

            while(longitude == 0) {
                tryGeo();
                sleep(10);
            }
            Log.e("cords", longitude + " : " + latitude);
            Find_order fo = new Find_order(longitude, latitude);
            while (!socket.isConnected()) {
                sleep(10);
            }
            socket.sendMessage(gson.toJson(fo));
        }catch (URISyntaxException ex) {
            Log.e("SockErr", ex.getMessage());
        }
    }
}