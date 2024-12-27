package com.example.clientapp.generalData;

import static android.os.SystemClock.sleep;

import android.util.Log;

import com.example.clientapp.sockets.Driver_data;
import com.example.clientapp.sockets.Driver_on_site;
import com.example.clientapp.sockets.Driver_on_the_way;
import com.example.clientapp.sockets.Error_resp;
import com.example.clientapp.sockets.Trip_beginning;
import com.example.clientapp.sockets.Trip_ending;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Connection
{
    private int BUFFER_SIZE = 1024;
    BufferedReader in;
    BufferedWriter out;
    private  Socket mSocket = null;
    private  String mHost = null;
    private  int mPort = 0;

    public static final String LOG_TAG = "SOCKET";
    boolean ordering, canceling;
    byte[] data;
    private final static String soclink = "http://" + Server.getHost() + ":" + Server.getPort() + "/ws/client"
            + "token: " + App.getAccessToken();


    public Connection (final String host, final int port)
    {
        this.mHost = host;
        this.mPort = port;
    }

    public void session() throws Exception {
        canceling = false;
        ordering = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    openConnection();
                    App.setStatus(Status.CONNECTED);
                    while (!ordering) {
                        sleep(1);
                    }
                    sendData(data);


                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void makeOrder(byte[] order) {
        data = order;
        ordering = true;
    }
    // Метод открытия сокета
    public void openConnection() throws Exception
    {
        // Если сокет уже открыт, то он закрывается
        closeConnection();
        try {
            // Создание сокета
            mSocket = new Socket(mHost, mPort);
            in = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
        } catch (IOException e) {
            throw new Exception("Невозможно создать сокет: "
                    + e.getMessage());
        }
    }
    /**
     * Метод закрытия сокета
     */
    public void closeConnection()
    {
        if (mSocket != null && !mSocket.isClosed()) {
            try {
                in.close();
                mSocket.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Ошибка при закрытии сокета :"
                        + e.getMessage());
            } finally {
                mSocket = null;
            }
        }
        mSocket = null;
        App.setStatus(Status.USING);
    }
    /**
     * Метод отправки данных
     */
    public void sendData(byte[] data) throws Exception {
        // Проверка открытия сокета
        if (mSocket == null || mSocket.isClosed()) {
            throw new Exception("Ошибка отправки данных. " +
                    "Сокет не создан или закрыт");
        }
        // Отправка данных
        try {
            mSocket.getOutputStream().write(data);
            mSocket.getOutputStream().flush();
        } catch (IOException e) {
            throw new Exception("Ошибка отправки данных : "
                    + e.getMessage());
        }
    }

    public boolean readEmpty() throws Exception {
        Gson gson = new Gson();
        try {
            String response = "";
            int charsRead = 0;
            char[] buffer = new char[BUFFER_SIZE];
            while ((charsRead = in.read(buffer)) != -1) {
                response += new String(buffer).substring(0, charsRead);
            }

            try {
                gson.fromJson(response, Error_resp.class);
                return false;
            } catch (Exception e) {}
            gson.fromJson(response, Driver_on_site.class);
            return true;
        } catch (IOException e) {
            throw new Exception("Ошибка чтения данных : "
                    + e.getMessage());
        }
    }
    public String getDriverData() throws Exception {
        Gson gson = new Gson();
        try {
            String response = "";
            int charsRead = 0;
            char[] buffer = new char[BUFFER_SIZE];
            while ((charsRead = in.read(buffer)) != -1 && !canceling) {
                response += new String(buffer).substring(0, charsRead);
            }
            try {
                gson.fromJson(response, Error_resp.class);
                return "Error";
            } catch (Exception e) {}
            Driver_data ans = gson.fromJson(response, Driver_data.class);
            return "Function is not supported yet";
        } catch (IOException e) {
            throw new Exception("Ошибка чтения данных: "
                    + e.getMessage());
        }
    }
    public String getStartTime() throws Exception {
        Gson gson = new Gson();
        try {
            String response = "";
            int charsRead = 0;
            char[] buffer = new char[BUFFER_SIZE];
            while ((charsRead = in.read(buffer)) != -1) {
                response += new String(buffer).substring(0, charsRead);
            }

            try {
                gson.fromJson(response, Error_resp.class);
                return "Error";
            } catch (Exception e) {}
            Driver_on_the_way ans = gson.fromJson(response, Driver_on_the_way.class);
            return ans.getStartTime();
        } catch (IOException e) {
            throw new Exception("Ошибка чтения данных : "
                    + e.getMessage());
        }
    }

    public String getBeginningTime() throws Exception {
        Gson gson = new Gson();
        try {
            String response = "";
            int charsRead = 0;
            char[] buffer = new char[BUFFER_SIZE];
            while ((charsRead = in.read(buffer)) != -1) {
                response += new String(buffer).substring(0, charsRead);
            }

            try {
                gson.fromJson(response, Error_resp.class);
                return "Error";
            } catch (Exception e) {}
            Trip_beginning ans = gson.fromJson(response, Trip_beginning.class);
            return ans.getBeginTime();
        } catch (IOException e) {
            throw new Exception("Ошибка чтения данных : "
                    + e.getMessage());
        }
    }

    public String getEndingTime() throws Exception {
        Gson gson = new Gson();
        try {
            String response = "";
            int charsRead = 0;
            char[] buffer = new char[BUFFER_SIZE];
            while ((charsRead = in.read(buffer)) != -1) {
                response += new String(buffer).substring(0, charsRead);
            }

            try {
                gson.fromJson(response, Error_resp.class);
                return "Error";
            } catch (Exception e) {}
            Trip_ending ans = gson.fromJson(response, Trip_ending.class);
            return ans.getEndTime();
        } catch (IOException e) {
            throw new Exception("Ошибка чтения данных : "
                    + e.getMessage());
        }
    }
    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
        closeConnection();
    }
}
