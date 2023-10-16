package org.when.stats.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.when.data.delivery.domain.RecordStatus;
import org.when.data.delivery.entity.DeliveryRecord;
import org.when.data.delivery.mapper.DeliveryRecordMapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DeliveryRecordRepository {

    private final DeliveryRecordMapper recordMapper;

    public List<DeliveryRecord> findAllBetween(LocalDateTime startTime, LocalDateTime endTime, Collection<String> watermanPhones) {
        if (watermanPhones.isEmpty()) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<DeliveryRecord> completed = Wrappers.lambdaQuery(DeliveryRecord.class)
                .eq(DeliveryRecord::getRecordStatus, RecordStatus.CONFIRMED.getCode())
                .between(DeliveryRecord::getDeliveredTime, startTime, endTime)
                .in(DeliveryRecord::getWatermanPhone, watermanPhones);

        return recordMapper.selectList(completed);
    }

    public List<DeliveryRecord> findAllBetween(LocalDateTime startTime, LocalDateTime endTime, String watermanPhone) {
        if (!StringUtils.hasText(watermanPhone)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<DeliveryRecord> completed = Wrappers.lambdaQuery(DeliveryRecord.class)
                .eq(DeliveryRecord::getRecordStatus, RecordStatus.DELIVERED.getCode())
                .between(DeliveryRecord::getDeliveredTime, startTime, endTime)
                .eq(DeliveryRecord::getWatermanPhone, watermanPhone);

        return recordMapper.selectList(completed);
    }
}
