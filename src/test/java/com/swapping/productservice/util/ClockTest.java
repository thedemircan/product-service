package com.swapping.productservice.util;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

public class ClockTest {

    @Test
    public void it_should_get_now() throws InterruptedException {
        //Given
        Clock.freeze(LocalDateTime.of(2069, 3, 1, 3, 1, 31));
        final LocalDateTime now = Clock.now();

        //When
        final LocalDateTime actualNow = Clock.now();

        //Then
        Assertions.assertThat(actualNow).isEqualTo(now);
        Clock.unfreeze();

    }


    @Test
    public void it_should_freeze_but_different_times() throws InterruptedException {
        //Given
        final LocalDateTime now = Clock.now();
        Clock.freeze(LocalDateTime.of(2069, 3, 1, 3, 1, 31));

        //When
        final LocalDateTime actualNow = Clock.now();

        //Then
        Assertions.assertThat(actualNow).isNotEqualTo(now);
        Clock.unfreeze();
    }

    @Test
    public void it_should_unfreeze() throws InterruptedException {
        //Given
        Clock.freeze(LocalDateTime.of(2069, 3, 1, 3, 1, 31));
        final LocalDateTime now = Clock.now();

        //When
        Clock.unfreeze();
        final LocalDateTime actualNow = Clock.now();

        //Then
        Assertions.assertThat(actualNow).isNotEqualTo(now);
    }

    @Test
    public void it_should_freeze() {
        //Given
        final Date date = DateUtils.convertLocalDateTimeToDate(Clock.now());

        //When
        Clock.freeze(date);

        //Then
        assertThat(date).isEqualTo(DateUtils.convertLocalDateTimeToDate(Clock.now()));
        Clock.unfreeze();
    }

    @Test
    public void it_should_get_utc_time_now() {
        //Given
        LocalDateTime localDateTime = LocalDateTime.now();
        Clock.freeze(localDateTime);
        LocalDateTime nowExpected = Clock.now().atZone(TimeZone.getDefault().toZoneId()).withZoneSameInstant(ZoneId.of("Z")).toLocalDateTime();

        //When
        LocalDateTime nowUtc = Clock.now(ZoneOffset.UTC);

        //Then
        assertThat(nowUtc).isEqualTo(nowExpected);
        Clock.unfreeze();
    }
}