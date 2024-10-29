package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;

public class RegistrationActivity extends AppCompatActivity {

    ImageButton back;
    EditText name;
    EditText login;
    Button reg_but;
    TextView reg_to_log_but;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CheckBox politics_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        initialization();
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

    private void back() {
        finish();
    }

    private void checkChange() {
        reg_but.setEnabled(!reg_but.isEnabled());
        if(reg_but.isEnabled())
            reg_but.setBackground(getDrawable(R.drawable.custom_but_blue));
        else
            reg_but.setBackground(getDrawable(R.drawable.custom_but_blue_inactive));
    }

    private void initialization() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        back = findViewById(R.id.reg_back_but);
        back.setOnClickListener(v -> back());

        name = findViewById(R.id.name_view_r);
        login = findViewById(R.id.login_view_r);

        reg_but = findViewById(R.id.reg_but_r);
        reg_but.setOnClickListener(v -> {
            if (tryToReg()) {
                correctReg();
            }
            else incorrectReg();
        });

        politics_check = findViewById(R.id.politics_check_box_r);
        politics_check.setOnClickListener(v -> checkChange());

        reg_to_log_but = findViewById(R.id.reg_to_log_but);
        reg_to_log_but.setOnClickListener(v -> startLog());
    }
}