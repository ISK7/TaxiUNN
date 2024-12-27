package com.example.clientapp.sockets;

public class Make_order {
    String  message_type = "MAKE_ORDER";
    info info;
    public static String getMessage_type() {return "MAKE_ORDER";}

    public Make_order(float from_long, float from_lat, float to_long, float to_lat,
                      String fare, float price){
        info = new info(new float[]{from_long,from_lat}, new float[] {to_long, to_lat},
                fare, price);
    }
    class info {
        float[] location_from;
        float[] location_to;
        String fare;
        float price;
        info(float[] from, float[] to, String fare, float price) {
            location_from = from;
            location_to = to;
            this.fare = fare;
            this.price = price;
        }
    }
}
