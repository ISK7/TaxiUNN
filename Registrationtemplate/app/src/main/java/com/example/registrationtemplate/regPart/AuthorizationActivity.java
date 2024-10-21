package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;

public class AuthorizationActivity extends AppCompatActivity {

    Button yesBut;
    Button logBut;
    private boolean isAuthorized = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authoriz_offer);
        if(isAuthorized) startMain();
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
    private void startMain() {
        Intent intent = new Intent(this, MainAppActivity.class);
        startActivity(intent);
    }

    private void initialize() {
        logBut = findViewById(R.id.go_to_log_but);
        logBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLog();
            }
        });

        yesBut = findViewById(R.id.go_to_reg_but);
        yesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startReg();
            }
        });
    }
}