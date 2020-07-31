package br.ufrgs.inf.pet.dinoapi.config;

import br.ufrgs.inf.pet.dinoapi.model.app_origin.AppOriginModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppOriginConfig {
    public String getOrigin() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            final String configFile = new String(
                    Files.readAllBytes(
                            Paths.get(getClass().getClassLoader().getResource("app_origin.json").getPath())
                    )
            );

            final AppOriginModel appOrigin = objectMapper.readValue(configFile, AppOriginModel.class);
            return appOrigin.getOrigin();
        } catch (JsonProcessingException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
    }
}
