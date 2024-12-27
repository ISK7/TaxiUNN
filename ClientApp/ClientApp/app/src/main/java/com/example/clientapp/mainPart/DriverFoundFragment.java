package com.example.clientapp.mainPart;

import static android.os.SystemClock.sleep;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.clientapp.R;

public class DriverFoundFragment extends Fragment {

    private EditText from, to;
    private TextView description, number;
    private Button cancel;
    private View view;
    private MainAppActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.driver_found, container, false);
        init();
        return view;
    }

    void setVariables(MainAppActivity main, String from_t, String to_t, String descr, String numb) {
        while(cancel == null) {
            sleep(10);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                from.setText(from_t);
                to.setText(to_t);
                description.setText(descr);
                number.setText(numb);
                from.invalidate();
                to.invalidate();
                description.invalidate();
                number.invalidate();
                mainActivity = main;
            }
        });
    }

    private void init() {
        from = view.findViewById(R.id.from_text_found);
        to = view.findViewById(R.id.to_text_found);
        description = view.findViewById(R.id.car_name);
        number = view.findViewById(R.id.car_number);
        cancel = view.findViewById(R.id.cancel_found_but);

        cancel.setOnClickListener(v -> {
            mainActivity.canselRideThenAwait();
        });
    }
}
