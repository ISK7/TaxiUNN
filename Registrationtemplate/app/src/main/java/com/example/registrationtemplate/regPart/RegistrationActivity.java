package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;

public class RegistrationActivity extends AppCompatActivity {

    EditText name;
    EditText login;
    Button reg_but;
    TextView reg_to_log_but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        initialization();
    }

    private void startMain() {
        Intent intent = new Intent(this, MainAppActivity.class);
        startActivity(intent);
    }

    private boolean tryToReg() {

        return true;
    }
    private void incorrectReg() {

    }
    private void trySendCode() {
            Intent intent = new Intent(this, CodeConfirmActivity.class);
            startActivity(intent);
    }
    private void startLog() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    private void initialization() {
        name = findViewById(R.id.name_view_r);
        login = findViewById(R.id.login_view_r);

        reg_but = findViewById(R.id.reg_but_r);
        reg_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tryToReg()) {
                    trySendCode();
                }
                else incorrectReg();
            }
        });

        reg_to_log_but = findViewById(R.id.reg_to_log_but);
        reg_to_log_but.setOnClickListener(v -> startLog());
    }
}