package br.ufrgs.inf.pet.dinoapi.configuration;

import com.google.gson.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class GsonConfig {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS'Z'");

    static class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(dateTimeFormatter.format(localDateTime));
        }
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), dateTimeFormatter);
        }
    }

    static class LocalDateSerializer implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        @Override
        public JsonElement serialize(LocalDate localDateTime, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(dateFormatter.format(localDateTime));
        }
        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDate.parse(json.getAsString(), dateFormatter);
        }
    }

    static class LocalTimeSerializer implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
        @Override
        public JsonElement serialize(LocalTime localDateTime, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(timeFormatter.format(localDateTime));
        }
        @Override
        public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalTime.parse(json.getAsString(), timeFormatter);
        }
    }

    static class LongSerializer implements JsonSerializer<Long>, JsonDeserializer<Long> {
        @Override
        public JsonElement serialize(Long value, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(value);
        }
        @Override
        public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return Long.parseLong(json.getAsString());
        }
    }

    static class IntegerSerializer implements JsonSerializer<Integer>, JsonDeserializer<Integer> {
        @Override
        public JsonElement serialize(Integer value, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(value);
        }
        @Override
        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return Integer.parseInt(json.getAsString());
        }
    }

    @Bean
    GsonBuilder gsonBuilder() {
        final GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());

        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());

        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeSerializer());

        gsonBuilder.registerTypeAdapter(Long.class, new LongSerializer());

        gsonBuilder.registerTypeAdapter(Integer.class, new IntegerSerializer());

        return gsonBuilder;
    }
}

