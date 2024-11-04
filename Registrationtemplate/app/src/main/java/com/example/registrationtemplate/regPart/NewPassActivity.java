package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;

//Активность для подтверждения необходимости нового пароля
public class NewPassActivity extends AppCompatActivity {

    EditText e_mail;
    TextView e_mail_er;
    ImageButton back_but;
    Button sendCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_password);
        initialization();
    }

    private boolean tryToSend() {
        return true;
    }

    private void sendCode() {
        Intent intent = new Intent(this, CodeConfirmActivity.class);
        startActivity(intent);
    }
    private void back() {
        finish();
    }

    private void initialization() {
        e_mail = findViewById(R.id.log_in_view_n);

        e_mail_er = findViewById(R.id.error_email_n);

        sendCode = findViewById(R.id.send_but_n);
        sendCode.setOnClickListener(v -> {
            if(tryToSend())
                sendCode();
        });

        back_but = findViewById(R.id.newp_back_but);
        back_but.setOnClickListener(v -> back());
    }
}
