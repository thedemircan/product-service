package com.swapping.productservice.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

public final class Clock {

    private static boolean isFrozen;

    private static LocalDateTime timeSet;

    private Clock() {
    }

    public static synchronized void freeze() {
        isFrozen = true;
    }

    public static synchronized void freeze(LocalDateTime date) {
        freeze();
        setTime(date);
    }

    public static synchronized void freeze(Date date) {
        freeze();
        setTime(DateUtils.convertDateToLocalDateTime(date));
    }

    public static synchronized void unfreeze() {
        isFrozen = false;
        timeSet = null;
    }

    public static LocalDateTime now() {
        LocalDateTime dateTime = LocalDateTime.now();
        if (isFrozen) {
            if (timeSet == null) {
                timeSet = dateTime;
            }
            return timeSet;
        }

        return dateTime;
    }

    public static LocalDateTime now(ZoneOffset zoneOffset) {
        LocalDateTime dateTime = LocalDateTime.now(zoneOffset);
        if (isFrozen) {
            if (timeSet == null) {
                timeSet = dateTime;
            }
            return timeSet.atZone(TimeZone.getDefault().toZoneId()).withZoneSameInstant(ZoneId.of("Z")).toLocalDateTime();
        }
        return dateTime;
    }

    public static synchronized void setTime(LocalDateTime date) {
        timeSet = date;
    }

}
