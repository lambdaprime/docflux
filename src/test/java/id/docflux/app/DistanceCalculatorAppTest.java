package id.docflux.app;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DistanceCalculatorAppTest {

    private ByteArrayOutputStream baos;

    @BeforeEach
    public void setup() {
        baos = new ByteArrayOutputStream();
        DistanceCalculatorApp.out = new PrintStream(baos);
    }

    @Test
    public void test_distance() throws Exception {
        DistanceCalculatorApp.main(new String[] {
            "1 2 3 4", "3 0 4 1"
        });
        List<String> out = output();
        out.forEach(System.out::println);
        assertEquals("0.6", out.get(0));
    }

    @Test
    public void test_equal() throws Exception {
        DistanceCalculatorApp.main(new String[] {
            "1 2 3 4", "3 2 4 1"
        });
        List<String> out = output();
        out.forEach(System.out::println);
        assertEquals("1.0", out.get(0));
    }

    @Test
    public void test_repeative() throws Exception {
        DistanceCalculatorApp.main(new String[] {
            "1 2 1 1", "2 1 1 1"
        });
        List<String> out = output();
        out.forEach(System.out::println);
        assertEquals("1.0", out.get(0));
    }

    @Test
    public void test_default_seed() throws Exception {
        DistanceCalculatorApp.main(new String[] {
            "--minhash", "", "1 2 1 1", "2 1 1 1"
        });
        List<String> out = output();
        out.forEach(System.out::println);
        assertEquals("1.0", out.get(0));
    }

    @Test
    public void test_different_minhash_default() throws Exception {
        DistanceCalculatorApp.main(new String[] {
            "--minhash", "", "one two", "one three"
        });
        List<String> out = output();
        out.forEach(System.out::println);
        assertTrue(0.1 < Double.parseDouble(out.get(0)));
    }

    @Test
    public void test_different_minhash() throws Exception {
        DistanceCalculatorApp.main(new String[] {
            "--minhash", "-l 5 -s 123", "one two", "one three"
        });
        List<String> out = output();
        out.forEach(System.out::println);
        assertEquals("0.1111111111111111", out.get(0));
    }

    private List<String> output() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray())));
        return reader.lines().collect(toList());
    }
}
