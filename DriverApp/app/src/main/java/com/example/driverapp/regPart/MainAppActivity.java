package com.example.driverapp.regPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.driverapp.R;
import com.example.driverapp.generalData.App;
import com.example.driverapp.generalData.Status;

public class MainAppActivity extends AppCompatActivity {

    Button logout_but;

    //Общие для всего приложения настройки
    SharedPreferences sharedPreferences;

    //Объект для изменения общих настроек
    SharedPreferences.Editor regEditor;
    TextView order, on_way, on_site, begin, end;
    Button find, accept, decline, on_site_b, begin_b, end_b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_app);

        App.setStatus(Status.USING);
        init();
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
    }
}