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
        final Instant instant = date.toInstant(ZoneOffset.UTC);
        final ZonedDateTime zonedResult = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"));
        return Date.from(zonedResult.toInstant());
    }

    public LocalDateTime toLocalDateTime(Date date) {
        final Instant instant = date.toInstant();
        final ZonedDateTime zonedResult = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"));

        return zonedResult.toLocalDateTime();
    }

    public ZonedDateTime toUTCZonedDateTime(LocalDateTime date) {
        final Instant instant = date.toInstant(ZoneOffset.UTC);
        return ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"));
    }

    public ZonedDateTime getUTCZonedDateTime() {
        final LocalDateTime date = LocalDateTime.now();
        return this.toUTCZonedDateTime(date);
    }
}
