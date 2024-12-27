package com.example.driverapp.regPart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.driverapp.R;
import com.example.driverapp.generalData.App;
import com.example.driverapp.generalData.Status;

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