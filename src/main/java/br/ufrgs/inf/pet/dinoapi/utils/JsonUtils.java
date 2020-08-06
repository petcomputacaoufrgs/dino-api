package br.ufrgs.inf.pet.dinoapi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    public static <T> String convertObjectToJSON(T obj) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();

        final String json = mapper.writeValueAsString(obj);

        return json;
    }

}
