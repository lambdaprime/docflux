package id.docflux.app;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Function;

import id.minhash.DistanceCalculator;

public class FindSimilarLineApp {

    private static PrintStream out = System.out;

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            AppUtils.usage(out );
            System.exit(1);
        }
        Set<String> s1 = AppUtils.asSet(args[0]);
        class Pair {
            double dist;
            String line;
        };
        Function<String, Pair> calc = line -> {
            Set<String> s2 = AppUtils.asSet(line);
            Pair p = new Pair();
            p.dist = DistanceCalculator.jaccard(s1, s2);
            p.line = line;
            return p;
        };
        Comparator<Pair> cmp = Comparator.comparing(p -> p.dist);
        Files.readAllLines(Paths.get(args[1])).stream()
            .map(calc)
            .sorted(cmp.reversed())
            .limit(5)
            .forEach(p -> out.format("[%s] %s\n", p.dist, p.line));
    }

}