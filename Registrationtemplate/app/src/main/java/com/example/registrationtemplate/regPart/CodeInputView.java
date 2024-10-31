package com.example.registrationtemplate.regPart;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.registrationtemplate.R;

//Кастомная View для принятия кода
public class CodeInputView extends LinearLayout {

    private EditText[] editTexts;

    public CodeInputView(Context context) {
        super(context);
        init(context);
    }

    public CodeInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_code_input, this, true);

        editTexts = new EditText[5];
        editTexts[0] = findViewById(R.id.code_1);
        editTexts[1] = findViewById(R.id.code_2);
        editTexts[2] = findViewById(R.id.code_3);
        editTexts[3] = findViewById(R.id.code_4);
        editTexts[4] = findViewById(R.id.code_5);

        for (int i = 0; i < editTexts.length; i++) {
            final int index = i;
            editTexts[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < editTexts.length - 1) {
                        editTexts[index + 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    public String getCode() {
        StringBuilder code = new StringBuilder();
        for (EditText editText : editTexts) {
            code.append(editText.getText().toString());
        }
        return code.toString();
    }

    public void clear() {
        for (EditText editText : editTexts) {
            editText.setText("");
        }
        editTexts[0].requestFocus();
    }

    //Обработка нажатия backspace
    public void handleBackPress() {
        for (int i = editTexts.length - 1; i >= 0; i--) {
            if (editTexts[i].isFocused()) {
                if (i > 0) {
                    editTexts[i].setText(""); // Удаляем цифру
                    editTexts[i - 1].requestFocus(); // Переход к предыдущему полю
                } else {
                    editTexts[i].setText(""); // Если первый элемент, просто очищаем
                }
                break;
            } else if (editTexts[i].getText().toString().isEmpty()) {
                editTexts[i].requestFocus(); // Перейти к первому пустому элементу
                break;
            }
        }
    }

    //Обработка нажатия клавиши
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
            handleBackPress(); // Обработка нажатия Backspace
            return true; // Поглощаем событие
        }
        return super.dispatchKeyEvent(event);
    }
}
