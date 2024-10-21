package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;

public class LogInActivity extends AppCompatActivity {

    EditText login;
    EditText password;
    Button login_but;
    ImageButton back_but;
    TextView to_new_password_but;
    TextView to_reg_but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        initialization();
    }

    private boolean tryToLogin() {

        return true;
    }
    private void incorrectLogin() {

    }
    private void startMain() {
        Intent intent = new Intent(this, MainAppActivity.class);
        startActivity(intent);
    }
    private void back() {
        finish();
    }
    private void forgetPassword() {

    }
    private void createAccount() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void initialization() {
        login = findViewById(R.id.login_view_l);
        password = findViewById(R.id.password_view_l);

        login_but = findViewById(R.id.login_but_l);
        login_but.setOnClickListener(v -> {
            if (tryToLogin()) {
                startMain();
            }
            else incorrectLogin();
        });

        back_but = findViewById(R.id.login_back_but);
        back_but.setOnClickListener(v -> {
            back();
        });

        to_new_password_but = findViewById(R.id.forget_password_but);
        to_new_password_but.setOnClickListener(v -> forgetPassword());

        to_reg_but = findViewById(R.id.log_to_reg_but);
        to_reg_but.setOnClickListener(v -> createAccount());
    }

}