package id.docflux.app;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

import id.minhash.DistanceCalculator;
import id.util.SmartArgs;

public class DistanceCalculatorApp {

    private static Optional<String> mhoptions;

    static PrintStream out = System.out;

    private static List<String> sets = new ArrayList<>();
    
    private static final Map<String, Consumer<String>> handlers = Map.of(
        "--minhash", arg -> mhoptions = Optional.of(arg)
    );

    private static final Function<String, Boolean> defaultHandler = arg -> {
        switch(arg) {
        default: {
            sets.add(arg);
        }}
        return true;
    };

    private static void reset() {
        sets.clear();
        mhoptions = Optional.empty();
    }

    public static void main(String[] args) throws Exception {
        reset();
        if (args.length < 2) {
            AppUtils.usage(out);
            System.exit(1);
        }
        new SmartArgs(handlers, defaultHandler).parse(args);
        if (sets.size() < 2) {
            AppUtils.usage(out);
            System.exit(1);
        }
        if (!mhoptions.isEmpty()) {
            var mhargs = new ArrayList<>();
            if (!mhoptions.get().contains("-s")) {
                mhargs.addAll(Arrays.asList("-s", "" + System.currentTimeMillis()));
            }
            if (!mhoptions.get().isEmpty()) {
                mhargs.addAll(Pattern.compile(" ").splitAsStream(mhoptions.get())
                    .collect(toList()));
            }
            
            var e1 = new BackgroundMinhashAppExecutor();
            mhargs.add(sets.get(0));
            var mh1 = e1.run(mhargs.toArray(new String[0]));

            mhargs.remove(mhargs.size() - 1);
            
            var e2 = new BackgroundMinhashAppExecutor();
            mhargs.add(sets.get(1));
            var mh2 = e2.run(mhargs.toArray(new String[0]));

            sets.clear();
            sets.add(mh1.stream().collect(joining(" ")));
            sets.add(mh2.stream().collect(joining(" ")));
        }
        Set<String> s1 = Arrays.stream(sets.get(0).split(" ")).collect(toSet());
        Set<String> s2 = Arrays.stream(sets.get(1).split(" ")).collect(toSet());
        out.println(DistanceCalculator.jaccard(s1, s2));
    }

}
