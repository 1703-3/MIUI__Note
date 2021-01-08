package net.micode.notes.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    public static String getCurrentTimeStr() {
        return getTimeStr(System.currentTimeMillis());
    }

    public static String getTimeStr(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return format.format(date);
    }

}
