package org.when.data.delivery.domain;

import java.util.Arrays;

public enum RecordStatus {
    REJECTED(-1, "已拒绝"),
    PENDING_DELIVERY(0, "待配送"),
    DELIVERED(1, "已送达"),
    CONFIRMED(2, "已确认");

    private final Integer code;
    private final String status;

    RecordStatus(Integer code, String status) {
        this.code = code;
        this.status = status;
    }

    public static RecordStatus statusOf(Integer statusCode) {
        return Arrays.stream(values())
                .filter(status -> status.getCode().equals(statusCode))
                .findAny()
                .orElseThrow(() -> new RuntimeException(statusCode.toString()));
    }

    public Integer getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }
}
