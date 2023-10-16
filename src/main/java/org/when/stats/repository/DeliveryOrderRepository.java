package org.when.stats.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.when.data.delivery.domain.DeliveryStatus;
import org.when.data.delivery.entity.DeliveryOrderEntity;
import org.when.data.delivery.mapper.DeliveryOrderEntityMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class DeliveryOrderRepository {

    private final DeliveryOrderEntityMapper orderMapper;

    public List<DeliveryOrderEntity> findAllBetween(LocalDateTime startTime, LocalDateTime endTime, Set<String> watermanIds) {
        LambdaQueryWrapper<DeliveryOrderEntity> completed = Wrappers.lambdaQuery(DeliveryOrderEntity.class)
                .eq(DeliveryOrderEntity::getDeliveryStatus, DeliveryStatus.COMPLETED.getCode())
                .between(DeliveryOrderEntity::getCreateTime, startTime, endTime);
        if (!watermanIds.isEmpty()) {
            completed.in(DeliveryOrderEntity::getWatermanId, watermanIds);
        }

        return orderMapper.selectList(completed);
    }
}
