package org.when.stats.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.when.data.product.entity.BundleEntity;
import org.when.data.product.mapper.BundleEntityMapper;

import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Repository
@RequiredArgsConstructor
public class BundleRepository {
    private final BundleEntityMapper bundleMapper;

    public Map<Integer, BundleEntity> findEnabledBundleMap() {
        LambdaQueryWrapper<BundleEntity> enabled = Wrappers.lambdaQuery(BundleEntity.class)
                .eq(BundleEntity::getEnabled, true);
        return bundleMapper.selectList(enabled).stream()
                .collect(toMap(BundleEntity::getId, Function.identity()));
    }

    public BundleEntity findById(Integer id) {
        return bundleMapper.selectById(id);
    }
}
