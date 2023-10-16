package org.when.data.order.service;

/**
 * 订单数据统计
 */
public interface OrderStatsService {
    // 每月、每季度和每年的总销售额
    void dailyStats();

    void weeklyStats();

    void monthlyStats();

    void yearlyStats();

    // 不同产品（水票、水桶、饮水机）的销售量和销售额
    void countByType();

}
