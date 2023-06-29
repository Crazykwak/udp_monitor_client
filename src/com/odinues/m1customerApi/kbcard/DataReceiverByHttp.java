package com.odinues.m1customerApi.kbcard;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DataReceiverByHttp extends DataReceiver {
    private DataStore dataStore;
    private URL url;
    private HttpURLConnection http;
    private JSONParser parser = new JSONParser();
    private String currentDate;
    private String responseData;
    private StringBuilder sb = new StringBuilder();


    public DataReceiverByHttp(String host, int port, String path ,DataStore dataStore) {
        Runtime.getRuntime().addShutdownHook(new ShutdownForHttp());
        this.dataStore = dataStore;
        try {
            String urlPath = "http://" + host + ":" + port + path;
            url = new URL(urlPath);
            setHttp();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setHttp() throws IOException {
        http = (HttpURLConnection) url.openConnection();
        http.setConnectTimeout(2000);
        http.setReadTimeout(30000);
        http.setDoOutput(false);
        http.setRequestMethod("GET");
    }

    @Override
    public void run() {
        try {
            while (true) {
                http.connect();
                InputStream in = http.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                while ((responseData = br.readLine()) != null) {
                    sb.append(responseData);
                }
                JSONObject jsonObject = (JSONObject) parser.parse(sb.toString().trim());
                dataStore.getAndSetColArray(jsonObject);
                currentDate = Util.getDate();
                dataStore.put("currentDate", currentDate);

                if (dataStore.getColumnArray() != null) {
                    for (String target : dataStore.getColumnArray() ) {
                        dataStore.put(target, String.valueOf(jsonObject.getOrDefault(target, "")));
                    }
                }

                sb.setLength(0);
                br.close();
                in.close();
                http.disconnect();
                setHttp();
                sleep(1000);
            }
        } catch (IOException e) {
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private class ShutdownForHttp extends Thread {
        @Override
        public void run() {
            System.out.println("Shutdown Signal");
            System.out.println("try http disconnect");
            http.disconnect();
            System.out.println("http disconnect complete");
        }
    }
}
