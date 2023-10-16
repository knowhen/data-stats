package org.when.stats.service;

import org.when.stats.vo.DormDeliveryStatsResult;
import org.when.stats.vo.StaffStatsResult;

import java.time.LocalDate;

public interface DeliveryStatsService extends DateRangeProvider {
    /**
     * 教职工送水统计
     *
     * @param day       日期
     * @param staffType 工作人员类型
     * @return 送水统计结果
     */
    StaffStatsResult deliveryDailyStatsByType(Integer staffType, LocalDate day);

    /**
     * 教职工送水统计
     *
     * @param day   日期
     * @param phone 水工手机号
     * @return 送水统计结果
     */
    StaffStatsResult deliveryDailyStatsByPhone(String phone, LocalDate day);

    /**
     * 宿舍楼送水统计
     *
     * @param day           日期
     * @param watermanPhone 水工手机号
     * @return 送水统计结果
     */
    DormDeliveryStatsResult dormDeliveryDailyStats(LocalDate day, String watermanPhone);
}
