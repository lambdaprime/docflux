package id.docflux.app;

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class AppUtils {

    private static Pattern pattern = Pattern.compile(" ");

    @SuppressWarnings("resource")
    public static void usage(PrintStream out) throws IOException {
        Scanner scanner = new Scanner(DistanceCalculatorApp.class.getResource("/README.md").openStream())
                .useDelimiter("\n");
        while (scanner.hasNext())
            out.println(scanner.next());
    }

    public static Set<String> asSet(String string) {
        Predicate<String> isEmpty = String::isEmpty;
        return pattern.splitAsStream(string)
                .filter(isEmpty.negate())
                .collect(toSet());
    }
}
