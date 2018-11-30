package me.quiz_together.root.util;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Random;

import net.jqwik.api.Arbitrary;
import net.jqwik.api.Shrinkable;

public class ArbitraryUtils {
    private static final Random RANDOM = new Random();

    public static <T> T one(Arbitrary<T> arbitrary) {
        return arbitrary.generator(1)
                        .next(RANDOM)
                        .value();
    }

    public static <T> List<T> list(Arbitrary<T> arbitrary, int size) {
        return arbitrary.generator(1)
                        .stream(RANDOM)
                        .limit(size)
                        .map(Shrinkable::value)
                        .collect(toList());
    }
}
