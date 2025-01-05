package com.shuai.auth.util;

public class TableInfoContext {

    private static final ThreadLocal<String> TL = new ThreadLocal<String>();

    public static void steInfo(String info) {
        TL.set(info);
    }

    public static String getInfo() {
        return TL.get();
    }

    public static void remove() {
        TL.remove();
    }
}
