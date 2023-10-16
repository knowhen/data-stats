package org.when.stats.service;

import org.when.data.order.domain.OrderType;
import org.when.stats.vo.OrderStatsChartVO;
import org.when.stats.vo.OrderStatsFormVO;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;

public interface OrderStatsService {

    OrderStatsFormVO dailyStats(LocalDate day);

    OrderStatsFormVO monthlyStats(YearMonth month);

    OrderStatsChartVO yearlyStats(Year year, OrderType orderType);

}
