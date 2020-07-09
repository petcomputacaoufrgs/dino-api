package br.ufrgs.inf.pet.dinoapi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    public static <T> String convertObjectToJSON(T obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(obj);

        return json;
    }

}
