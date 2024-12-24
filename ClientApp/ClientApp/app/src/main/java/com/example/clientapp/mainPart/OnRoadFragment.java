package com.example.clientapp.mainPart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.clientapp.R;
import com.example.clientapp.generalData.App;
import com.example.clientapp.generalData.Status;

public class OnRoadFragment extends Fragment {

    TextView name, number, offer, going;
    EditText from, to;
    Button cancel;
    private View view;
    private MainAppActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.driver_found, container, false);
        init();
        return view;
    }

    void setVariables(MainAppActivity main, String from_t, String to_t,
                      String name_t, String number_t, String offer_t, String going_t) {
        mainActivity = main;
        from.setText(from_t);
        to.setText(to_t);
        name.setText(name_t);
        number.setText(number_t);
        offer.setText(offer_t);
        going.setText(going_t);
    }

    private void init() {
        name = view.findViewById(R.id.car_name);
        number = view.findViewById(R.id.car_number);
        offer = view.findViewById(R.id.offer_time_road);
        going = view.findViewById(R.id.going_time_road);
        from = view.findViewById(R.id.from_text_onr);
        to = view.findViewById(R.id.to_text_onr);
        cancel = view.findViewById(R.id.cancel_road_but);

        cancel.setOnClickListener(v-> {
            App.setStatus(Status.NO_STATUS);
            mainActivity.toOffer();
        });
    }
}
