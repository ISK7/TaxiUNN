package com.example.clientapp.mainPart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.clientapp.R;

public class DriverFoundFragment extends Fragment {

    private EditText from, to;
    private Button cancel;
    private View view;
    private MainAppActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.driver_found, container, false);
        init();
        return view;
    }

    void setVariables(MainAppActivity main, String from_t, String to_t) {
        from.setText(from_t);
        to.setText(to_t);
        mainActivity = main;
    }

    private void init() {
        from = view.findViewById(R.id.from_text_found);
        to = view.findViewById(R.id.to_text_found);
        cancel = view.findViewById(R.id.cancel_found_but);

        cancel.setOnClickListener(v -> {
            mainActivity.canselRideThenAwait();
        });
    }
}
