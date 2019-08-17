package id.docflux.app;

import static id.minhash.DistanceCalculator.jaccard;
import static id.minhash.DistanceCalculator.overlap;
import static java.util.stream.Collectors.toSet;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import id.util.SmartArgs;

public class FindSimilarFileApp {

    private static PrintStream out = System.out;

    private static List<String> arguments = new ArrayList<>();
    private static boolean isOverlap;
    private static boolean isJaccard;
    
    private static final Function<String, Boolean> defaultHandler = arg -> {
        switch(arg) {
        case "-overlap": {
            isOverlap = true;
            break;
        }
        case "-jaccard": {
            isJaccard = true;
            break;
        }
        default: {
            arguments.add(arg);
        }}
        return true;
    };

    private static Set<String> asSet(Path path) {
        return AppUtils.readAllLines(path)
                .map(AppUtils::asSet)
                .flatMap(Set::stream)
                .collect(toSet());
    }
    
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            AppUtils.usage(out );
            System.exit(1);
        }
        new SmartArgs(Collections.emptyMap(), defaultHandler).parse(args);
        Set<String> s1 = asSet(Paths.get(arguments.get(0)));
        Function<Path, Pair> calc = path -> {
            Set<String> s2 = asSet(path);
            Pair p = new Pair();
            p.dist = isJaccard? jaccard(s1, s2): overlap(s1, s2);
            p.item = path.toString();
            return p;
        };
        Comparator<Pair> cmp = Comparator.comparing(p -> p.dist);
        Files.walk(Paths.get(arguments.get(1)))
            .filter(p -> !p.toFile().isDirectory())
            .map(calc)
            .sorted(cmp.reversed())
            .limit(5)
            .forEach(p -> out.format("[%s] %s\n", p.dist, p.item));
    }

}
