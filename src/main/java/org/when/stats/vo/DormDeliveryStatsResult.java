package org.when.stats.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class DormDeliveryStatsResult {
    private DormDeliveryStatsVO totalRecords;
    private List<DormDeliveryStatsVO> stats;
}
