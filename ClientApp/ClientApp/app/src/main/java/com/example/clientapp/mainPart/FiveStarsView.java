package com.example.clientapp.mainPart;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.clientapp.R;

public class FiveStarsView extends LinearLayout {

    ImageView[] stars;
    int score;
    public FiveStarsView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.five_sars_view, this, true);

        score = 0;
        stars = new ImageView[5];
        stars[0] = findViewById(R.id.star_1);
        stars[1] = findViewById(R.id.star_2);
        stars[2] = findViewById(R.id.star_3);
        stars[3] = findViewById(R.id.star_4);
        stars[4] = findViewById(R.id.star_5);

        for (int i = 0; i <  stars.length; i++) {
            final int index = i;
            stars[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int j = 0; j <= index; j++) {
                        stars[0].setImageResource(R.drawable.filled_star);
                    }
                    for(int j = index + 1; j <= stars.length; j++) {
                        stars[0].setImageResource(R.drawable.no_star);
                    }
                    score = index + 1;
                }
            });
        }
    }
    public int getScore() {
        return score;
    }
}
