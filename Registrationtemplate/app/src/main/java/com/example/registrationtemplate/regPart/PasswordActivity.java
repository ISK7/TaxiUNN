package com.example.registrationtemplate.regPart;

import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password);
        initialization();
    }

    private void back() {
        finish();
    }

    private boolean tryToSetPassword() {
        return true;
    }

    private void startMain() {
        Intent intent = new Intent(this, MainAppActivity.class);
        startActivity(intent);
    }

    private void initialization() {
        back = findViewById(R.id.password_back_but);
        back.setOnClickListener(v -> back());

        newPassword = findViewById(R.id.password_enter_p);
        secondPassword = findViewById(R.id.password_confirm_p);

        done = findViewById(R.id.done_but_p);
        done.setOnClickListener(v -> {
            if(tryToSetPassword())
                startMain();
        });
    }
}
