package com.example.registrationtemplate.generalData;

import java.net.URL;

//Класс для работы с сервером
public class Server {

    private final static String link = "https://virtserver.swaggerhub.com/vinicuktimofei/taxiUNN/1.0.0";

    private final static int port = 8080;

    public static int getPort() {
        return port;
    }
}
