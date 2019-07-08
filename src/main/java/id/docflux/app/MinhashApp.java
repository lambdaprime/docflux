package id.docflux.app;

import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import id.minhash.Minhash;
import id.util.SmartArgs;

public class MinhashApp {

    private static Optional<String> fileName;
    private static Optional<String> string;
    private static boolean perLine;
    private static Optional<Integer> length;
    private static Optional<Long> seed;

    static PrintStream out = System.out;

    private static final Map<String, Consumer<String>> handlers = Map.of(
        "-s", arg -> seed = Optional.of(Long.valueOf(arg)),
        "-l", arg -> length = Optional.of(Integer.valueOf(arg)),
        "-f", arg -> fileName = Optional.of(arg)
    );
    private static final Function<String, Boolean> defaultHandler = arg -> {
        switch(arg) {
        case "-ll": {
            perLine = true;
            break;
        }
        default: {
            string = Optional.of(arg);
            return false;
        }}
        return true;
    };

    private static void calculateForLines(String fileName) throws IOException {
        Files.readAllLines(Paths.get(fileName)).stream()
            .map(Minhash::new)
            .forEach(out::println);
    }

    private static void calculateForDoc(String fileName) throws IOException {
        String doc = Files.readAllLines(Paths.get(fileName)).stream()
                .collect(joining(" "));
        out.println(new Minhash(doc));
    }

    private static void processFile(String filename) {
        try {
            if (perLine) {
                calculateForLines(filename);
            } else {
                calculateForDoc(filename);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void processString(String str) {
        out.println(new Minhash(str));
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            AppUtils.usage(out);
            System.exit(1);
        }
        seed = Optional.empty();
        length = Optional.empty();
        fileName = Optional.empty();
        string = Optional.empty();
        new SmartArgs(handlers, defaultHandler).parse(args);
        Minhash.init(seed, length);
        fileName.ifPresent(MinhashApp::processFile);
        string.ifPresent(MinhashApp::processString);
    }

}
