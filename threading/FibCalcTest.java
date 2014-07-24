import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by andrey.kravets on 7/24/2014.
 */
public class FibCalcTest extends TestCase {

    private FibCalc fibonacciCalc;

    @Before
    public void init() {
        fibonacciCalc = new FibCalcImpl();
    }

    @Test
    public void testFib() {
        assertEquals(55, fibonacciCalc.fib(10));
        assertEquals(6765, fibonacciCalc.fib(20));
    }

}
