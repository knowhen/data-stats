package org.when.stats.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.when.data.staff.entity.StaffEntity;
import org.when.data.staff.mapper.StaffEntityMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StaffRepository {

    private final StaffEntityMapper staffMapper;

    public List<StaffEntity> findByType(Integer typeCode) {
        LambdaQueryWrapper<StaffEntity> byType = Wrappers.lambdaQuery(StaffEntity.class)
                .eq(StaffEntity::getStaffType, typeCode);
        return staffMapper.selectList(byType);
    }

    public StaffEntity findByPhone(String phone) {
        LambdaQueryWrapper<StaffEntity> byPhone = Wrappers.lambdaQuery(StaffEntity.class)
                .eq(StaffEntity::getPhone, phone);
        return staffMapper.selectOne(byPhone);
    }
}
