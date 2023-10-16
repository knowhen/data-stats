package org.when.stats.service;

import org.when.stats.vo.StaffStatsResult;

import java.time.LocalDate;

public interface VerifyStatsService extends DateRangeProvider {
    StaffStatsResult dailyStats(Integer staffType, LocalDate day);

    StaffStatsResult dailyStats(String phone, LocalDate day);

}
