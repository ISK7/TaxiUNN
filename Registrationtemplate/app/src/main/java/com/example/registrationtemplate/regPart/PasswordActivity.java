package com.example.registrationtemplate.regPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registrationtemplate.R;
import com.example.registrationtemplate.generalData.App;
import com.example.registrationtemplate.generalData.Status;

//Активность для ввода пароля и его подтверждения
public class PasswordActivity extends AppCompatActivity {

    ImageButton back;
    EditText newPassword;
    EditText secondPassword;
    TextView newPassword_er;
    TextView secondPassword_er;
    Button done;
    TextView header;

    //Общие для всего приложения настройки
    SharedPreferences sharedPreferences;

    //Объект для изменения общих настроек
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
        editor.commit();

        startMain();
    }

    private void incorrectPassword() {
    }

    private void startMain() {
        Intent intent = new Intent(this, MainAppActivity.class);
        startActivity(intent);
    }

    private void initialization() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        Status s = App.getStatus();

        header = findViewById(R.id.password_title_p);
        if(s == Status.LOG_IN)
        {
            header.setText(R.string.set_new_password_head);
        }
        else {
            header.setText(R.string.set_password_head);
        }

        back = findViewById(R.id.password_back_but);
        back.setOnClickListener(v -> back());

        newPassword = findViewById(R.id.password_enter_p);
        secondPassword = findViewById(R.id.password_confirm_p);

        newPassword_er = findViewById(R.id.password_first_error_p);
        secondPassword_er = findViewById(R.id.password_second_error_p);

        done = findViewById(R.id.done_but_p);
        done.setOnClickListener(v -> {
            if(tryToSetPassword())
                correctPassword();
            else incorrectPassword();
        });
    }
}
