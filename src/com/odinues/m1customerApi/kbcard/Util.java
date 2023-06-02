package com.odinues.m1customerApi.kbcard;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static final String RESET = "\u001B[0m";

    // cursor move
    public static void cursorDown(int line) {
        System.out.printf("%c[%dB",0x1b,line);
    }
    public static void cursorUp(int line) {
        System.out.printf("%c[%dA",0x1b,line);
    }
    public static void refreshConsole() {
        System.out.printf("%c[%dJ",0x1b);
    }
    public static String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = dateFormat.format(new Date());
        return date;
    }

}