package org.alessio29.savagebot.bennies;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public enum BennyColor {

    RED,
    WHITE,
    BLUE,
    GOLDEN;


    private static final Map<String, BennyColor> mapping = new HashMap<>();
    static {
        mapping.put("w", BennyColor.WHITE);
        mapping.put("b", BennyColor.BLUE );
        mapping.put("r", BennyColor.RED);
        mapping.put("g", BennyColor.GOLDEN);
    }

    public static BennyColor get(String color) {
        return mapping.get(color);
    }

    public static Map.Entry<BennyColor, Integer> parseBennies(String modifier) {

        BennyColor color = null;
        char last = modifier.trim().toLowerCase().charAt(modifier.length() -1);

        switch (last) {
            case 'w' :
                color = WHITE;
                break;
            case 'b' :
                color = BLUE;
                break;
            case 'r' :
                color = RED;
                break;
            case 'g' :
                color = GOLDEN;
                break;

        }
        Integer count = 1;
        if (modifier.length()>1) {
            try {
                count = Integer.parseInt(modifier.substring(0,modifier.length()-1));
            } catch (Exception e) {
            }
        }
        return new AbstractMap.SimpleEntry<>(color, count);
    }

}
