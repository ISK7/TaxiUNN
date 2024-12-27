package com.example.clientapp.sockets;

public class Trip_ending {
    public String message_type = "TRIP_ENDING";
    info info;
    public static String getMessage_type() {return "TRIP_ENDING";}

    public String getEndTime() {
        return info.time_trip_ending;
    }
    class info {
        String time_trip_ending;
    }
}
