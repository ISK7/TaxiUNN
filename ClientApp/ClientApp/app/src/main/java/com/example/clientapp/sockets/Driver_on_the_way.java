package com.example.clientapp.sockets;

public class Driver_on_the_way {
    String  message_type = "DRIVER_ON_THE_WAY";
    public static String getMessage_type() {return "DRIVER_ON_THE_WAY";}


    info info;
    public String getStartTime() {
        return info.time_start_order;
    }

    class info {
        String time_start_order;
    }
}
