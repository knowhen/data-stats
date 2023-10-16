package org.when.data.staff.domain;


import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public enum StaffType {
    DORM_KEEPER(1, "宿管"), WATERMAN(2, "水工"), SUPER_WATERMAN(3, "教职工水工");
    private Integer code;
    private String type;

    StaffType(Integer code, String type) {
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
        Optional<StaffType> any = findAny(e -> e.getType().equals(type));
        return any.map(StaffType::getCode)
                .orElseThrow(() -> new RuntimeException(type));

    }

    public static StaffType typeOf(Integer code) {
        Optional<StaffType> any = findAny(e -> e.getCode().equals(code));
        return any.orElse(null);
    }

    private static Optional<StaffType> findAny(Predicate<StaffType> filter) {
        return Arrays.stream(StaffType.values())
                .filter(filter)
                .findAny();
    }

}
