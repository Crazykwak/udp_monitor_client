package com.odinues.m1customerApi.kbcard;

import java.io.IOException;

public class Monitor {
    public static void main(String[] args) throws IOException {

        Runtime.getRuntime().addShutdownHook(new Shutdown());

        boolean isHttp = true;
        if (args.length == 0) {
            System.out.println("args �Է°� ����. HTTP ���� �⵿");
        }

        if (args.length > 0) {
            String arg = args[0];
            if (arg.toUpperCase().equals("UDP")) {
                isHttp = false;
            }
        }

        System.out.println("===== ����� ���ǰ� �ҷ����� =====");
        String columnBackGround = System.getProperty("colBgColor") == null ? "\033[47m" : "\033[" + System.getProperty("colBgColor") + "m";
        String columnColor = System.getProperty("colColor") == null ? "\033[1;30m" : "\033[1;" + System.getProperty("colColor") + "m";
        String dataColor = System.getProperty("color") == null ? "\u001B[36m" : "\u001B[" + System.getProperty("color") + "m";
        String host = System.getProperty("host") == null ? "localhost" : System.getProperty("host");
        String path = System.getProperty("path") == null ? "response" : System.getProperty("path");
        int port = System.getProperty("port") == null ? 30100 : Integer.parseInt(System.getProperty("port"));
        System.out.println(columnBackGround + columnColor + "�÷� ���� �� ���ڻ�" + Util.RESET);
        System.out.println(dataColor + "������ ���ڻ�" + Util.RESET);
        System.out.println("��� host = " + host);
        System.out.println("��� port = " + port);
        if (isHttp) {
            System.out.println("Http ������� ����");
            System.out.println("��� path = " + path);
        } else {
            System.out.println("UDP ���� ����");
        }
        System.out.println("===== ����� ���ǰ� �ҷ����� �Ϸ� =====");

        // store ����
        DataStore dataStore = new DataStore();

        DataReceiver dataReceiver = null;

        if (isHttp) {
            dataReceiver = new DataReceiverByHttp(host, port, path, dataStore);

        } else {
            // udp ��� (�����Ͱ� ����) ������ ����
            dataReceiver = new DataReceiverByUDPThread(host, port, dataStore);
        }
        dataReceiver.start();
        // ����͸� ��� Ŭ���� ����
        DataConsoleWriter dataConsoleWriter = new DataConsoleWriter(dataStore);

        // ��� ���� ����
        dataConsoleWriter.setColumnBackGround(columnBackGround);
        dataConsoleWriter.setColumnColor(columnColor);
        dataConsoleWriter.setDataColor(dataColor);
        dataConsoleWriter.setSleepMilsTime(500);

        // ����͸��� ���� ��� ����
        dataConsoleWriter.run();

    }

    private static class Shutdown extends Thread {
        @Override
        public void run() {
            Util.cursorDown(100);
            System.out.println(Util.RESET);
            System.out.println("Process shutdown");
        }
    }
}
