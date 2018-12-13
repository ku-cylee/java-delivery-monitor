package cose102.delivery_monitor.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Shortcuts {
    private static String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    public static Date stringToDateTime(String string) {
        try {
            return new SimpleDateFormat(dateTimeFormat, Locale.KOREA).parse(string);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateTimeToString(Date dateTime) {
        return new SimpleDateFormat(dateTimeFormat).format(dateTime);
    }
}
