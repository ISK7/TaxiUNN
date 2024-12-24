package com.example.clientapp.mainPart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.clientapp.R;
import com.example.clientapp.generalData.App;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class OfferFragment extends BottomSheetDialogFragment {

    private int cheap = 240, medium = 340, expensive = 540;
    private EditText from;
    private EditText to;
    private ArrayList<Button> tariffs;
    private ArrayList<Integer> costs;
    private Button cur_tariff;
    private Button offer;
    private View view;
    private TextView from_er, to_er;

    private MainAppActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.offer_fragment, container, false);
        init();
        return view;
    }

    public String getFromText() {
        return from.getText().toString();
    }
    public String getToText() {
        return to.getText().toString();
    }

    public void SetMain(MainAppActivity m) {
        mainActivity = m;
    }

    private void init(){
        from = view.findViewById(R.id.from_input);
        to = view.findViewById(R.id.to_input);
        from_er = view.findViewById(R.id.from_er);
        to_er = view.findViewById(R.id.to_er);

        tariffs = new ArrayList<>();
        costs = new ArrayList<>();
        tariffs.add(view.findViewById(R.id.tariff_cheap));
        costs.add(cheap);
        tariffs.add(view.findViewById(R.id.tariff_medium));
        costs.add(medium);
        tariffs.add(view.findViewById(R.id.tariff_expencive));
        costs.add(expensive);
        offer = view.findViewById(R.id.offer_but);

        for(Button tariff : tariffs) {
            tariff.setOnClickListener(v -> {
                if(cur_tariff == tariff) return;
                if(cur_tariff != null) {
                    cur_tariff.setBackground(getResources().getDrawable(R.drawable.custom_view_black_inactive));
                }
                tariff.setBackground(getResources().getDrawable(R.drawable.custom_view_black_corners));
                cur_tariff = tariff;
                if (!offer.isEnabled()) {
                    offer.setEnabled(true);
                    offer.setBackground(getResources().getDrawable(R.drawable.custom_but_blue));
                }
            });
        }

        offer.setOnClickListener(v -> {
            EditText[] fields = {from, to};
            TextView[] errors = {from_er, to_er};
            if (!App.fieldsNotEmpty(fields, errors) || cur_tariff == null) return;
            mainActivity.makeOffer(from.getText().toString(), to.getText().toString(),
                    costs.get(tariffs.indexOf(cur_tariff)));
        });

        BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.root));
        bottomSheetBehavior.setPeekHeight(dpToPx(154));
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }
}
