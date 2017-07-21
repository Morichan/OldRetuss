import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import retuss.ControllerTest;
import retuss.RetussWindowTest;

@RunWith( Suite.class )
@Suite.SuiteClasses( { RetussWindowTest.class, ControllerTest.class } )
public class AllTests {
}
