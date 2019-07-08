package id.docflux.app;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class AppUtils {

    @SuppressWarnings("resource")
    public static void usage(PrintStream out) throws IOException {
        Scanner scanner = new Scanner(DistanceCalculatorApp.class.getResource("/README.md").openStream())
                .useDelimiter("\n");
        while (scanner.hasNext())
            out.println(scanner.next());
    }
}
