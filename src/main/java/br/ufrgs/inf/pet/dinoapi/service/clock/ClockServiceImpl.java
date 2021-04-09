package br.ufrgs.inf.pet.dinoapi.service.clock;

import org.springframework.stereotype.Service;
import java.time.*;
import java.util.Date;

@Service
public class ClockServiceImpl implements ClockService {
    public Date now() {
        return new Date();
    }

    public Date nowPlusMinutes(long minutes) {
        final LocalDateTime date = LocalDateTime.now().plusMinutes(minutes);
        final ZonedDateTime zonedResult = date.atZone(ZoneId.systemDefault());
        return Date.from(zonedResult.toInstant());
    }

    public LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public LocalDateTime toLocalDateTime(Long millis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis * 1000), ZoneId.systemDefault());
    }

    public ZonedDateTime toUTCZonedDateTime(LocalDateTime date) {
        return ZonedDateTime.of(date, ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC);
    }

    public ZonedDateTime getUTCZonedDateTime() {
        final LocalDateTime date = LocalDateTime.now();
        return this.toUTCZonedDateTime(date);
    }
}
