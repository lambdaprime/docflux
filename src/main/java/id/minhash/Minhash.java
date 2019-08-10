package id.minhash;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

public class Minhash {
    private static final int DEFAULT_LEN = 128;
    private static int NUMPERM = DEFAULT_LEN;
    private static final int BANDSIZE = 4;

    private static Random rand = new Random();
    private static long[] permA = permutations();
    private static long[] permB = permutations();
	private static Long seed;

    private static final int PRIME = 2147483647;
    private static final int MAXHASH = Integer.MAX_VALUE;

    private long[] hashvalues;
    private String[] minvalues;

    private Supplier<String[]> HASH_BANDS = () -> {
        String[] hashBands = new String[hashvalues.length / BANDSIZE];
        for (int i = 0; i < hashBands.length; i++) {
            int start = i * BANDSIZE;
            int end = start + BANDSIZE;
            long[] band = Arrays.copyOfRange(hashvalues, start, end);
            hashBands[i] = stream(band).mapToObj(String::valueOf).collect(joining("."));
        };
        return hashBands;
    };

    public Minhash() {
        hashvalues = new long[NUMPERM];
        Arrays.fill(hashvalues, MAXHASH);
        minvalues = new String[hashvalues.length];
    }

    public Minhash(String str) {
        this();
        stream(str.split(" ")).forEach(this::add);
    }

    public Minhash(long[] hashvalues) {
        this.hashvalues = hashvalues;
    }

    public void add(String str) {
        if (str.isEmpty()) return;
        for (int i = 0; i < hashvalues.length; i++) {
            long a = permA[i];
            long b = permB[i];
            long hash = (a * fingerprint(str) + b) % PRIME;
            //System.out.println(hash);
            if (hash < 0) throw new RuntimeException("Negative hash value");
            if (hash < hashvalues[i]) {
                hashvalues[i] = hash;
                minvalues[i] = str;
            }
        }
    }

    public double distance(Minhash other) {
        Set<Long> s1 = Arrays.stream(hashvalues).boxed().collect(toSet());
        Set<Long> s2 = Arrays.stream(other.hashvalues).boxed().collect(toSet());
        return DistanceCalculator.jaccard(s1, s2);
    }

    public String[] getHashBands() {
        return HASH_BANDS.get();
    }

    public String[] getMinValues() {
		return minvalues;
	}

    @Override
    public String toString() {
        return stream(hashvalues)
                .boxed()
                .map(i -> "" + i)
                .collect(joining(" "));
    }

    public String dump() {
    	var v = stream(minvalues).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    	var buf = new StringBuilder();
    	Consumer<String> appendLine = str -> buf.append(str).append("\n");
    	appendLine.accept("seed: " + seed);
    	appendLine.accept(v.toString());
    	appendLine.accept(toString());
        return buf.toString();
    }

    private static long[] permutations() {
        Set<Long> used = new HashSet<>();
        for (int j = 0; j < NUMPERM; j++) {
            long randInt = rand.nextInt(Integer.MAX_VALUE);
            while (used.contains(randInt))
                randInt = rand.nextInt(Integer.MAX_VALUE);
            if (randInt < 0) throw new RuntimeException("Negative permutation value");
            used.add(randInt);
        }
        return used.stream().mapToLong(Long::longValue)
                .toArray();
    }

    private long fingerprint(String str) {
    	long h = 0;
        for (int i = 0; i < str.length(); i++) {
            h = 31 * h + str.charAt(i);
        }
        return h & Integer.MAX_VALUE;
    }

    static void reset() {
        permA = permutations();
        permB = permutations();
    }

    public static void init(Optional<Long> seed, Optional<Integer> len) {
        NUMPERM = len.orElse(DEFAULT_LEN);
        Minhash.seed = seed.orElse(null); 
        rand = seed.isEmpty()? new Random(): new Random(seed.get());
        permA = permutations();
        permB = permutations();
    }

    public static void main(String[] args) {
        IntStream.range(0, 1000)
            .mapToDouble(i -> {
                reset();
                Minhash m1 = new Minhash("there is no spoon");
                Minhash m2 = new Minhash("there is no minhash");
                return m1.distance(m2);
            })
            .sorted()
            .forEach(System.out::println);
    }

}