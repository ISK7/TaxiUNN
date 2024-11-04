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

/*
Активность с выбором элемента
*/
public class AuthorizationActivity extends AppCompatActivity {


    Button yesBut;
    Button logBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authoriz_offer);
        App.setStatus(Status.NO_STATUS);

        initialize();
    }

    private void startReg() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void startLog() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    private void initialize() {
        logBut = findViewById(R.id.go_to_log_but);
        logBut.setOnClickListener(v -> startLog());

        yesBut = findViewById(R.id.go_to_reg_but);
        yesBut.setOnClickListener(v -> startReg());
    }
}