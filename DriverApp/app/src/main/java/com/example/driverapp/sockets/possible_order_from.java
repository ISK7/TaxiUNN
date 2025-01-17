package com.example.driverapp.sockets;

public class possible_order_from {
    String message_type = "POSSIBLE_ORDER";
    public static String  getMessageType() {
        return "POSSIBLE_ORDER";
    }
    i info;
    public float[] getFrom() {
        return info.location_from;
    }
    public float[] getTo() {
        return info.location_to;
    }
    public String getFare() {
        return info.fare;
    }

    class i {
        float[] location_from;
        float[] location_to;
        String fare;
    }
}
