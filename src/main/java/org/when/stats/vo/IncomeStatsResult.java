package org.when.stats.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class IncomeStatsResult {
    private String report;
    private List<IncomeStatsVO> stats;
}
