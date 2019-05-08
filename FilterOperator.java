package it.at;

import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import lombok.SneakyThrows;

@SuppressWarnings("rawtypes")
public enum FilterOperator {

    AND(And.class),
    OR(Or.class, false, false),
    IS_TRUE(IsTrue.class, false, false),
    IS_FALSE(IsFalse.class, false, true),
    EQUALS(Equals.class),
    NOT_EQUALS(NotEquals.class),
    IS_NULL(IsNull.class, false, true),
    IS_NOT_NULL(IsNotNull.class, false, true),
    LIKE(Like.class),
    GTE(Gte.class),
    GT(Gt.class),
    LTE(Lte.class),
    LT(Lt.class);

    private final BiFunction<Set<Object>, Object, Boolean> operation;
    private final boolean checkRight;
    private final boolean skipFilter;

    private FilterOperator(Class<? extends BiFunction> operation) {
        this(operation, true, false);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private FilterOperator(
        Class<? extends BiFunction> operation, boolean checkRight, boolean skipFilter
    ) {
        this.checkRight = checkRight;
        this.skipFilter = skipFilter;
        this.operation = operation.newInstance();
    }

    public <E, R> boolean apply(Set<E> l, E r, Function<E, R> mapping) {
        if (Objects.isNull(l)) {
            return false;
        }

        if (checkRight && Objects.isNull(r)) {
            return false;
        }

        return operation.apply(
            l.stream()
                .filter(skipFilter ? e -> true : Objects::nonNull)
                .map(mapping)
                .collect(Collectors.toSet()),
            mapping.apply(r)
        );
    }

    public <E> boolean apply(Set<E> l, E r) {
        return apply(l, r, Function.identity());
    }

    static class Equals<E> implements BiFunction<Set<E>, E, Boolean> {
        @Override
        public Boolean apply(Set<E> l, E r) {
            return l.stream().anyMatch(e -> e.equals(r));
        }
    }

    static class NotEquals<E> implements BiFunction<Set<E>, E, Boolean> {
        @Override
        public Boolean apply(Set<E> l, E r) {
            return !new Equals<E>().apply(l, r);
        }
    }

    static class IsNull<E> implements BiFunction<Set<E>, E, Boolean> {
        @Override
        public Boolean apply(Set<E> l, E r) {
            return l.stream().allMatch(Objects::isNull);
        }
    }

    static class IsNotNull<E> implements BiFunction<Set<E>, E, Boolean> {
        @Override
        public Boolean apply(Set<E> l, E r) {
            return l.stream().allMatch(Objects::nonNull);
        }
    }

    static class Like implements BiFunction<Set<String>, String, Boolean> {
        @Override
        public Boolean apply(Set<String> l, String r) {
            return l.stream().anyMatch(e -> e.contains(r));
        }
    }

    static class Gt<E extends Comparable<E>> implements BiFunction<Set<E>, E, Boolean> {
        @Override
        public Boolean apply(Set<E> l, E r) {
            return l.stream().anyMatch(e -> e.compareTo(r) > 0);
        }
    }

    static class Gte<E extends Comparable<E>> implements BiFunction<Set<E>, E, Boolean> {
        @Override
        public Boolean apply(Set<E> l, E r) {
            return l.stream().anyMatch(e -> e.compareTo(r) >= 0);
        }
    }

    static class Lt<E extends Comparable<E>> implements BiFunction<Set<E>, E, Boolean> {
        @Override
        public Boolean apply(Set<E> l, E r) {
            return l.stream().anyMatch(e -> e.compareTo(r) < 0);
        }
    }

    static class Lte<E extends Comparable<E>> implements BiFunction<Set<E>, E, Boolean> {
        @Override
        public Boolean apply(Set<E> l, E r) {
            return l.stream().anyMatch(e -> e.compareTo(r) <= 0);
        }
    }

    static class IsTrue implements BiFunction<Set<Boolean>, Boolean, Boolean> {
        @Override
        public Boolean apply(Set<Boolean> l, Boolean r) {
            return l.stream().anyMatch(BooleanUtils::isTrue);
        }
    }

    static class IsFalse implements BiFunction<Set<Boolean>, Boolean, Boolean> {
        @Override
        public Boolean apply(Set<Boolean> l, Boolean r) {
            // return !new IsTrue().apply(l, r);
            return l.stream().allMatch(BooleanUtils::isFalse);
        }
    }

    static class And implements BiFunction<Set<Boolean>, Boolean, Boolean> {
        @Override
        public Boolean apply(Set<Boolean> l, Boolean r) {
            return l.stream().allMatch(BooleanUtils::isTrue) && BooleanUtils.isTrue(r);
        }
    }

    static class Or implements BiFunction<Set<Boolean>, Boolean, Boolean> {
        @Override
        public Boolean apply(Set<Boolean> l, Boolean r) {
            return l.stream().anyMatch(BooleanUtils::isTrue) || BooleanUtils.isTrue(r);
        }
    }

}
