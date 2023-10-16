package org.when.stats.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SalesVO {
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal amount;
}
