package id.minhash;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

public class MinhashTest {

    @Test
    public void test_minwise() throws Exception {
    	Random rand = new Random();
    	IntStream.range(0, 100).forEach(i -> {
	    	Minhash.init(Optional.of((long)rand.nextInt(Integer.MAX_VALUE)), Optional.of(32),
	    	    Optional.empty());
	        Minhash m = new Minhash("there is no spoon");
	        String dump = m.dump();
	        System.out.println(dump);
	        System.out.println(m);
			assertTrue(dump.contains("there"));
			assertTrue(dump.contains("is"));
			assertTrue(dump.contains("no"));
			assertTrue(dump.contains("spoon"));
    	});
    }

    @Test
    public void test_distribution() throws Exception {
    	Random rand = new Random();
    	IntStream.range(0, 100).forEach(t -> {
	    	Minhash.init(Optional.of((long)rand.nextInt(Integer.MAX_VALUE)), Optional.of(32),
	    	    Optional.empty());
	        Minhash m = new Minhash("All those moments will be lost in time, like tears in rain.");
	        String dump = m.dump();
	        System.out.println(dump);
	        System.out.println(m);
	        Matcher matcher = Pattern.compile("=(\\d+)").matcher(dump);
	        boolean exceed = matcher.results()
	        	.map(r -> r.group(1))
	        	.mapToInt(Integer::parseInt)
	        	.anyMatch(i -> i > 10);
	        assertFalse(exceed);
    	});
    }
}
