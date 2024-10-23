package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;

public class MainAppActivity extends AppCompatActivity {

    Button logout_but;
    SharedPreferences.Editor regEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_app);
        init();
    }

    private void init() {
        regEditor = getSharedPreferences("RegPrefs", MODE_PRIVATE).edit();

        logout_but = findViewById(R.id.log_out_but);
        logout_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regEditor.putBoolean("isLogged",false);
                regEditor.apply();
                //do smth
                Intent intent = new Intent(v.getContext(), AuthorizationActivity.class);
                startActivity(intent);
            }
        });
    }
}