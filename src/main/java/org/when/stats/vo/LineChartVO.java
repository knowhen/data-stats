package org.when.stats.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LineChartVO {
    private Set<String> keys;
    private List<Map<String, Object>> data;
}
