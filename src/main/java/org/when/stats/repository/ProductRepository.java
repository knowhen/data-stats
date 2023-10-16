package org.when.stats.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.when.data.order.domain.OrderType;
import org.when.data.product.entity.ProductEntity;
import org.when.data.product.entity.ProductType;
import org.when.data.product.mapper.ProductEntityMapper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final ProductEntityMapper productMapper;

    public Map<Integer, ProductEntity> findEnabledProductMap() {
        return findAllEnabledProducts().stream()
                .collect(toMap(ProductEntity::getId, Function.identity()));
    }

    public Map<Integer, ProductEntity> findProductMapByOrderType(OrderType orderType) {
        LambdaQueryWrapper<ProductEntity> wrapper = getEnabledProductWrapper();
        if (orderType == OrderType.T) {
            wrapper.eq(ProductEntity::getProductType, ProductType.TICKET.getCode());
        } else if (orderType == OrderType.O) {
            wrapper.and(wq -> wq.eq(ProductEntity::getProductType, ProductType.BUCKET.getCode())
                    .or()
                    .eq(ProductEntity::getProductType, ProductType.DISPENSER.getCode()));
        }
        return productMapper.selectList(wrapper).stream()
                .collect(toMap(ProductEntity::getId, Function.identity()));
    }

    private List<ProductEntity> findAllEnabledProducts() {
        LambdaQueryWrapper<ProductEntity> enabled = getEnabledProductWrapper();
        return productMapper.selectList(enabled);
    }


    private LambdaQueryWrapper<ProductEntity> getEnabledProductWrapper() {
        LambdaQueryWrapper<ProductEntity> enabled = Wrappers.lambdaQuery(ProductEntity.class)
                .eq(ProductEntity::getEnabled, true);
        return enabled;
    }

    public Map<Integer, ProductEntity> findAllProductMap() {
        return productMapper.selectList(Wrappers.emptyWrapper()).stream()
                .collect(toMap(ProductEntity::getId, Function.identity()));
    }
}
