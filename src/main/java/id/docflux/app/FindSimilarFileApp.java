package id.docflux.app;

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Function;

import id.minhash.DistanceCalculator;

public class FindSimilarFileApp {

    private static PrintStream out = System.out;

    private static Set<String> asSet(Path path) {
        try {
            return Files.lines(path)
                    .map(AppUtils::asSet)
                    .flatMap(Set::stream)
                    .collect(toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            AppUtils.usage(out );
            System.exit(1);
        }
        Set<String> s1 = asSet(Paths.get(args[0]));
        Function<Path, Pair> calc = path -> {
            Set<String> s2 = asSet(path);
            Pair p = new Pair();
            p.dist = DistanceCalculator.jaccard(s1, s2);
            p.item = path.toString();
            return p;
        };
        Comparator<Pair> cmp = Comparator.comparing(p -> p.dist);
        Files.walk(Paths.get(args[1]))
            .map(calc)
            .sorted(cmp.reversed())
            .limit(5)
            .forEach(p -> out.format("[%s] %s\n", p.dist, p.item));
    }

}
