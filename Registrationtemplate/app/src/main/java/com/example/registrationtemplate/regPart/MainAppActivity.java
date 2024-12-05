package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;
import com.example.registrationtemplate.generalData.App;
import com.example.registrationtemplate.generalData.Status;

public class MainAppActivity extends AppCompatActivity {

    Button logout_but;

    //Общие для всего приложения настройки
    SharedPreferences sharedPreferences;

    //Объект для изменения общих настроек
    SharedPreferences.Editor regEditor;
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
    }
}