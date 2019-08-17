package id.docflux.app;

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class AppUtils {

    private static Pattern pattern = Pattern.compile(" ");
    private static Predicate<String> isEmpty = String::isEmpty;

    @SuppressWarnings("resource")
    public static void usage(PrintStream out) throws IOException {
        Scanner scanner = new Scanner(DistanceCalculatorApp.class.getResource("/README.md").openStream())
                .useDelimiter("\n");
        while (scanner.hasNext())
            out.println(scanner.next());
    }

    public static Set<String> asSet(String string) {
        return pattern.splitAsStream(string)
                .filter(isEmpty.negate())
                .collect(toSet());
    }

    public static Stream<String> readAllLines(Path path) {
        try {
            return Files.readAllLines(path).stream()
                    .map(String::trim)
                    .filter(isEmpty.negate());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stream<String> readAllLines(String path) {
        return readAllLines(Paths.get(path));
    }

}
