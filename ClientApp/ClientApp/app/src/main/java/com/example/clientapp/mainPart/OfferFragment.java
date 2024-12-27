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
    private OfferView cur_tariff;
    private ArrayList<OfferView> tariffs;
    LinearLayout tariff_list;
    private Button offer;
    private View view;
    private TextView from_er, to_er, no_tariff;
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
        offer = view.findViewById(R.id.offer_but);
        no_tariff = view.findViewById(R.id.no_tariff_text);

        tariff_list = view.findViewById(R.id.offer_layout);
        tariffs = new ArrayList<>();

        offer.setOnClickListener(v -> {
            EditText[] fields = {from, to};
            TextView[] errors = {from_er, to_er};
            if (!App.fieldsNotEmpty(fields, errors) || cur_tariff == null) return;
            mainActivity.tryOffer(from.getText().toString(), to.getText().toString(),
                    cur_tariff.getName(), cur_tariff.getCost());
        });

        BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.root));
        bottomSheetBehavior.setPeekHeight(dpToPx(154));
    }

    void delNoTariffSign() {
        tariff_list.removeView(no_tariff);
    }

    void chooseTariff(OfferView tar) {
        cur_tariff = tar;
        for(OfferView tariff : tariffs) {
            if(cur_tariff.equals(tariff)) tariff.setEnabledBackground();
            else tariff.setDisabledBackground();
        }
        if (!offer.isEnabled()) {
            offer.setEnabled(true);
            offer.setBackground(getResources().getDrawable(R.drawable.custom_but_blue));
        }
    }

    void addTariff(Float price, String name) {
        OfferView tariff = new OfferView(getContext());
        tariff.setStats(this, price,name);
        tariffs.add(tariff);
        tariff_list.addView(tariff);
    }
    void setToEr(String er) {
        to_er.setText(er);
    }
    void setFromEr(String er) {
        from_er.setText(er);
    }
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }
}
