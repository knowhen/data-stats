package org.when.stats.service;

import org.when.stats.vo.IncomeStatsResult;

import java.time.LocalDate;

public interface IncomeStatsService extends DateRangeProvider {
    IncomeStatsResult dailyStats(LocalDate day);

}
