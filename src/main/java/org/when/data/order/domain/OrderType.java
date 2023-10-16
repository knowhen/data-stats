package org.when.data.order.domain;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public enum OrderType {
    T(1, "水票订单"), B(2, "套餐订单"), O(3, "其他"), D(4, "分票订单");
    private final Integer code;
    private final String type;

    OrderType(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public static Integer codeOf(String type) {
        Optional<OrderType> any = findAny(e -> e.getType().equals(type));
        return any.map(OrderType::getCode)
                .orElseThrow(() -> new RuntimeException(type));

    }

    public static OrderType typeOf(Integer code) {
        Optional<OrderType> any = findAny(e -> e.getCode().equals(code));
        return any.orElseThrow(() -> new RuntimeException(code.toString()));
    }

    private static Optional<OrderType> findAny(Predicate<OrderType> filter) {
        return Arrays.stream(OrderType.values())
                .filter(filter)
                .findAny();
    }
}
