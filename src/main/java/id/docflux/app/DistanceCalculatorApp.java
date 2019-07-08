package id.docflux.app;

import java.io.PrintStream;
import java.util.Arrays;

import id.minhash.Minhash;

public class DistanceCalculatorApp {

    static PrintStream out = System.out;

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            AppUtils.usage(out);
            System.exit(1);
        }
        long[] mhash1 = Arrays.stream(args[0].split(" ")).mapToLong(Long::parseLong).toArray();
        long[] mhash2 = Arrays.stream(args[1].split(" ")).mapToLong(Long::parseLong).toArray();
        out.println(new Minhash(mhash1).distance(new Minhash(mhash2)));
    }

}
