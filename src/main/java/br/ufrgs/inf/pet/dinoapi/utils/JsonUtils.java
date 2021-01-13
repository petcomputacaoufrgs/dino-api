package br.ufrgs.inf.pet.dinoapi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    public static <T> String convertToJson(T obj) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(obj);
    }

    public static <T> T convertJsonToObj(String json, Class<T> valueType) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper.readValue(json, valueType);
    }
}