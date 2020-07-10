package br.ufrgs.inf.pet.dinoapi.utils;
import br.ufrgs.inf.pet.dinoapi.constants.TimezoneConstants;

import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DatetimeUtils {
    public static LocalDateTime convertMillisecondsToLocalDatetime(Long ms) {
        return Instant.ofEpochMilli(ms).atZone(ZoneId.of(TimezoneConstants.DefaultTimezone)).toLocalDateTime();
    }

    public static Long convertLocalDatetimeToMilliseconds(LocalDateTime date) {
        final ZonedDateTime zoneDate = date.atZone(ZoneId.of(TimezoneConstants.DefaultTimezone));
        return zoneDate.toInstant().toEpochMilli();
    }
}
