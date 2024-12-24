package com.example.clientapp.mainPart;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.clientapp.R;

public class DriverSearchFragment extends Fragment {
    private View loadingBar;
    private EditText from, to;
    private Button cancel;
    private View view;
    private MainAppActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.driver_search_fragment, container, false);
        init();
        return view;
    }

    public void setMain(MainAppActivity m) {
        mainActivity = m;
    }
    private void init() {
        loadingBar = view.findViewById(R.id.loading_bar);
        from = view.findViewById(R.id.from_text);
        to = view.findViewById(R.id.to_text);
        cancel = view.findViewById(R.id.cancel_search_but);

        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.loading_animator);
        animatorSet.setTarget(loadingBar);
        animatorSet.start();

        from.setText(mainActivity.getFrom());
        to.setText(mainActivity.getTo());
        cancel.setOnClickListener(v -> {
            mainActivity.cancelOffer();
        });
    }
}
