package org.when.data.order.service;

import java.time.LocalDateTime;

/**
 * todo: 抽象出多个接口，最终通过组合的形式，创建通用的数据统计接口模板
 * 销售数据：
 * 销售额：每月、每季度和每年的总销售额，以及与前一时间段的比较。
 * 销售趋势：销售额的趋势和季节性变化。
 * 产品销售情况：不同产品（水票、水桶、饮水机）的销售量和销售额。
 *
 * 用户数据：
 * 用户注册数量：每月新增用户数量。
 * 用户活跃度：每月、每周和每日的活跃用户数量和活跃度。// 需要微信接口支持
 *
 * 产品数据：
 * 库存管理：各种产品的库存水平，以避免库存不足或过剩。
 *
 * 订单数据：
 * 订单数量：每天、每周和每月的订单数量。
 * 订单类型：不同类型的订单（新订单、重新订购、定期订购）的比例。
 */
public interface CheckingService {

    /**
     * 核对指定时间范围商品销售情况与库存情况是否一致
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    void checkingSalesAndStocks(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计指定时间范围水票核销情况
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    void countVerifiedTickets(LocalDateTime startTime, LocalDateTime endTime);
}
