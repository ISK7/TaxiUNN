package com.example.driverapp.sockets;

public class possible_order {
    String message_type = "POSSIBLE_ORDER";
    public static String  getMessageType() {
        return "POSSIBLE_ORDER";
    }
    i info;
    public possible_order(boolean agree) {
        info = new i(agree);
    }

    class i {
        public i(boolean agree) {
            is_agree = agree;
        }
        boolean is_agree;
    }
}
