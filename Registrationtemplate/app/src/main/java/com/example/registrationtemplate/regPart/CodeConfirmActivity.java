package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;

//Активность для проверки кода подтверждения
public class CodeConfirmActivity extends AppCompatActivity {

    ImageButton back;
    Button confirm;
    TextView send_again;

    //Кастомная view
    CodeInputView code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_code);
        initialization();
    }

    private void back() {
        finish();
    }

    private void sendAgain() {

    }
    private boolean tryToConfirm() {
        return true;
    }
    private void startPassword() {
        Intent intent = new Intent(this, PasswordActivity.class);
        startActivity(intent);
    }

    private void initialization() {
        back = findViewById(R.id.code_back_but);
        back.setOnClickListener(v -> back());

        code = findViewById(R.id.code_c);

        send_again = findViewById(R.id.send_again_c);
        send_again.setOnClickListener(v -> sendAgain());

        confirm = findViewById(R.id.confirm_but_c);
        confirm.setOnClickListener(v -> {
            if(tryToConfirm())
                startPassword();
        });
    }
}
