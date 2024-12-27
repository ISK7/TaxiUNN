package com.example.clientapp.mainPart;

import static android.os.SystemClock.sleep;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.clientapp.R;

public class DriverIsWaitingFragment extends Fragment {
    EditText from, to;
    TextView name, number;
    private View view;
    private MainAppActivity main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.driver_is_waiting, container, false);
        init();
        return view;
    }

    void setVariables(MainAppActivity m,String from_t, String to_t,
                      String name_t, String number_t) {
        while (number == null) {
            sleep(10);
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                main = m;
                from.setText(from_t);
                to.setText(to_t);
                name.setText(name_t);
                number.setText(number_t);
            }
        });
    }

    private void init() {
        from = view.findViewById(R.id.from_text_waiting);
        to = view.findViewById(R.id.to_text_waiting);
        name = view.findViewById(R.id.car_name);
        number = view.findViewById(R.id.car_number);
    }
}
