package org.when.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class Lambdas {
    public static <T, R> List<R> map(List<T> list, Function<T, R> function) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(function)
                .collect(toList());
    }

    public static <T> List<T> filter(Collection<T> list, Predicate<T> filter) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream()
                .filter(filter)
                .collect(toList());
    }
}
