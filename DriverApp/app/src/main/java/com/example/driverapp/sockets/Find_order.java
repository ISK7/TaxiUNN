package com.example.driverapp.sockets;

public class Find_order {
    String message_type = "FIND_ORDER";

    i info;
    public static String  getMessageType() {
        return "FIND_ORDER";
    }
    public Find_order(double lon, double lat) {
        info = new i(lon, lat);
    }
    class i {
        public i(double lon, double lat) {
            location = new float[2];
            location[0] = (float)lon;
            location[1] = (float)lat;
        }
        float[] location;
    }
}
