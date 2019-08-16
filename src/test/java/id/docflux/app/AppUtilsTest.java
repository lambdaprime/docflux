package id.docflux.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AppUtilsTest {

    @Test
    public void asSet_no_spaces() throws Exception {
        assertEquals(2, AppUtils.asSet("Hello     asSet").size());
    }

}
