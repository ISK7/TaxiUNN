package com.example.clientapp.requests;

public class price_list {
    float[] location_from;
    float[] location_to;
    public price_list(float long_f, float lat_f, float long_t, float lat_t) {
        location_to = new float[2];
        location_from = new float[2];
        location_from[0] = long_f;
        location_from[1] = lat_f;
        location_to[0] = long_t;
        location_to[1] = lat_t;
    }
}
