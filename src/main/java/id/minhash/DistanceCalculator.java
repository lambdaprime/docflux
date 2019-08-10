package id.minhash;

import static java.util.stream.Collectors.toSet;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DistanceCalculator {

    public static double jaccard(Set<?> s1, Set<?> s2) {
        double intersect = Stream.of(s1, s2)
            .flatMap(Set::stream)
            .collect(Collectors.groupingBy(Function.identity()))
            .entrySet().stream()
            .filter(e -> e.getValue().size() > 1)
            .count();
        double union = Stream.of(s1, s2)
                .flatMap(Set::stream)
                .collect(toSet()).size();
        return intersect / union;
    }
}
