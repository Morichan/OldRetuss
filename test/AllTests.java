import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import retuss.ClassDiagramCanvasTest;
import retuss.ControllerTest;
import retuss.RetussWindowTest;
import retuss.UtilityJavaFXComponentTest;

@RunWith( Suite.class )
@Suite.SuiteClasses( {
        RetussWindowTest.class,
        ControllerTest.WithoutGuiTest.class,
        ControllerTest.class,
        ClassDiagramCanvasTest.class,
        UtilityJavaFXComponentTest.class
} )
public class AllTests {
}
