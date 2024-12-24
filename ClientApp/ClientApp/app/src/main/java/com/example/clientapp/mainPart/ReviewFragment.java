package com.example.clientapp.mainPart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.clientapp.R;

public class ReviewFragment extends Fragment {
    FiveStarsView stars;
    EditText review;
    Button send, cancel;
    RoadIsEndFragment parent;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.rewiev, container, false);
        init();
        return view;
    }

    void setParent(RoadIsEndFragment roadIsEndFragment) {
        parent = roadIsEndFragment;
    }

    private void init() {
        stars = view.findViewById(R.id.stars);
        review = view.findViewById(R.id.review_text);
        send = view.findViewById(R.id.send_but_rev);
        cancel = view.findViewById(R.id.close_but_rev);

        cancel.setOnClickListener(v -> {
            parent.restore();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(this)
                    .commit();
        });
    }
}
