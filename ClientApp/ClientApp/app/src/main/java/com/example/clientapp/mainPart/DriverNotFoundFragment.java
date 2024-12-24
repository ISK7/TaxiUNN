package com.example.clientapp.mainPart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.clientapp.R;

public class DriverNotFoundFragment extends Fragment {
    private View view;
    Button close;
    MainAppActivity main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.drivers_not_found, container, false);
        init();
        return view;
    }
    void setMain(MainAppActivity m) {
        main = m;
    }

    private void init(){
        close = view.findViewById(R.id.close_but_nfound);
        close.setOnClickListener(v -> {
            main.toOffer();
        });
    }
}
