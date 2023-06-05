package com;

import java.io.IOException;
import java.util.Map;

public class Monitor {
    public static void main(String[] args) throws IOException {

        Runtime.getRuntime().addShutdownHook(new Shutdown());

        System.out.println("===== ����� ���ǰ� �ҷ����� =====");
        String columnBackGround = System.getProperty("colBgColor") == null ? "\033[47m" : "\033[" + System.getProperty("colBgColor") + "m";
        String columnColor = System.getProperty("colColor") == null ? "\033[1;30m" : "\033[1;" + System.getProperty("colColor") + "m";
        String dataColor = System.getProperty("color") == null ? "\u001B[36m" : "\u001B[" + System.getProperty("color") + "m";
        String host = System.getProperty("host") == null ? "localhost" : System.getProperty("host");
        int port = System.getProperty("port") == null ? 30100 : Integer.parseInt(System.getProperty("port"));

        System.out.println(columnBackGround + columnColor + "�÷� ���� �� ���ڻ�" + Util.RESET);
        System.out.println(dataColor + "������ ���ڻ�" + Util.RESET);
        System.out.println("��� host = " + host);
        System.out.println("��� port = " + port);
        System.out.println("===== ����� ���ǰ� �ҷ����� �Ϸ� =====");
        // udp ��� (�����Ͱ� ����) ������ ����
        DataReceiverThread dataReceiverThread = new DataReceiverThread(host, port);
        Map<String, String> monitoringDataStore = dataReceiverThread.getMonitoringDataStore();
        // udp ��� ������ ����
        dataReceiverThread.start();

        // ����͸� ��� Ŭ���� ����
        DataConsoleWriter dataConsoleWriter = new DataConsoleWriter(dataReceiverThread, monitoringDataStore);

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
