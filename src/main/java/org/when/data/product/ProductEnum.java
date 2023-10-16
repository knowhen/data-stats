package org.when.data.product;

import java.util.Arrays;
import java.util.Optional;

public enum ProductEnum {
    TICKET(1, "水票"),
    TICKET2(2, "水票"),
    BUCKET(3, "水桶"),
    DISPENSER(4, "饮水机"),
    TICKET3(5, "水票（赠品）"),
    DISPENSER2(6, "饮水机（赠品）"),
    TICKET4(8, "水票（分配）");

    private Integer code;
    private String desc;

    ProductEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ProductEnum fromCode(Integer code) {
        Optional<ProductEnum> optional = Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findAny();
        return optional.orElse(null);
    }
}
