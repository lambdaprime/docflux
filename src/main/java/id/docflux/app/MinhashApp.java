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

    private Optional<String> fileName = Optional.empty(); 
    private Optional<String> string = Optional.empty();
    private Optional<Integer> length = Optional.empty();
    private Optional<Long> seed = Optional.empty();
    private boolean perLine;
    private boolean showDump;

    static PrintStream out = System.out;

    private final Map<String, Consumer<String>> handlers = Map.of(
        "-s", arg -> seed = Optional.of(Long.valueOf(arg)),
        "-l", arg -> length = Optional.of(Integer.valueOf(arg)),
        "-f", arg -> fileName = Optional.of(arg)
    );
    private final Function<String, Boolean> defaultHandler = arg -> {
        switch(arg) {
        case "-ll": {
            perLine = true;
            break;
        }
        case "-dump": {
            showDump = true;
            break;
        }
        default: {
            string = Optional.of(arg);
            return false;
        }}
        return true;
    };

    private void calculateForLines(String fileName) throws IOException {
        Files.readAllLines(Paths.get(fileName)).stream()
            .map(Minhash::new)
            .forEach(out::println);
    }

    private void calculateForDoc(String fileName) throws IOException {
        String doc = Files.readAllLines(Paths.get(fileName)).stream()
                .collect(joining(" "));
        out.println(new Minhash(doc));
    }

    private void processFile(String filename) {
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

    private void processString(String str) {
        var mh = new Minhash(str);
        if (showDump)
            System.out.println(mh.dump());
        out.println(mh);
    }

    private void runMain(String[] args) throws Exception {
        new SmartArgs(handlers, defaultHandler).parse(args);
        Minhash.init(seed, length);
        fileName.ifPresent(this::processFile);
        string.ifPresent(this::processString);
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            AppUtils.usage(out);
            System.exit(1);
        }
        new MinhashApp().runMain(args);
    }

}
