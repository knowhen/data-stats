package org.when.stats.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class DeliveryStatsResult {
    private Integer totalRecords;
    private List<DeliveryStatsVO> stats;
}
