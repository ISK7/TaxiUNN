package com.example.clientapp.mainPart;

import static android.os.SystemClock.sleep;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.clientapp.R;
import com.example.clientapp.generalData.App;
import com.example.clientapp.generalData.Status;

public class RoadIsEndFragment extends Fragment {
    TextView offer, going, end;
    Button close;

    int anchor;
    private View view;
    private MainAppActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.road_is_end, container, false);
        init();
        return view;
    }

    public void setVariable(MainAppActivity m, String offer_t, String going_t, String end_t) {
        while(close == null) {
            sleep(10);
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity = m;
                offer.setText(offer_t);
                going.setText(going_t);
                end.setText(end_t);
            }
        });
    }
    private void init() {
        offer = view.findViewById(R.id.offer_time_end);
        going = view.findViewById(R.id.going_time_end);
        end = view.findViewById(R.id.end_time_end);
        close = view.findViewById(R.id.close_but_end);
        anchor = R.id.review_anchor;

        close.setOnClickListener(v -> {
            App.setStatus(Status.NO_STATUS);
            mainActivity.toOffer();
        });
    }

    public void restore() {
        getView().setAlpha(1.0f);
        getView().setClickable(true);
    }
}
