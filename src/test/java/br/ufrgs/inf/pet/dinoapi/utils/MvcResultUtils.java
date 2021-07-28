package br.ufrgs.inf.pet.dinoapi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

public class MvcResultUtils {
    public static <T> T getResponseBodyFromResult(MvcResult result, Class<T> tClass) throws UnsupportedEncodingException, JsonProcessingException {
        final String stringContent = result.getResponse().getContentAsString();

        return JsonUtils.convertJsonToObj(stringContent, tClass, JsonMapperUtils.clientObjectMapper());
    }
}
