package com.example.clientapp.generalData;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketManager {
    private WebSocketClient socket;
    private final static String soclink = "ws://" + Server.getHost() + ":" + Server.getPort() + "/ws/client";
    public WebSocketManager() throws URISyntaxException {
        URI uri = new URI(soclink);

        // Создание WebSocket клиента
        socket = new WebSocketClient(uri, new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake handshake) {
               Log.e("connect","Connected to the server!");
               App.setStatus(Status.CONNECTED);
            }

            @Override
            public void onMessage(String message) {
                App.getData(message);
                // Обработка входящих сообщений
                Log.e("Received message: ", message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                App.setStatus(Status.USING);
                Log.e("Disconnected from the server. Reason: ", reason);
            }

            @Override
            public void onError(Exception ex) {
                Log.e("Error: ", ex.getMessage());
            }
        };
        socket.addHeader("token", App.getAccessToken());
    }

    // Метод для установления соединения
    public void connect() {
        if (socket != null && !socket.isOpen()) {
            socket.connect();
        }
    }

    // Метод для отправки сообщения
    public void sendMessage(String message) {
        if (socket != null && socket.isOpen()) {
            socket.send(message);
        }
    }

    // Метод для отключения
    public void disconnect() {
        if (socket != null) {
            socket.close();
        }
    }
}
