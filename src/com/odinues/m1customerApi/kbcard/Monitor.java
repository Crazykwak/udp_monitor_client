package com.odinues.m1customerApi.kbcard;

import java.io.IOException;

public class Monitor {
    public static void main(String[] args) throws IOException {

        Runtime.getRuntime().addShutdownHook(new Shutdown());

        boolean isHttp = true;
        if (args.length == 0) {
            System.out.println("args 입력값 없음. HTTP 모드로 기동");
        }

        if (args.length > 0) {
            String arg = args[0];
            if (arg.toUpperCase().equals("UDP")) {
                isHttp = false;
            }
        }

        System.out.println("===== 사용자 정의값 불러오기 =====");
        String columnBackGround = System.getProperty("colBgColor") == null ? "\033[47m" : "\033[" + System.getProperty("colBgColor") + "m";
        String columnColor = System.getProperty("colColor") == null ? "\033[1;30m" : "\033[1;" + System.getProperty("colColor") + "m";
        String dataColor = System.getProperty("color") == null ? "\u001B[36m" : "\u001B[" + System.getProperty("color") + "m";
        String host = System.getProperty("host") == null ? "localhost" : System.getProperty("host");
        String path = System.getProperty("path") == null ? "response" : System.getProperty("path");
        int port = System.getProperty("port") == null ? 30100 : Integer.parseInt(System.getProperty("port"));
        System.out.println(columnBackGround + columnColor + "컬럼 배경색 및 글자색" + Util.RESET);
        System.out.println(dataColor + "데이터 글자색" + Util.RESET);
        System.out.println("통신 host = " + host);
        System.out.println("통신 port = " + port);
        if (isHttp) {
            System.out.println("Http 통신으로 실행");
            System.out.println("통신 path = " + path);
        } else {
            System.out.println("UDP 모드로 실행");
        }
        System.out.println("===== 사용자 정의값 불러오기 완료 =====");

        // store 생성
        DataStore dataStore = new DataStore();

        DataReceiver dataReceiver = null;

        if (isHttp) {
            dataReceiver = new DataReceiverByHttp(host, port, path, dataStore);

        } else {
            // udp 통신 (데이터값 저장) 쓰레드 생성
            dataReceiver = new DataReceiverByUDPThread(host, port, dataStore);
        }
        dataReceiver.start();
        // 모니터링 출력 클래스 생성
        DataConsoleWriter dataConsoleWriter = new DataConsoleWriter(dataStore);

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
