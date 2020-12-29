package br.ufrgs.inf.pet.dinoapi.service.clock;

import io.jsonwebtoken.Clock;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

public interface ClockService extends Clock  {
    Date nowPlusMinutes(long minutes);

    LocalDateTime toLocalDateTime(Date date);

    ZonedDateTime toUTCZonedDateTime(LocalDateTime date);
}
