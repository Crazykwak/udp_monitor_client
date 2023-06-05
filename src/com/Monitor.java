package com;

import java.io.IOException;
import java.util.Map;

public class Monitor {
    public static void main(String[] args) throws IOException {

        Runtime.getRuntime().addShutdownHook(new Shutdown());

        System.out.println("===== 사용자 정의값 불러오기 =====");
        String columnBackGround = System.getProperty("colBgColor") == null ? "\033[47m" : "\033[" + System.getProperty("colBgColor") + "m";
        String columnColor = System.getProperty("colColor") == null ? "\033[1;30m" : "\033[1;" + System.getProperty("colColor") + "m";
        String dataColor = System.getProperty("color") == null ? "\u001B[36m" : "\u001B[" + System.getProperty("color") + "m";
        String host = System.getProperty("host") == null ? "localhost" : System.getProperty("host");
        int port = System.getProperty("port") == null ? 30100 : Integer.parseInt(System.getProperty("port"));

        System.out.println(columnBackGround + columnColor + "컬럼 배경색 및 글자색" + Util.RESET);
        System.out.println(dataColor + "데이터 글자색" + Util.RESET);
        System.out.println("통신 host = " + host);
        System.out.println("통신 port = " + port);
        System.out.println("===== 사용자 정의값 불러오기 완료 =====");
        // udp 통신 (데이터값 저장) 쓰레드 생성
        DataReceiverThread dataReceiverThread = new DataReceiverThread(host, port);
        Map<String, String> monitoringDataStore = dataReceiverThread.getMonitoringDataStore();
        // udp 통신 쓰레드 시작
        dataReceiverThread.start();

        // 모니터링 출력 클래스 생성
        DataConsoleWriter dataConsoleWriter = new DataConsoleWriter(dataReceiverThread, monitoringDataStore);

        // 출력 색상 세팅
        dataConsoleWriter.setColumnBackGround(columnBackGround);
        dataConsoleWriter.setColumnColor(columnColor);
        dataConsoleWriter.setDataColor(dataColor);
        dataConsoleWriter.setSleepMilsTime(500);

        // 모니터링을 위한 출력 시작
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
