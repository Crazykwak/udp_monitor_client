package com;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataReceiverThread extends Thread {

    private final int receiveSize = 1024 * 5;
    private final byte[] sendBytes = new byte[10];
    private byte[] receiveBytes = new byte[receiveSize];
    private DatagramPacket send;
    private DatagramPacket receive;
    private DatagramSocket ds;
    private JSONParser parser = new JSONParser();
    private Map<String, String> monitoringDataStore;
    private String[] columnArray;
    private String currentDate;

    @Override
    public void run() {

        try {
            ds.setSoTimeout(1000);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        while(true) {

            try {
                ds.send(send);
                ds.receive(receive);
                if (receive.getData() != null) {
                    String data = new String(receive.getData(), 0, receive.getLength());
                    JSONObject jsonObject = (JSONObject) parser.parse(data);

                    if (columnArray == null) {
                        getAndSetColArray(jsonObject);
                        sleep(100);
                        continue;
                    }

                    for (String target : columnArray) {
                        monitoringDataStore.put(target, String.valueOf(jsonObject.getOrDefault(target, "")));
                    }
                    currentDate = Util.getDate();
                    monitoringDataStore.put("currentDate", currentDate);
                    sleep(1000);
                }
            } catch (SocketTimeoutException e) {

            } catch (ParseException e) {

            } catch (InterruptedException e) {

            } catch (IOException e) {

            }
        }
    }

    private void getAndSetColArray(JSONObject jsonObject) {
        JSONArray jsonArray = (JSONArray) jsonObject.get("colArray");
        if (jsonArray != null && (jsonArray.getClass().isArray() || jsonArray instanceof List)) {
            columnArray = jsonArrayToStringArray(jsonArray);
        }
    }

    private String[] jsonArrayToStringArray(JSONArray jsonArray) {
        String[] arr = new String[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            arr[i] = (String) jsonArray.get(i);
        }
        return arr;
    }

    public DataReceiverThread(String host, int port) throws UnknownHostException, SocketException {
        InetAddress inetAddress = InetAddress.getByName(host);
        send = new DatagramPacket(sendBytes, sendBytes.length, inetAddress, port);
        receive = new DatagramPacket(receiveBytes, receiveBytes.length);
        ds = new DatagramSocket();
        monitoringDataStore = new ConcurrentHashMap<>();
    }

    public Map<String, String> getMonitoringDataStore() {
        return monitoringDataStore;
    }

    public String[] getColumnArray() {
        return columnArray;
    }
}
