package org.when.data.product.entity;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public enum ProductType {
    TICKET(1, "水票"),
    BUCKET(2, "水桶"),
    DISPENSER(3, "饮水机"),
    OTHER(4, "其他"),
    DISTRIBUTION(5, "分配专属");
    private final Integer code;
    private final String type;

    ProductType(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public static ProductType typeOf(Integer code) {
        Optional<ProductType> any = findAny(e -> e.getCode().equals(code));
        return any.orElse(OTHER);
    }

    private static Optional<ProductType> findAny(Predicate<ProductType> filter) {
        return Arrays.stream(ProductType.values())
                .filter(filter)
                .findAny();
    }
}
