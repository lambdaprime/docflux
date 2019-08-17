package id.minhash;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class DistanceCalculatorTest {

    @Test
    public void test_overlap() throws Exception {
        Set<String> s1 = Stream.of("a", "b", "c").collect(toSet());
        Set<String> s2 = Stream.of("a", "c").collect(toSet());
        assertEquals(1, DistanceCalculator.overlap(s1, s2));
    }

}
