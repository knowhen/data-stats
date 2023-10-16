package org.when.data.order.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
public class SalesStats {
    private String name;
    private BigDecimal price;
    private Integer sales;
    private BigDecimal amount;

    public SalesStats() {
        this("", BigDecimal.ZERO, 0, BigDecimal.ZERO);
    }

    public SalesStats(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
        this.sales = 0;
        this.amount = BigDecimal.ZERO;
    }

    public SalesStats(String name, BigDecimal price, Integer sales) {
        this.name = name;
        this.price = price;
        this.sales = sales;
        this.amount = price.multiply(new BigDecimal(sales));
    }

    public SalesStats(String name, BigDecimal price, Integer sales, BigDecimal amount) {
        this.name = name;
        this.price = price;
        this.sales = sales;
        this.amount = amount;
    }

    public SalesStats combine(SalesStats other) {
        if (this.name.isEmpty()) {
            return other;
        } else {
            Integer quantity = this.sales + other.getSales();
            BigDecimal total = this.amount.add(other.getAmount());
            return new SalesStats(this.name, this.price, quantity, total);
        }
    }

    @Override
    public String toString() {
        return name + ": " + price + " * " + sales + " = " + amount;
    }
}
