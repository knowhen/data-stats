package org.when.stats.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface DateRangeProvider {

    default LocalDateTime getStartTime(LocalDate date) {
        return date.atStartOfDay();
    }

    default LocalDateTime getEndTime(LocalDate date) {
        return date.atTime(LocalTime.MAX);
    }
}
