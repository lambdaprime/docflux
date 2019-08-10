package id.docflux.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MinhashAppTest {

    @BeforeEach
    public void setup() {
    }

    @Test
    public void test_all_defaults() throws Exception {
        String absolutePath = path("doc1");
        var executor = new BackgroundMinhashAppExecutor();
        List<String> out = executor.run(new String[] {
            absolutePath
        });
        out.forEach(System.out::println);
        assertEquals(1, out.size());
        assertEquals(128, out.get(0).split(" ").length);
    }

    @Test
    public void test_seed() throws Exception {
        String absolutePath = path("doc1");

        List<String> out1 = testSeed(absolutePath, 123);
        out1.forEach(System.out::println);
        List<String> out2 = testSeed(absolutePath, 123);
        out2.forEach(System.out::println);
        
        assertEquals(out1, out2);

    }

    @Test
    public void test_length() throws Exception {
        String absolutePath = path("doc1");
        var executor = new BackgroundMinhashAppExecutor();
        List<String> out = executor.run(new String[] {
            "-l", "20", absolutePath
        });
        out.forEach(System.out::println);
        assertEquals(1, out.size());
        assertEquals(20, out.get(0).split(" ").length);
    }

    @Test
    public void test_per_line() throws Exception {
        String absolutePath = path("perline");
        var executor = new BackgroundMinhashAppExecutor();
        List<String> out = executor.run(new String[] {
            "-ll", "-f", absolutePath
        });
        out.forEach(System.out::println);
        assertEquals(26, out.size());
        assertEquals(128, out.get(13).split(" ").length);
    }

    @Test
    public void test_string() throws Exception {
        var executor = new BackgroundMinhashAppExecutor();
        List<String> out = executor.run(new String[] {
            "test"
        });
        out.forEach(System.out::println);
        assertEquals(1, out.size());
        assertEquals(128, out.get(0).split(" ").length);
    }

    @Test
    public void test_ignore_whitespaces() throws Exception {
        var executor1 = new BackgroundMinhashAppExecutor();
        List<String> out1 = executor1.run(new String[] {
            "-s", "123", "test1 test2"
        });
        out1.forEach(System.out::println);
        var executor2 = new BackgroundMinhashAppExecutor();
        List<String> out2 = executor2.run(new String[] {
            "-s", "123", "test1     test2"
        });
        out2.forEach(System.out::println);

        assertEquals(out1, out2);
    }

    private List<String> testSeed(String absolutePath, long s) throws Exception {
        var executor = new BackgroundMinhashAppExecutor();
        List<String> out = executor.run(new String[] {
            "-s", "" + s, "-f", absolutePath
        });
        assertEquals(1, out.size());
        assertEquals(128, out.get(0).split(" ").length);
        return out;
    }

    private String path(String fileName) throws URISyntaxException {
        URL res = getClass().getResource("/" + fileName);
        File file = Paths.get(res.toURI()).toFile();
        return file.getAbsolutePath();
    }

}
