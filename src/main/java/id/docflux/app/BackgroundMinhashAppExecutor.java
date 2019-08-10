package id.docflux.app;

import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

public class BackgroundMinhashAppExecutor {

    private ByteArrayOutputStream baos;
    
    public BackgroundMinhashAppExecutor() {
        baos = new ByteArrayOutputStream();
        MinhashApp.out = new PrintStream(baos);
    }
    
    public List<String> run(String[] args) throws Exception {
        MinhashApp.main(args);
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(new ByteArrayInputStream(baos.toByteArray())));
        return reader.lines().collect(toList());
    }

}
