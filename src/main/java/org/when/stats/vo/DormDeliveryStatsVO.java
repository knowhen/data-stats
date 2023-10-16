package org.when.stats.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DormDeliveryStatsVO extends NameAndDateVO {

    public DormDeliveryStatsVO() {
        this.deliveredBucket = 0;
        this.recycledBucket = 0;
        this.recycledETicket = 0;
        this.recycledPaperTicket = 0;
    }

    private Integer deliveredBucket;

    private Integer recycledBucket;

    private Integer recycledETicket;

    private Integer recycledPaperTicket;

    public DormDeliveryStatsVO accept(DormDeliveryStatsVO source) {
        this.deliveredBucket += source.getDeliveredBucket();
        this.recycledBucket += source.getRecycledBucket();
        this.recycledETicket += source.getRecycledETicket();
        this.recycledPaperTicket += source.getRecycledPaperTicket();
        return this;
    }

    public DormDeliveryStatsVO combine(DormDeliveryStatsVO other) {
        this.deliveredBucket += other.getDeliveredBucket();
        this.recycledBucket += other.getRecycledBucket();
        this.recycledETicket += other.getRecycledETicket();
        this.recycledPaperTicket += other.getRecycledPaperTicket();
        return this;
    }
}
