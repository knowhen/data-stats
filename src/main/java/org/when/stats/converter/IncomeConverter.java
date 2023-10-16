package org.when.stats.converter;

import org.when.data.order.entity.OrderEntity;
import org.when.data.order.entity.OrderLineEntity;
import org.when.data.product.entity.ProductEntity;
import org.when.data.product.entity.ProductType;
import org.when.stats.vo.IncomeStatsVO;

import java.time.LocalDate;
import java.util.List;

public class IncomeConverter {
    public static IncomeStatsVO convert(LocalDate date, OrderEntity order, List<OrderLineEntity> orderLines, List<ProductEntity> products) {
        IncomeStatsVO result = new IncomeStatsVO();
        result.setDate(date);
        result.setOrderNo(order.getOrderNo());
        result.setDigest(order.getRemark());
        result.setOrderAmount(order.getTotalPrice());

        // 判断是水桶、饮水机、赠品饮水机、赠品水票、正常水票、可开票水票，设置对应的数值
        for (OrderLineEntity orderLine : orderLines) {
            ProductEntity product = products.stream()
                    .filter(e -> e.getId().equals(orderLine.getProductId()))
                    .findAny()
                    .get();

            Integer typeCode = product.getProductType();
            ProductType productType = ProductType.typeOf(typeCode);
            if (productType == ProductType.TICKET) {
                if (product.getIsGift()) {
                    result.setGiftTicket(orderLine.getQuantity());
                } else {
                    if (product.getInvoiced()) {
                        result.setInvoicedTicket(orderLine.getQuantity());
                    } else {
                        result.setNormalTicket(orderLine.getQuantity());
                    }
                }

            }
            if (productType == ProductType.BUCKET) {
                result.setBucketDeposit(1);
            }
            if (productType == ProductType.DISPENSER) {
                if (product.getIsGift()) {
                    result.setGiftDispenser(1);
                } else {
                    result.setDispenser(1);
                }
            }
        }
        return result;
    }
}
