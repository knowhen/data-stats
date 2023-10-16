package org.when.stats.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class IncomeStatsVO {
    private LocalDate date;

    private String orderNo;

    private String digest;

    private BigDecimal orderAmount;

    private BigDecimal serviceCharge;

    private BigDecimal settlementAmount;

    private Integer bucketDeposit;

    private Integer dispenser;

    private Integer giftDispenser;

    private Integer giftTicket;

    private Integer normalTicket;

    private Integer invoicedTicket;

    private BigDecimal bottledWater;

    private BigDecimal balance;

}
