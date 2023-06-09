package com.odinues.m1customerApi.kbcard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static final String RESET = "\u001B[0m";
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    // cursor move
    public static void cursorDown(int line) {
        System.out.printf("%c[%dB",0x1b,line);
    }
    public static void cursorUp(int line) {
        System.out.printf("%c[%dA",0x1b,line);
    }
    public static void refreshConsole() {
        System.out.print("\u001b[2J");
    }
    public static String getDate() {
        String date = dateFormat.format(new Date());
        return date;
    }

}