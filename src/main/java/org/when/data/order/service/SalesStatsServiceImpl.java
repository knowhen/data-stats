package org.when.data.order.service;

import lombok.RequiredArgsConstructor;
import org.when.data.order.entity.OrderEntity;
import org.when.data.order.entity.OrderLineEntity;
import org.when.data.product.entity.BundleEntity;
import org.when.data.product.entity.ProductEntity;
import org.when.stats.repository.BundleRepository;
import org.when.stats.repository.OrderRepository;
import org.when.stats.repository.ProductRepository;
import org.when.stats.vo.SalesVO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

//@Service
@RequiredArgsConstructor
public class SalesStatsServiceImpl implements SalesStatsService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final BundleRepository bundleRepository;

    @Override
    public List<SalesVO> statsSalesBetween(LocalDateTime startTime, LocalDateTime endTime) {
        Map<Integer, BundleEntity> bundleMap = bundleRepository.findAllMap();
        Map<Integer, List<OrderEntity>> bundleOrderMap = orderRepository.findCompletedBundleOrderMapBetween(startTime, endTime, bundleMap.keySet());

        List<SalesVO> bundleSales = bundleMap.values().stream()
                .map(bundle -> statsSales(bundle, bundleOrderMap))
                .collect(toList());

        Map<Integer, ProductEntity> productMap = productRepository.findEnabledProductMap();
        Map<Integer, OrderLineEntity> productOrderMap = orderRepository.findCompletedProductOrderMapBetween(startTime, endTime);

        List<SalesVO> productSales = productMap.values().stream()
                .map(product -> statsSales(product, productOrderMap))
                .collect(toList());

        return Stream.concat(productSales.stream(), bundleSales.stream()).collect(toList());
    }

    private SalesVO statsSales(BundleEntity bundle, Map<Integer, List<OrderEntity>> bundleOrderMap) {
        SalesVO result = new SalesVO();
        result.setName(bundle.getBundleName());
        result.setPrice(bundle.getPrice());

        List<OrderEntity> bundleOrders = bundleOrderMap.get(bundle.getId());
        BigDecimal amount = bundleOrders.stream()
                .map(OrderEntity::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        result.setAmount(amount);
        result.setQuantity(bundleOrderMap.size());
        return result;
    }

    private SalesVO statsSales(ProductEntity product, Map<Integer, OrderLineEntity> productOrderMap) {
        OrderLineEntity orderLine = productOrderMap.get(product.getId());

        if (Objects.nonNull(orderLine)) {
            return new SalesVO()
                    .setName(product.getProductName())
                    .setPrice(product.getPrice())
                    .setQuantity(orderLine.getQuantity())
                    .setAmount(orderLine.getTotalPrice());
        } else {
            return new SalesVO()
                    .setName(product.getProductName())
                    .setPrice(product.getPrice())
                    .setQuantity(0)
                    .setAmount(BigDecimal.ZERO);
        }
    }

}
