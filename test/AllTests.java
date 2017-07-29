import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import retuss.ClassDiagramDrawerTest;
import retuss.ControllerTest;
import retuss.RetussWindowTest;
import retuss.UtilityJavaFXComponentTest;

@RunWith( Suite.class )
@Suite.SuiteClasses( {
        RetussWindowTest.class,
        ControllerTest.class,
        ClassDiagramDrawerTest.class,
        UtilityJavaFXComponentTest.class
} )
public class AllTests {
}
