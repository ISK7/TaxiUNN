package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.content.SharedPreferences;
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
    SharedPreferences.Editor editor;
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
    private void correctReg() {
        editor.putString("emailAddress",login.getText().toString());
        editor.putString("name", name.getText().toString());
        editor.apply();

        trySendCode();
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
        editor = getSharedPreferences("RegPrefs", MODE_PRIVATE).edit();

        name = findViewById(R.id.name_view_r);
        login = findViewById(R.id.login_view_r);

        reg_but = findViewById(R.id.reg_but_r);
        reg_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tryToReg()) {
                    correctReg();
                }
                else incorrectReg();
            }
        });

        reg_to_log_but = findViewById(R.id.reg_to_log_but);
        reg_to_log_but.setOnClickListener(v -> startLog());
    }
}