package id.docflux.app;

import static id.minhash.DistanceCalculator.jaccard;
import static id.minhash.DistanceCalculator.overlap;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import id.util.SmartArgs;

public class FindSimilarLineApp {

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

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            AppUtils.usage(out );
            System.exit(1);
        }
        new SmartArgs(Collections.emptyMap(), defaultHandler).parse(args);
        Set<String> s1 = AppUtils.asSet(arguments.get(0));
        Function<String, Pair> calc = line -> {
            Set<String> s2 = AppUtils.asSet(line);
            Pair p = new Pair();
            p.dist = isJaccard? jaccard(s1, s2): overlap(s1, s2);
            p.item = line;
            return p;
        };
        AppUtils.readAllLines(arguments.get(1))
            .map(calc)
            .sorted(Pair.comparableReversed())
            .limit(5)
            .forEach(p -> out.format("[%s] %s\n", p.dist, p.item));
    }

}
