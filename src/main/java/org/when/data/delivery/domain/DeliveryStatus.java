package org.when.data.delivery.domain;


import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public enum DeliveryStatus {
    TBD(0, "待确认"), COMPLETED(1, "已完成"), CANCELLED(-1, "已取消");

    private final Integer code;
    private final String status;

    DeliveryStatus(Integer code, String status) {
        this.code = code;
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public static Integer codeOf(String status) {
        Optional<DeliveryStatus> any = findAny(e -> e.getStatus().equals(status));
        return any.map(DeliveryStatus::getCode)
                .orElseThrow(() -> new RuntimeException(status));

    }

    public static DeliveryStatus statusOf(Integer code) {
        Optional<DeliveryStatus> any = findAny(e -> e.getCode().equals(code));
        return any.orElseThrow(() -> new RuntimeException(code.toString()));
    }

    private static Optional<DeliveryStatus> findAny(Predicate<DeliveryStatus> filter) {
        return Arrays.stream(DeliveryStatus.values())
                .filter(filter)
                .findAny();
    }
}
