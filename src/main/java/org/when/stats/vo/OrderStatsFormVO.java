package org.when.stats.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderStatsFormVO {
    private BigDecimal totalAmount;
    private List<SalesVO> sales;
}
