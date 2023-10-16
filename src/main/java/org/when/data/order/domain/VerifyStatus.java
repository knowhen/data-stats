package org.when.data.order.domain;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public enum VerifyStatus {
    CANCELLED(-1, "已撤回"), COMPLETED(1, "已核销");
    private final Integer code;
    private final String status;

    VerifyStatus(Integer code, String status) {
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
        Optional<VerifyStatus> any = findAny(e -> e.getStatus().equals(status));
        return any.map(VerifyStatus::getCode)
                .orElseThrow(() -> new RuntimeException(status));

    }

    public static VerifyStatus statusOf(Integer code) {
        Optional<VerifyStatus> any = findAny(e -> e.getCode().equals(code));
        return any.orElseThrow(() -> new RuntimeException(code.toString()));
    }

    private static Optional<VerifyStatus> findAny(Predicate<VerifyStatus> filter) {
        return Arrays.stream(VerifyStatus.values())
                .filter(filter)
                .findAny();
    }
}
