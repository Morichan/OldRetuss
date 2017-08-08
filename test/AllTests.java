import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import retuss.*;

@RunWith( Suite.class )
@Suite.SuiteClasses( {
        RetussWindowTest.class,
        ControllerTest.class,
        ClassDiagramDrawerTest.class,
        UtilityJavaFXComponentTest.class,
        ClassNodeDiagramTest.class
} )
public class AllTests {
}
