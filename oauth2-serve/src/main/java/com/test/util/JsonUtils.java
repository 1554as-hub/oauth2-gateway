package com.test.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.util.StringUtils;

import java.io.IOException;

public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public JsonUtils(){

    }

    public static <T> T serializable(String json , Class<T> clazz) {

        if(StringUtils.isNullOrEmpty(json)) {
            return  null;
        } else {
            try {
                return objectMapper.readValue(json , clazz);
            } catch (IOException var3) {

                return  null;
            }
        }
    }

    public static <T> T serializable(String json , TypeReference<T> reference) {
        if(StringUtils.isNullOrEmpty(json)) {
            return  null;
        } else {
            try {
                return objectMapper.readValue(json , reference);
            }catch (IOException var3) {
                return  null;
            }
        }
    }

    public static String deserializer(Object json) {
        if(json == null) {
            return  null;
        } else {
            try {
                return objectMapper.writeValueAsString(json);
            }catch (JsonProcessingException var2) {
                return null;
            }
        }
    }

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES , false);
    }

}
