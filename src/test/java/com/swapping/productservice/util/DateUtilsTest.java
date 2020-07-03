package com.swapping.productservice.util;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class DateUtilsTest {

    @Test
    public void it_should_convert_local_date_time_to_date() {
        //Given
        LocalDateTime ldt = LocalDateTime.of(2019, 3, 1, 3, 1, 31);
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());

        //When
        final Date date = DateUtils.convertLocalDateTimeToDate(ldt);

        //Then
        assertThat(date.getTime()).isEqualTo(zdt.toInstant().toEpochMilli());
    }

    @Test
    public void it_should_convert_date_to_local_date_time() {
        // Given
        Date date = new Date();

        // When
        LocalDateTime localDateTime = DateUtils.convertDateToLocalDateTime(date);

        // Then
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        assertThat(zdt.toInstant().toEpochMilli()).isEqualTo(date.getTime());

    }

    @Test
    public void it_should_get_string_date_by_date() {
        // Given
        Clock.freeze();
        final LocalDateTime dateTime = LocalDateTime.of(2019, 12, 25, 5, 5, 5, 5);

        // When
        final String stringDate = DateUtils.getStringDateByLocalDateTime(dateTime);

        // Then
        assertThat(stringDate).isEqualTo("25.12.2019");
        Clock.unfreeze();
    }
}