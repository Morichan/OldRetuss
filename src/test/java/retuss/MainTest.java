package retuss;

import org.junit.jupiter.api.Test;
import retuss.Main;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    Main main = new Main();
    @Test
    public void test() {
        String expected = "test";
        String actual = main.testMethod( expected );
        assertEquals( actual, expected );
    }
    @Test
    public void test2() {
        fail("not yet implement");
    }
}