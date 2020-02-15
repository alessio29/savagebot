package org.alessio29.savagebot.internal.utils;

import org.alessio29.savagebot.characters.State;

import java.util.HashSet;
import java.util.Set;

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

    public static boolean isIntegerNumber(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static Set notNullValue(Set set) {
        if (set == null) {
            return new HashSet();
        }
        return set;
    }
}
