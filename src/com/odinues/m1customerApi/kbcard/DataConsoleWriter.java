package com.odinues.m1customerApi.kbcard;

import static java.lang.Thread.sleep;
public class DataConsoleWriter {

    private String[] columnArray;
    private final String equalLineString = "============================================================================================================\n";
    private int lineSize = 0;
    private String columnBackGround = "\033[47m";
    private String columnColor = "\033[1;30m";
    private String dataColor = "\u001B[36m";
    private String pad = "%20s";
    private String bar = "|";

    private DataStore dataStore = null;
    private int sleepMilsTime = 500;

    public DataConsoleWriter(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void setColumnBackGround(String columnBackGround) {
        this.columnBackGround = columnBackGround;
    }

    public void setColumnColor(String columnColor) {
        this.columnColor = columnColor;
    }

    public void setDataColor(String dataColor) {
        this.dataColor = dataColor;
    }

    public void setSleepMilsTime(int sleepMilsTime) {
        this.sleepMilsTime = sleepMilsTime;
    }

    private void setLineSize() {
        lineSize = 0;
        for (String column : dataStore.getColumnArray()) {
            if (column.equals("/")) {
                lineSize++;
            }
        }
    }

    /**
     * 쓰기 시작하는 함수 while(true)로 계속 실행
     */
    public void run() {

        try {

            // 세팅한 부분을 보기 위해 (main에서 찍고 있음) 콘솔 화면 초기화를 제거하고 이걸 사용함.
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            while (true) {

                if (dataStore.getColumnArray() == null) {
                    sleep(100);
                    continue;
                }

                dataStore.getColumnArray();
                setLineSize();

                columnHeaderSysOut();
                // columnArray 에서 "/" 개수 + 1이 출력될 줄임. column + data 를 생각하면 1 컬럼당 2줄이 필요
                // 그러므로 lineSize * 2 만큼 커서를 올린다.
                Util.cursorUp((lineSize * 2));
                monitoringDataSysOut();
                endLineSysOut();
                // lineSize 의 2배와 endLine 과 빈공백을 포함해서 + 해주면 딱 맞음 기본 + 5 에 endLine 1줄당 +1
                Util.cursorUp((lineSize * 2) + 6);

                // 콘솔 출력 주기임. sleep를 제거할 시 커서가 엄청난 속도로 깜빡이기 때문에
                // 적당한 조절이 필요함.
                System.out.flush();
                sleep(sleepMilsTime);
                Util.refreshConsole();

            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void odinueLogoSysOut() {
        String date = Util.getDate();
        System.out.printf(equalLineString);
        System.out.printf("= Odinue M1 E2E Monitor Release 0.20                                 현재시각 " + date +"          =\n");
        System.out.printf(equalLineString);
    }

    private void columnHeaderSysOut() {
        odinueLogoSysOut();
        StringBuilder sb = new StringBuilder();
        sb.append(columnBackGround);
        sb.append(columnColor);
        columnArray = dataStore.getColumnArray();
        for (int i = 0; i < columnArray.length; i++) {
            String target = columnArray[i];

            if (target.equals("/")) {
                sb.append(Util.RESET);
                System.out.printf(sb.toString() + "\n\n");
                sb.setLength(0);
                sb.append(columnBackGround + columnColor);
                continue;
            }
            sb.append(String.format(pad, target + bar));
        }
        sb.append(Util.RESET);
        System.out.printf(sb.toString() + "\n");

    }

    private void monitoringDataSysOut() {
        StringBuilder sb = new StringBuilder();

        sb.append(dataColor);
        for (int i = 0; i < columnArray.length; i++) {
            String target = columnArray[i];
            if (target.equals("/")) {
                System.out.println(sb.toString());
                Util.cursorDown(1);
                sb.setLength(0);
                continue;
            }
            String value = dataStore.get(target) == null ? "" : String.valueOf(dataStore.get(target));
            sb.append(String.format(pad, value + bar));
        }
        sb.append(Util.RESET);
        System.out.printf(sb.toString() + "\n");
    }
    private void endLineSysOut() {
        String currentDate = dataStore.get("currentDate");
        System.out.printf("== 최근 통신 시간 : " + currentDate + " =====================================================================\n");
    }
}
