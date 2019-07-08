package id.docflux.app;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.docflux.app.MinhashApp;

public class MinhashAppTest {

    private ByteArrayOutputStream baos;

    @BeforeEach
    public void setup() {
        baos = new ByteArrayOutputStream();
        MinhashApp.out = new PrintStream(baos);
    }

    @Test
    public void test_all_defaults() throws Exception {
        String absolutePath = path("doc1");
        MinhashApp.main(new String[] {
            absolutePath
        });
        List<String> out = output();
        out.forEach(System.out::println);
        assertEquals(1, out.size());
        assertEquals(128, out.get(0).split(" ").length);
    }

    @Test
    public void test_seed() throws Exception {
        String absolutePath = path("doc1");

        List<String> out1 = testSeed(absolutePath, 123);
        out1.forEach(System.out::println);
        baos.reset();
        List<String> out2 = testSeed(absolutePath, 123);
        out2.forEach(System.out::println);
        
        assertEquals(out1, out2);

    }

    @Test
    public void test_length() throws Exception {
        String absolutePath = path("doc1");
        MinhashApp.main(new String[] {
            "-l", "20", absolutePath
        });
        List<String> out = output();
        out.forEach(System.out::println);
        assertEquals(1, out.size());
        assertEquals(20, out.get(0).split(" ").length);
    }

    @Test
    public void test_per_line() throws Exception {
        String absolutePath = path("perline");
        MinhashApp.main(new String[] {
            "-ll", "-f", absolutePath
        });
        List<String> out = output();
        out.forEach(System.out::println);
        assertEquals(26, out.size());
        assertEquals(128, out.get(13).split(" ").length);
    }

    @Test
    public void test_string() throws Exception {
        MinhashApp.main(new String[] {
            "test"
        });
        List<String> out = output();
        out.forEach(System.out::println);
        assertEquals(1, out.size());
        assertEquals(128, out.get(0).split(" ").length);
    }

    @Test
    public void test_ignore_whitespaces() throws Exception {
        MinhashApp.main(new String[] {
            "-s", "123", "test1 test2"
        });
        List<String> out1 = output();
        out1.forEach(System.out::println);
        baos.reset();
        MinhashApp.main(new String[] {
            "-s", "123", "test1     test2"
        });
        List<String> out2 = output();
        out2.forEach(System.out::println);

        assertEquals(out1, out2);
    }

    private List<String> testSeed(String absolutePath, long s) throws Exception {
        MinhashApp.main(new String[] {
            "-s", "" + s, "-f", absolutePath
        });
        List<String> out = output();
        assertEquals(1, out.size());
        assertEquals(128, out.get(0).split(" ").length);
        return out;
    }

    private String path(String fileName) throws URISyntaxException {
        URL res = getClass().getResource("/" + fileName);
        File file = Paths.get(res.toURI()).toFile();
        return file.getAbsolutePath();
    }

    private List<String> output() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray())));
        return reader.lines().collect(toList());
    }
}
