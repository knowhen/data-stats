package org.when.stats.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatsVO {

    private String name;

    private LocalDate date;

    private Integer quantity;
}
