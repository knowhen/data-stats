package org.when.stats.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.when.data.order.entity.OrderEntity;
import org.when.data.order.entity.OrderLineEntity;
import org.when.data.product.entity.ProductEntity;
import org.when.stats.converter.IncomeConverter;
import org.when.stats.repository.OrderRepository;
import org.when.stats.repository.ProductRepository;
import org.when.stats.service.IncomeStatsService;
import org.when.stats.vo.IncomeStatsResult;
import org.when.stats.vo.IncomeStatsVO;
import org.when.util.Lambdas;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class IncomeStatsServiceImpl implements IncomeStatsService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public IncomeStatsResult dailyStats(LocalDate date) {
        // 订单统计
        List<IncomeStatsVO> stats = new ArrayList<>();

        Map<Integer, ProductEntity> productMap = getProductMap();

        List<OrderEntity> completedOrders = getCompletedOrders(getStartTime(date), getEndTime(date));
        List<OrderLineEntity> allOrderLines = getAllOrderLinesOf(completedOrders);
        Map<String, List<OrderLineEntity>> orderLineMap = groupingByOrderId(allOrderLines);

        IncomeStatsVO dailyIncome;
        for (OrderEntity order : completedOrders) {
            List<OrderLineEntity> orderLines = getOrderLines(orderLineMap, order);
            List<ProductEntity> products = getOrderProducts(productMap, orderLines);
            dailyIncome = IncomeConverter.convert(date, order, orderLines, products);
            stats.add(dailyIncome);
        }

        // 合计
        StringBuilder report = new StringBuilder();
        Map<Integer, OrderLineEntity> combinedOrderLines = combineOrderLinesWithSameProductId(allOrderLines);
        Collection<OrderLineEntity> orderLines = combinedOrderLines.values();
        BigDecimal totalAmount = orderLines.stream()
                .map(OrderLineEntity::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        for (OrderLineEntity orderLine : orderLines) {
            report.append("商品名称：" + orderLine.getProductName())
                    .append("，商品单价：" + orderLine.getPrice())
                    .append("，订单数量：" + orderLine.getQuantity())
                    .append("，销售金额：" + orderLine.getTotalPrice())
                    .append("\n");
        }
        report.append("合计收款：" + totalAmount);

        return new IncomeStatsResult()
                .setStats(stats)
                .setReport(report.toString());
    }

    private List<ProductEntity> getOrderProducts(Map<Integer, ProductEntity> productMap, List<OrderLineEntity> orderLines) {
        List<Integer> productIds = Lambdas.map(orderLines, OrderLineEntity::getProductId);
        List<ProductEntity> products = Lambdas.filter(productMap.values(), product -> productIds.contains(product));
        return products;
    }

    private List<OrderLineEntity> getOrderLines(Map<String, List<OrderLineEntity>> orderLineMap, OrderEntity order) {
        List<OrderLineEntity> orderLines = orderLineMap.get(order.getId());
        return orderLines;
    }

    private Map<Integer, ProductEntity> getProductMap() {
        Map<Integer, ProductEntity> productMap = productRepository.findAllProductMap();
        return productMap;
    }

    private Map<String, List<OrderLineEntity>> groupingByOrderId(List<OrderLineEntity> orderLines) {
        return orderLines.stream()
                .collect(groupingBy(OrderLineEntity::getOrderId));
    }

    private List<OrderLineEntity> getAllOrderLinesOf(List<OrderEntity> completedOrders) {
        return orderRepository.findAllOrderLinesOf(completedOrders);
    }

    private List<OrderEntity> getCompletedOrders(LocalDateTime startTime, LocalDateTime endTime) {
        List<OrderEntity> completedOrders = orderRepository.findCompletedOrdersBetween(startTime, endTime);
        return completedOrders;
    }

    /**
     * 合并列表中商品id相同的子订单
     *
     * @param orderLines 子订单列表
     * @return 商品id与合并后子订单的映射表
     */
    private Map<Integer, OrderLineEntity> combineOrderLinesWithSameProductId(List<OrderLineEntity> orderLines) {
        return orderLines.stream()
                .collect(toMap(OrderLineEntity::getProductId, Function.identity(), merge()));
    }

    // 合并product id相同的OrderLine
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


}
