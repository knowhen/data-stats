package org.when.data.product;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

public enum BundleEnum {
    BUNDLE_(4, "50桶水票", "400"),
    BUNDLE_2(5, "100桶水票", "800"),
    BUNDLE_3(7, "200桶水票", "1600"),
    BUNDLE_4(8, "50桶水票", "450"),
    BUNDLE_5(9, "100桶水票", "900"),
    BUNDLE_6(11, "200桶水票", "1800"),
    BUNDLE_7(12, "30桶水票", "240");
    private Integer code;
    private String desc;
    private BigDecimal price;

    BundleEnum(Integer code, String desc, String price) {
        this.code = code;
        this.desc = desc;
        this.price = new BigDecimal(price);
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public static BundleEnum fromCode(Integer code) {
        Optional<BundleEnum> optional = Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findAny();
        return optional.orElse(null);
    }
}
