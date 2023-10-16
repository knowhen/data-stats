package org.when.stats.converter;


import org.when.data.order.entity.OrderEntity;
import org.when.data.order.entity.OrderLineEntity;
import org.when.data.product.entity.BundleEntity;
import org.when.data.product.entity.ProductEntity;
import org.when.stats.vo.SalesVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SalesConverter {
    public static SalesVO convert(ProductEntity product, Map<Integer, OrderLineEntity> productOrderMap) {
        OrderLineEntity orderLine = productOrderMap.get(product.getId());

        if (Objects.nonNull(orderLine)) {
            return new SalesVO()
                    .setName(product.getProductName())
                    .setPrice(product.getPrice())
                    .setQuantity(orderLine.getQuantity())
                    .setAmount(orderLine.getTotalPrice());
        } else {
            return emptySales(product.getProductName(), product.getPrice());
        }
    }

    public static SalesVO convert(BundleEntity bundle, Map<Integer, List<OrderEntity>> bundleOrderMap) {
        SalesVO result = new SalesVO();
        result.setName(bundle.getBundleName());
        result.setPrice(bundle.getPrice());

        List<OrderEntity> bundleOrders = bundleOrderMap.get(bundle.getId());
        if (Objects.nonNull(bundleOrders)) {
            BigDecimal amount = bundleOrders.stream()
                    .map(OrderEntity::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return new SalesVO()
                    .setName(bundle.getBundleName())
                    .setPrice(bundle.getPrice())
                    .setQuantity(bundleOrders.size())
                    .setAmount(amount);
        } else {
            return emptySales(bundle.getBundleName(), bundle.getPrice());
        }
    }


    private static SalesVO emptySales(String name, BigDecimal price) {
        return new SalesVO()
                .setName(name)
                .setPrice(price)
                .setQuantity(0)
                .setAmount(BigDecimal.ZERO);
    }
}
