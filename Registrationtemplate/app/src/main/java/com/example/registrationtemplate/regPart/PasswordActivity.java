package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;

public class PasswordActivity extends AppCompatActivity {

    ImageButton back;
    EditText newPassword;
    EditText secondPassword;
    Button done;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password);
        initialization();
    }

    private String passwordToHash(String input) {
        String result = new String(input);
        return result;
    }

    private void back() {
        finish();
    }

    private boolean tryToSetPassword() {
        return true;
    }

    private void correctPassword() {
        editor.putString("password", passwordToHash(secondPassword.getText().toString()));
        editor.putBoolean("isLogged",true);
        editor.apply();

        startMain();
    }

    private void incorrectPassword() {
    }

    private void startMain() {
        Intent intent = new Intent(this, MainAppActivity.class);
        startActivity(intent);
    }

    private void initialization() {
        editor = getSharedPreferences("RegPrefs", MODE_PRIVATE).edit();

        back = findViewById(R.id.password_back_but);
        back.setOnClickListener(v -> back());

        newPassword = findViewById(R.id.password_enter_p);
        secondPassword = findViewById(R.id.password_confirm_p);

        done = findViewById(R.id.done_but_p);
        done.setOnClickListener(v -> {
            if(tryToSetPassword())
                correctPassword();
            else incorrectPassword();
        });
    }
}
