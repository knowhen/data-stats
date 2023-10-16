package org.when.data.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.when.data.order.domain.SalesStats;
import org.when.data.order.entity.OrderEntity;
import org.when.data.order.entity.OrderLineEntity;
import org.when.data.order.mapper.OrderEntityMapper;
import org.when.data.order.mapper.OrderLineEntityMapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

//@Service
@RequiredArgsConstructor
public class OrderStatsServiceImpl implements OrderStatsService {
    private final OrderEntityMapper orderMapper;
    private final OrderLineEntityMapper orderLineMapper;

    @Override
    public void dailyStats() {
        // 商品订单列表和套餐订单列表
        // 商品订单每种商品每天的销量、销售额
        // 套餐订单每种套餐每天的销量、销售额
        List<OrderEntity> normalOrders = findAllNormalOrders();

        Map<LocalDate, List<OrderEntity>> dailyNormalOrderMap = normalOrders.stream()
                .collect(groupingBy(OrderEntity::getCreateTimeAsLocalDate));

        dailyNormalOrderMap.forEach(
                (date, dailyOrders) -> {
                    List<String> orderIds = dailyOrders.stream()
                            .map(OrderEntity::getId)
                            .collect(toList());
                    List<OrderLineEntity> orderLines = findAllOrderLines(orderIds);
                    Map<Integer, OrderLineEntity> dailyProductSalesMap = orderLines.stream()
                            .collect(toMap(OrderLineEntity::getProductId, Function.identity(), merge()));
                    System.out.println("日期：" + date);
                    List<SalesStats> salesStats = dailyProductSalesMap.values().stream()
                            .map(e -> new SalesStats(e.getProductName(), e.getPrice(), e.getQuantity()))
                            .collect(toList());
                }
        );

        List<OrderEntity> bundleOrders = findAllBundleOrders();

        Map<LocalDate, Collection<SalesStats>> dailyBundleSalesMap = bundleOrders.stream()
                .collect(
                        groupingBy(
                                OrderEntity::getCreateTimeAsLocalDate,
                                collectingAndThen(
                                        groupingBy(
                                                OrderEntity::getBundleId,
                                                mapping(
                                                        order -> new SalesStats(order.getRemark(), order.getTotalPrice(), 1),
                                                        reducing(new SalesStats(), SalesStats::combine)
                                                )
                                        ),
                                        Map::values
                                )
                        ));

        dailyBundleSalesMap.forEach(
                (date, salesStats) -> {
                    System.out.println("日期：" + date);
                    salesStats.forEach(System.out::println);
                }
        );
    }

    private List<OrderEntity> findAllBundleOrders() {
        LambdaQueryWrapper<OrderEntity> isBundle = Wrappers.lambdaQuery(OrderEntity.class)
                .isNotNull(OrderEntity::getBundleId);
        return orderMapper.selectList(isBundle);
    }

    private List<OrderEntity> findAllNormalOrders() {
        LambdaQueryWrapper<OrderEntity> notBundle = Wrappers.lambdaQuery(OrderEntity.class)
                .isNull(OrderEntity::getBundleId);
        return orderMapper.selectList(notBundle);
    }

    private List<OrderLineEntity> findAllOrderLines(List<String> orderIds) {
        if (orderIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<OrderLineEntity> byOrderIds = Wrappers.lambdaQuery(OrderLineEntity.class)
                .in(OrderLineEntity::getOrderId, orderIds);
        return orderLineMapper.selectList(byOrderIds);
    }

    private BinaryOperator<OrderLineEntity> merge() {
        return (t, u) -> {
            OrderLineEntity result = new OrderLineEntity();
            result.setProductId(t.getProductId());
            result.setProductName(t.getProductName());
            result.setPrice(t.getPrice());
            result.setQuantity(t.getQuantity() + u.getQuantity());
            result.setTotalPrice(t.getTotalPrice().add(u.getTotalPrice()));
            return result;
        };
    }

    @Override
    public void weeklyStats() {
        //
    }

    @Override
    public void monthlyStats() {

    }

    @Override
    public void yearlyStats() {

    }

    @Override
    public void countByType() {

    }
}
