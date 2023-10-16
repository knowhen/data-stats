package org.when.stats.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.when.data.order.domain.OrderType;
import org.when.data.order.entity.OrderEntity;
import org.when.data.order.entity.OrderLineEntity;
import org.when.data.order.mapper.OrderEntityMapper;
import org.when.data.order.mapper.OrderLineEntityMapper;
import org.when.util.Lambdas;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final OrderEntityMapper orderMapper;
    private final OrderLineEntityMapper orderLineMapper;

    public Map<Integer, List<OrderEntity>> findCompletedBundleOrderMapBetween(LocalDateTime startTime, LocalDateTime endTime, Set<Integer> bundleIds) {
        return findCompletedBundleOrdersBetween(startTime, endTime, bundleIds)
                .stream()
                .collect(groupingBy(OrderEntity::getBundleId));
    }

    private List<OrderEntity> findCompletedBundleOrdersBetween(LocalDateTime startTime, LocalDateTime endTime, Set<Integer> bundleIds) {
        LambdaQueryWrapper<OrderEntity> completedBundleOrders = Wrappers.lambdaQuery(OrderEntity.class)
                .in(OrderEntity::getBundleId, bundleIds)
                .eq(OrderEntity::getOrderStatus, 1)
                .between(OrderEntity::getCreateTime, startTime, endTime);
        return orderMapper.selectList(completedBundleOrders);
    }

    public Map<Integer, OrderLineEntity> findCompletedProductOrderMapBetween(LocalDateTime startTime, LocalDateTime endTime) {
        List<OrderEntity> completedProductOrders = findCompletedProductOrdersBetween(startTime, endTime);
        List<String> orderIds = Lambdas.map(completedProductOrders, OrderEntity::getId);
        List<OrderLineEntity> orderLines = findAllOrderLines(orderIds);

        return orderLines.stream()
                .collect(toMap(OrderLineEntity::getProductId, Function.identity(), merge()));
    }

    public Map<Integer, OrderLineEntity> findCompletedProductOrderMapBetween(LocalDateTime startTime, LocalDateTime endTime, OrderType orderType) {
        List<OrderEntity> completedProductOrders = findCompletedProductOrdersBetween(startTime, endTime, orderType);
        List<String> orderIds = Lambdas.map(completedProductOrders, OrderEntity::getId);
        List<OrderLineEntity> orderLines = findAllOrderLines(orderIds);

        return orderLines.stream()
                .collect(toMap(OrderLineEntity::getProductId, Function.identity(), merge()));
    }

    // 合并product id相同的OrderLine
    private BinaryOperator<OrderLineEntity> merge() {
        return (t, u) -> {
            OrderLineEntity result = new OrderLineEntity();
            result.setProductId(t.getProductId());
            result.setProductName(t.getProductName());
            BigDecimal price = Objects.nonNull(t.getPrice()) ? t.getPrice() : u.getPrice();
            result.setPrice(price);
            result.setQuantity(t.getQuantity() + u.getQuantity());
            result.setTotalPrice(t.getTotalPrice().add(u.getTotalPrice()));
            return result;
        };
    }

    private List<OrderLineEntity> findAllOrderLines(List<String> orderIds) {
        if (orderIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<OrderLineEntity> byOrderIds = Wrappers.lambdaQuery(OrderLineEntity.class)
                .in(OrderLineEntity::getOrderId, orderIds);
        return orderLineMapper.selectList(byOrderIds);
    }

    public List<OrderEntity> findCompletedProductOrdersBetween(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<OrderEntity> completedProductOrders = Wrappers.lambdaQuery(OrderEntity.class)
                .eq(OrderEntity::getOrderStatus, 1)
                .isNull(OrderEntity::getBundleId)
                .between(OrderEntity::getCreateTime, startTime, endTime);
        return orderMapper.selectList(completedProductOrders);
    }

    public List<OrderEntity> findCompletedProductOrdersBetween(LocalDateTime startTime, LocalDateTime endTime, OrderType orderType) {
        LambdaQueryWrapper<OrderEntity> completedProductOrders = Wrappers.lambdaQuery(OrderEntity.class)
                .eq(OrderEntity::getOrderType, orderType.getCode())
                .eq(OrderEntity::getOrderStatus, 1)
                .isNull(OrderEntity::getBundleId)
                .between(OrderEntity::getCreateTime, startTime, endTime);
        return orderMapper.selectList(completedProductOrders);
    }

    public List<OrderEntity> findCompletedOrdersBetween(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<OrderEntity> completedOrder = Wrappers.lambdaQuery(OrderEntity.class)
                .eq(OrderEntity::getOrderStatus, 1)
                .between(OrderEntity::getCreateTime, startTime, endTime);
        return orderMapper.selectList(completedOrder);
    }

    /**
     * 根据订单列表查询所有子订单
     *
     * @param orders 订单列表
     * @return 子订单列表
     */
    public List<OrderLineEntity> findAllOrderLinesOf(List<OrderEntity> orders) {
        if (orders.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> orderIds = Lambdas.map(orders, OrderEntity::getId);
        LambdaQueryWrapper<OrderLineEntity> byOrderId = Wrappers.lambdaQuery(OrderLineEntity.class)
                .in(OrderLineEntity::getOrderId, orderIds);
        List<OrderLineEntity> orderLines = orderLineMapper.selectList(byOrderId);
        return orderLines;
    }
}
