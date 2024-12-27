package com.example.clientapp.mainPart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.clientapp.R;

public class OfferView extends RelativeLayout {
    Button but;
    View background;
    float price;
    TextView cost, name;
    OfferFragment parent;
    public OfferView(Context context) {
        super(context);
        init(context);
    }
    public void setStats(OfferFragment p, Float price, String name) {
        parent = p;
        cost.setText(price.toString() + getContext().getString(R.string.valute));
        this.name.setText(name);
        this.price = price;

        cost.invalidate();
        this.name.invalidate();
    }

    public void setEnabledBackground() {
        background.setBackground(getResources().getDrawable(R.drawable.custom_view_black_corners));
    }
    public void setDisabledBackground() {
        background.setBackground(getResources().getDrawable(R.drawable.custom_view_black_inactive));
    }

    public float getCost() {
        return price;
    }

    public String getName() {
        return name.getText().toString();
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.offer_view, this, true);
        background = findViewById(R.id.background);
        but = findViewById(R.id.button_t);
        cost = findViewById(R.id.cost_t);
        name = findViewById(R.id.name_t);

        but.setOnClickListener(v -> {
            parent.chooseTariff(this);
        });
    }
}
