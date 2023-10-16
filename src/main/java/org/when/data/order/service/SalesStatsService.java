package org.when.data.order.service;

import org.when.stats.vo.SalesVO;

import java.time.LocalDateTime;
import java.util.List;

public interface SalesStatsService {
    List<SalesVO> statsSalesBetween(LocalDateTime startTime, LocalDateTime endTime);
}
