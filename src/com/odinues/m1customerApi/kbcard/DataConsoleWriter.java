package com.odinues.m1customerApi.kbcard;

import java.util.Map;

import static java.lang.Thread.sleep;
public class DataConsoleWriter {

    private DataReceiverThread dataReceiverThread;
    private String[] columnArray;
    private final String equalLineString = "============================================================================================================\n";
    private int lineSize = 0;
    private String columnBackGround = "\033[47m";
    private String columnColor = "\033[1;30m";
    private String dataColor = "\u001B[36m";
    private Map<String, String> monitoringDataStore = null;
    private int sleepMilsTime = 500;

    public DataConsoleWriter(DataReceiverThread dataReceiverThread, Map<String, String> monitoringDataStore) {
        this.dataReceiverThread = dataReceiverThread;
        this.monitoringDataStore = monitoringDataStore;
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
        for (String column : columnArray) {
            if (column.equals("/")) {
                lineSize++;
            }
        }
    }

    private void setColumnArrayByDataReceiverThread() {
        columnArray = dataReceiverThread.getColumnArray();
    }

    /**
     * ���� �����ϴ� �Լ� while(true)�� ��� ����
     */
    public void run() {

        try {

            // ������ �κ��� ���� ���� (main���� ��� ����) �ܼ� ȭ�� �ʱ�ȭ�� �����ϰ� �̰� �����.
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            while (true) {

                if (columnArray == null) {
                    setColumnArrayByDataReceiverThread();
                    sleep(100);
                    continue;
                }

                if (lineSize == 0) {
                    setLineSize();
                }

                columnHeaderSysOut();
                // columnArray ���� "/" ���� + 1�� ��µ� ����. column + data �� �����ϸ� 1 �÷��� 2���� �ʿ�
                // �׷��Ƿ� lineSize * 2 ��ŭ Ŀ���� �ø���.
                Util.cursorUp((lineSize * 2));
                monitoringDataSysOut();
                endLineSysOut();
                // lineSize �� 2��� endLine �� ������� �����ؼ� + ���ָ� �� ���� �⺻ + 5 �� endLine 1�ٴ� +1
                Util.cursorUp((lineSize * 2) + 6);

                // �ܼ� ��� �ֱ���. sleep�� ������ �� Ŀ���� ��û�� �ӵ��� �����̱� ������
                // ������ ������ �ʿ���.
                sleep(sleepMilsTime);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void odinueLogoSysOut() {
        String date = Util.getDate();
        System.out.print(equalLineString);
        System.out.print("= Odinue M1 E2E Monitor Release 0.20                                 ����ð� " + date +"          =\n");
        System.out.print(equalLineString);
    }

    private void columnHeaderSysOut() {
        odinueLogoSysOut();

        String pad = "%20s";
        String bar = " |";
        StringBuilder sb = new StringBuilder();
        sb.append(columnBackGround);
        sb.append(columnColor);

        for (int i = 0; i < columnArray.length; i++) {
            String target = columnArray[i];

            if (target.equals("/")) {
                sb.append(Util.RESET);
                System.out.println(sb.toString());
                System.out.println();
                sb.setLength(0);
                sb.append(columnBackGround + columnColor);
                continue;
            }
            sb.append(String.format(pad, target + bar));
        }
        sb.append(Util.RESET);
        System.out.println(sb.toString());

    }

    private void monitoringDataSysOut() {
        StringBuilder sb = new StringBuilder();
        String pad = "%20s";
        String bar = "  |";

        sb.append(dataColor);
        for (int i = 0; i < columnArray.length; i++) {
            String target = columnArray[i];
            if (target.equals("/")) {
                System.out.println(sb.toString());
                Util.cursorDown(1);
                sb.setLength(0);
                continue;
            }
            String value = monitoringDataStore.get(target) == null ? "" : String.valueOf(monitoringDataStore.get(target));
            sb.append(String.format(pad, value + bar));
        }
        sb.append(Util.RESET);
        System.out.println(sb.toString());
    }
    private void endLineSysOut() {
        String currentDate = monitoringDataStore.get("currentDate");
        System.out.print("== �ֱ� ��� �ð� : " + currentDate + " =====================================================================\n");
    }
}
