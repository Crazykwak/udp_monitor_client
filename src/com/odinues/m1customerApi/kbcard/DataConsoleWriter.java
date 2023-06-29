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
     * ���� �����ϴ� �Լ� while(true)�� ��� ����
     */
    public void run() {

        try {

            // ������ �κ��� ���� ���� (main���� ��� ����) �ܼ� ȭ�� �ʱ�ȭ�� �����ϰ� �̰� �����.
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            while (true) {

                if (dataStore.getColumnArray() == null) {
                    sleep(100);
                    continue;
                }

                dataStore.getColumnArray();
                setLineSize();

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
        System.out.printf("= Odinue M1 E2E Monitor Release 0.20                                 ����ð� " + date +"          =\n");
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
        System.out.printf("== �ֱ� ��� �ð� : " + currentDate + " =====================================================================\n");
    }
}
