package com.odinues.m1customerApi.kbcard;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.*;

public class DataReceiverByUDPThread extends DataReceiver {

    private final int receiveSize = 1024 * 5;
    private final byte[] sendBytes = new byte[10];
    private byte[] receiveBytes = new byte[receiveSize];
    private DatagramPacket send;
    private DatagramPacket receive;
    private DatagramSocket ds;
    private JSONParser parser = new JSONParser();
    private DataStore dataStore;
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

                    dataStore.getAndSetColArray(jsonObject);

                    for (String target : dataStore.getColumnArray() ) {
                        dataStore.put(target, String.valueOf(jsonObject.getOrDefault(target, "")));
                    }

                    currentDate = Util.getDate();
                    dataStore.put("currentDate", currentDate);
                    sleep(1000);
                }
            } catch (SocketTimeoutException e) {

            } catch (ParseException e) {

            } catch (InterruptedException e) {

            } catch (IOException e) {

            }
        }
    }

    public DataReceiverByUDPThread(String host, int port, DataStore dataStore) throws UnknownHostException, SocketException {
        InetAddress inetAddress = InetAddress.getByName(host);
        send = new DatagramPacket(sendBytes, sendBytes.length, inetAddress, port);
        receive = new DatagramPacket(receiveBytes, receiveBytes.length);
        ds = new DatagramSocket();
    }
}
