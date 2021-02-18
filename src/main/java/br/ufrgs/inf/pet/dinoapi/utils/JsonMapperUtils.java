package br.ufrgs.inf.pet.dinoapi.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import java.io.IOException;
import java.time.ZonedDateTime;

public class JsonMapperUtils {
    public static ObjectMapper clientObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(
                new SimpleModule("foo")
                        .addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer())
                        .addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer())
        );
        return mapper;
    }

    public static class ZonedDateTimeSerializer extends StdScalarSerializer<ZonedDateTime> {
        public ZonedDateTimeSerializer() {
            super(ZonedDateTime.class);
        }

        @Override
        public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            final String output = value.toString();
            gen.writeString(output);
        }
    }

    public static class ZonedDateTimeDeserializer extends StdScalarDeserializer<ZonedDateTime> {
        public ZonedDateTimeDeserializer() {
            super(ZonedDateTime.class);
        }

        @Override
        public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) {
            try {
                return ZonedDateTime.parse(p.getValueAsString());
            } catch (Exception e) {
                return null;
            }
        }
    }
}
