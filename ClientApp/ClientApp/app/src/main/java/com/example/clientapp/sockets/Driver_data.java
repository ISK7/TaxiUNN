package com.example.clientapp.sockets;

public class Driver_data {
    String  message_type = "DRIVER_DATA";
    i info;
    public static String getMessage_type() {return "DRIVER_DATA";}

    public String getModel() {
        return info.model;
    }
    public String getMark() {
        return info.mark;
    }
    public String getColor() {
        return info.color;
    }
    public String getNumber() {
        return info.state_number;
    }

    class i {
        String model;
        String mark;
        String color;
        String state_number;
    }
}
