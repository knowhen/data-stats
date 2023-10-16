package org.when.stats.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderStatsChartVO {

    private BigDecimal totalAmount;

    private LineChartVO lineChart;
}
