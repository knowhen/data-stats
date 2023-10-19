package org.when.stats.service;

import org.when.data.order.domain.OrderType;
import org.when.stats.vo.OrderStatsChartVO;
import org.when.stats.vo.OrderStatsFormVO;
import org.when.stats.vo.SalesVO;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

public interface OrderStatsService {

    OrderStatsFormVO dailyStats(LocalDate day);

    OrderStatsFormVO monthlyStats(YearMonth month);

    OrderStatsFormVO yearlyStats(Year year);

    OrderStatsChartVO yearlyStats(Year year, OrderType orderType);

    List<SalesVO> bundleSalesStats();

    List<SalesVO> productSalesStats();

}
