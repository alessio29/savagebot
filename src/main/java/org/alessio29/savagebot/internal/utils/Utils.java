package org.alessio29.savagebot.internal.utils;

public class Utils {

    public static Integer notNullValue(Integer value) {

        if (value == null) {
            return 0;
        }
        return value;
    }

    public static String notNullValue(String value) {

        if (value == null) {
            return "";
        }
        return value;
    }

    public static Boolean notNullValue(Boolean value) {

        if (value == null) {
            return false;
        }
        return value;
    }

}
