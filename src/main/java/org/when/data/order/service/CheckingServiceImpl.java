package org.when.data.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.when.data.order.entity.OrderEntity;
import org.when.data.order.entity.OrderLineEntity;
import org.when.data.order.entity.StockEntity;
import org.when.data.order.entity.VerifyRecord;
import org.when.data.order.mapper.OrderEntityMapper;
import org.when.data.order.mapper.OrderLineEntityMapper;
import org.when.data.order.mapper.StockMapper;
import org.when.data.order.mapper.VerifyRecordMapper;
import org.when.data.product.ProductEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.*;

//@Service
@RequiredArgsConstructor
public class CheckingServiceImpl implements CheckingService {
    private final OrderEntityMapper orderMapper;
    private final OrderLineEntityMapper orderLineMapper;
    private final StockMapper stockMapper;
    private final VerifyRecordMapper verifyMapper;

    @Override
    public void checkingSalesAndStocks(LocalDateTime startTime, LocalDateTime endTime) {
        Map<Integer, OrderLineEntity> salesMap = findProductSalesInTimeRange(startTime, endTime);
        Map<Integer, Integer> stocksMap = findProductStocksInTimeRange(startTime, endTime);

        System.out.println("时间：" + startTime + "到" + endTime);
        System.out.println("销售情况：");
        checkingSalesAndStocks(salesMap, stocksMap);

        BigDecimal totalAmount = calculateTotalAmount(salesMap.values());
        System.out.println("合计：" + totalAmount);
    }

    @Override
    public void countVerifiedTickets(LocalDateTime startTime, LocalDateTime endTime) {

        LambdaQueryWrapper<VerifyRecord> byTimeRange = Wrappers.lambdaQuery(VerifyRecord.class)
                .eq(VerifyRecord::getVerifyStatus, 1)
                .between(VerifyRecord::getVerifyTime, startTime, endTime);
        Integer quantity = verifyMapper.selectList(byTimeRange).stream()
                .map(VerifyRecord::getQuantity)
                .reduce(0, Integer::sum);

        System.out.println("__________________________");
        System.out.println("核销情况：");
        System.out.println("核销水票：" + quantity + "张");

        LambdaQueryWrapper<StockEntity> verifiedTickets = Wrappers.lambdaQuery(StockEntity.class)
                .eq(StockEntity::getIsTicket, true)
                .eq(StockEntity::getVerified, true);

        stockMapper.selectList(verifiedTickets).stream()
                .collect(groupingBy(StockEntity::getProductId, counting()))
                .forEach(
                        (productId, count) -> {
                            ProductEnum product = ProductEnum.fromCode(productId);
                            System.out.println("总计核销" + count + "张" + product.getDesc() + "[" + productId + "]");
                        }
                );

    }

    /**
     * 查询指定时间范围内所有商品的订单情况
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 商品id与合并后的子订单的映射表
     */
    private Map<Integer, OrderLineEntity> findProductSalesInTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        List<String> orderIds = findCompletedOrderIds(startTime, endTime);
        if (orderIds.isEmpty()) {
            return new HashMap<>();
        }

        List<OrderLineEntity> orderLines = findAllOrderLinesOf(orderIds);

        Map<Integer, OrderLineEntity> combinedOrderLines = combineOrderLinesWithSameProductId(orderLines);
        return combinedOrderLines;
    }

    /**
     * 统计指定时间范围内所有商品的库存情况
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 商品id与库存数量的映射表
     */
    private Map<Integer, Integer> findProductStocksInTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        List<StockEntity> allStocks = findStocksInTimeRange(startTime, endTime);
        return allStocks.stream()
                .collect(groupingBy(
                        StockEntity::getProductId,
                        collectingAndThen(counting(), Long::intValue)
                ));

    }

    /**
     * 核对商品销售情况与库存情况
     *
     * @param salesMap  商品销售情况映射表
     * @param stocksMap 商品库存情况映射表
     */
    private void checkingSalesAndStocks(Map<Integer, OrderLineEntity> salesMap, Map<Integer, Integer> stocksMap) {
        if (salesMap.size() != stocksMap.size()) {
            stocksMap.forEach(
                    (productId, stocks) -> {
                        System.out.println("库存异常的商品id：" + productId + " 数量： " + stocks);
                    }
            );
        }
        salesMap.forEach(
                (productId, product) -> {
                    Integer stocks = stocksMap.get(productId);
                    System.out.println("商品名称:" + product.getProductName() + "[" + product.getProductId() + "]");
                    System.out.println("订单数量：" + product.getQuantity() + " 库存数量:" + stocks);
                    System.out.println("销售金额：" + product.getTotalPrice());
                    if (!product.getQuantity().equals(stocks)) {
                        System.out.println("订单与库存数量不匹配，赶紧去核对吧！");
                        System.out.println("订单与库存数量不匹配，赶紧去核对吧！！");
                        System.out.println("订单与库存数量不匹配，赶紧去核对吧！！！");
                    }
                    System.out.println("__________________________");
                }
        );
    }

    /**
     * 计算订单总金额
     *
     * @param combinedOrderLines
     * @return 订单总金额
     */
    private BigDecimal calculateTotalAmount(Collection<OrderLineEntity> combinedOrderLines) {
        BigDecimal totalAmount = combinedOrderLines.stream()
                .filter(isNotDistributedTicket())
                .map(OrderLineEntity::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalAmount;
    }

    /**
     * 查找指定时间段内所有商品的库存
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 商品库存列表
     */
    private List<StockEntity> findStocksInTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<StockEntity> byProductId = Wrappers.lambdaQuery(StockEntity.class)
                .between(StockEntity::getCreateTime, startTime, endTime);
        return stockMapper.selectList(byProductId);
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

    /**
     * 查询指定时间范围内所有已完成的订单id
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 订单id列表
     */
    private List<String> findCompletedOrderIds(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<OrderEntity> completedOrder = Wrappers.lambdaQuery(OrderEntity.class)
                .eq(OrderEntity::getOrderStatus, 1)
                .between(OrderEntity::getCreateTime, startTime, endTime);
        List<String> orderIds = orderMapper.selectList(completedOrder).stream()
                .map(OrderEntity::getId)
                .collect(toList());
        return orderIds;
    }

    /**
     * 根据订单id列表查询所有子订单
     *
     * @param orderIds 订单id列表
     * @return 子订单列表
     */
    private List<OrderLineEntity> findAllOrderLinesOf(List<String> orderIds) {
        LambdaQueryWrapper<OrderLineEntity> byOrderId = Wrappers.lambdaQuery(OrderLineEntity.class)
                .in(OrderLineEntity::getOrderId, orderIds);
        List<OrderLineEntity> orderLines = orderLineMapper.selectList(byOrderId);
        return orderLines;
    }

    // 非分配水票断言
    private Predicate<OrderLineEntity> isNotDistributedTicket() {
        return line -> !line.getProductId().equals(8);
    }

    // 合并product id相同的OrderLine
    private BinaryOperator<OrderLineEntity> merge() {
        return (t, u) -> {
            OrderLineEntity result = new OrderLineEntity();
            result.setProductId(t.getProductId());
            result.setProductName(t.getProductName());
            result.setQuantity(t.getQuantity() + u.getQuantity());
            result.setTotalPrice(t.getTotalPrice().add(u.getTotalPrice()));
            return result;
        };
    }
}
