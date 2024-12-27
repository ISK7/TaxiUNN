package com.example.clientapp.sockets;

public class Trip_beginning {
    String  message_type = "TRIP_BEGINNING";

    info info;
    public static String getMessage_type() {return "TRIP_BEGINNING";}

    public String getBeginTime() {
        return info.time_trip_beginning;
    }
    class info {
        String time_trip_beginning;
    }
}
