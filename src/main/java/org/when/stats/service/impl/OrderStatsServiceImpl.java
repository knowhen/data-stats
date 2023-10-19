package org.when.stats.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.when.data.order.domain.OrderType;
import org.when.data.order.entity.OrderEntity;
import org.when.data.order.entity.OrderLineEntity;
import org.when.data.product.entity.BundleEntity;
import org.when.data.product.entity.ProductEntity;
import org.when.stats.converter.SalesConverter;
import org.when.stats.repository.BundleRepository;
import org.when.stats.repository.OrderRepository;
import org.when.stats.repository.ProductRepository;
import org.when.stats.service.OrderStatsService;
import org.when.stats.vo.LineChartVO;
import org.when.stats.vo.OrderStatsChartVO;
import org.when.stats.vo.OrderStatsFormVO;
import org.when.stats.vo.SalesVO;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class OrderStatsServiceImpl implements OrderStatsService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final BundleRepository bundleRepository;

    @Override
    public OrderStatsFormVO dailyStats(LocalDate day) {
        LocalDateTime startTime = getStartTime(day);
        LocalDateTime endTime = getEndTime(day);
        List<SalesVO> sales = statsSalesBetween(startTime, endTime);
        return convertToStatsVO(sales);
    }

    private static OrderStatsFormVO convertToStatsVO(List<SalesVO> sales) {
        BigDecimal totalAmount = sales.stream()
                .map(SalesVO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new OrderStatsFormVO()
                .setTotalAmount(totalAmount)
                .setSales(sales);
    }

    @Override
    public OrderStatsFormVO monthlyStats(YearMonth month) {
        LocalDateTime startTime = getStartTime(month.atDay(1));
        LocalDateTime endTime = getEndTime(month.atEndOfMonth());
        List<SalesVO> sales = statsSalesBetween(startTime, endTime);
        return convertToStatsVO(sales);
    }

    @Override
    public OrderStatsFormVO yearlyStats(Year year) {
        LocalDateTime startTime = getStartTime(year.atDay(1));
        LocalDateTime endTime = getEndTime(LocalDate.of(year.getValue(), Month.DECEMBER, 31));
        List<SalesVO> sales = statsSalesBetween(startTime, endTime);
        return convertToStatsVO(sales);
    }

    public OrderStatsFormVO monthlyStats(YearMonth month, OrderType orderType) {
        LocalDateTime startTime = getStartTime(month.atDay(1));
        LocalDateTime endTime = getEndTime(month.atEndOfMonth());
        List<SalesVO> sales = statsSalesBetween(startTime, endTime, orderType);
        return convertToStatsVO(sales);
    }

    @Override
    public OrderStatsChartVO yearlyStats(Year year, OrderType orderType) {
        List<YearMonth> months = getMonths(year);
        List<Map<String, Object>> data = months.stream()
                .map(month -> {
                    List<SalesVO> sales = monthlyStats(month, orderType).getSales();
                    return convertToLineChartData(month, sales);
                }).collect(toList());

        Set<String> keys = data.get(0).keySet().stream()
                .filter(key -> !key.equals("type"))
                .collect(toSet());

        LineChartVO lineChartVO = new LineChartVO()
                .setKeys(keys)
                .setData(data);

        BigDecimal totalAmount = months.stream()
                .map(month -> monthlyStats(month, orderType).getSales())
                .flatMap(List::stream)
                .map(SalesVO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrderStatsChartVO()
                .setLineChart(lineChartVO)
                .setTotalAmount(totalAmount);
    }

    @Override
    public List<SalesVO> bundleSalesStats() {
        return statsBundleSalesBetween(LocalDateTime.of(LocalDate.MIN, LocalTime.MIN), LocalDateTime.now());
    }

    @Override
    public List<SalesVO> productSalesStats() {
        return statsProductSalesBetween(LocalDateTime.of(LocalDate.MIN, LocalTime.MIN), LocalDateTime.now());
    }

    private List<YearMonth> getMonths(Year year) {
        List<YearMonth> months = new ArrayList<>();
        for (Month month : Month.values()) {
            // 设置月份，保持年份不变
            YearMonth yearMonth = year.atMonth(month);
            months.add(yearMonth);
        }
        return months;
    }

    private Map<String, Object> convertToLineChartData(YearMonth month, List<SalesVO> sales) {
        Map<String, Object> data = sales.stream()
                .collect(toMap(keyFunction(), SalesVO::getQuantity));
        data.put("type", month.toString());

        return data;
    }

    private static Function<SalesVO, String> keyFunction() {
        return e -> e.getName() + "￥" + e.getPrice().intValue();
    }

    private LocalDateTime getStartTime(LocalDate date) {
        return date.atStartOfDay();
    }

    private LocalDateTime getEndTime(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MAX);
    }

    private List<SalesVO> statsSalesBetween(LocalDateTime startTime, LocalDateTime endTime) {
        List<SalesVO> bundleSales = statsBundleSalesBetween(startTime, endTime);

        List<SalesVO> productSales = statsProductSalesBetween(startTime, endTime);

        return Stream.concat(productSales.stream(), bundleSales.stream()).collect(toList());
    }

    private List<SalesVO> statsSalesBetween(LocalDateTime startTime, LocalDateTime endTime, OrderType orderType) {
        if (orderType == OrderType.B) {
            return statsBundleSalesBetween(startTime, endTime);
        }

        if (orderType == OrderType.T || orderType == OrderType.O) {
            return statsProductSalesBetween(startTime, endTime, orderType);
        }

        throw new RuntimeException("不支持的订单类型：" + orderType.getType());
    }

    private List<SalesVO> statsProductSalesBetween(LocalDateTime startTime, LocalDateTime endTime) {
        Map<Integer, ProductEntity> productMap = productRepository.findEnabledProductMap();
        Map<Integer, OrderLineEntity> productOrderMap = orderRepository.findCompletedProductOrderMapBetween(startTime, endTime);

        List<SalesVO> productSales = productMap.values().stream()
                .map(product -> SalesConverter.convert(product, productOrderMap))
                .collect(toList());
        return productSales;
    }

    private List<SalesVO> statsProductSalesBetween(LocalDateTime startTime, LocalDateTime endTime, OrderType orderType) {
        Map<Integer, ProductEntity> productMap = productRepository.findProductMapByOrderType(orderType);
        Map<Integer, OrderLineEntity> productOrderMap = orderRepository.findCompletedProductOrderMapBetween(startTime, endTime, orderType);

        List<SalesVO> productSales = productMap.values().stream()
                .map(product -> SalesConverter.convert(product, productOrderMap))
                .collect(toList());
        return productSales;
    }

    private List<SalesVO> statsBundleSalesBetween(LocalDateTime startTime, LocalDateTime endTime) {
        Map<Integer, BundleEntity> bundleMap = bundleRepository.findEnabledBundleMap();
        Map<Integer, List<OrderEntity>> bundleOrderMap = orderRepository.findCompletedBundleOrderMapBetween(startTime, endTime, bundleMap.keySet());

        List<SalesVO> bundleSales = bundleMap.values().stream()
                .map(bundle -> SalesConverter.convert(bundle, bundleOrderMap))
                .collect(toList());
        return bundleSales;
    }


}
