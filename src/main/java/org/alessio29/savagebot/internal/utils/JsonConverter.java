package org.alessio29.savagebot.internal.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {

    public static JsonConverter INSTANCE = new JsonConverter();

    private static ObjectMapper mapper = new ObjectMapper();


    private JsonConverter() {
        super();
    }

    public static JsonConverter getInstance() {
        return INSTANCE;
    }

    public <T> T fromJson(String json,  Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public String toJson(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            System.out.println(e.toString());
            return null;
        }
    }
}
