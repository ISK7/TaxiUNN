package com.example.clientapp.sockets;

public class Error_resp {
    String message_type = "ERROR";
    info info;
    public static String getMessage_type() {return "ERROR";}

    class info {
        String[] Error;
    }
}
