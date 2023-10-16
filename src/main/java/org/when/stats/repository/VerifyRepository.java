package org.when.stats.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.when.data.order.domain.VerifyStatus;
import org.when.data.order.entity.VerifyRecord;
import org.when.data.order.mapper.VerifyRecordMapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class VerifyRepository {
    private final VerifyRecordMapper verifyMapper;

    public List<VerifyRecord> findAllBetween(LocalDateTime startTime, LocalDateTime endTime, Collection<String> checkmanPhones) {
        if (checkmanPhones.isEmpty()) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<VerifyRecord> completed = Wrappers.lambdaQuery(VerifyRecord.class)
                .eq(VerifyRecord::getVerifyStatus, VerifyStatus.COMPLETED.getCode())
                .between(VerifyRecord::getVerifyTime, startTime, endTime)
                .in(VerifyRecord::getCheckmanPhone, checkmanPhones);

        return verifyMapper.selectList(completed);
    }

    public List<VerifyRecord> findAllBetween(LocalDateTime startTime, LocalDateTime endTime, String checkmanPhone) {
        if (!StringUtils.hasText(checkmanPhone)) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<VerifyRecord> completed = Wrappers.lambdaQuery(VerifyRecord.class)
                .eq(VerifyRecord::getVerifyStatus, VerifyStatus.COMPLETED.getCode())
                .between(VerifyRecord::getVerifyTime, startTime, endTime)
                .eq(VerifyRecord::getCheckmanPhone, checkmanPhone);

        return verifyMapper.selectList(completed);
    }
}
