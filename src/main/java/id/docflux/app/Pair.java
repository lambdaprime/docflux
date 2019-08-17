package id.docflux.app;

import java.util.Comparator;

public class Pair {
    double dist;
    String item;

    public static Comparator<Pair> comparable() {
        return Comparator.comparing((Pair p) -> p.dist);
    }

    public static Comparator<Pair> comparableReversed() {
        return comparable().reversed();
    }

}
