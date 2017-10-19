package retuss;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CompositionEdgeDiagramTest {
    CompositionEdgeDiagram obj;

    @Before
    public void setObj() {
        obj = new CompositionEdgeDiagram();
    }


    @Test
    public void コンポジションを追加する() {
        String className = "ClassName";
        String expected = "- composition";

        obj.createEdgeText( ContentType.Title, className );
        obj.createEdgeText( ContentType.Composition, expected );
        String actual = obj.getEdgeContentText( ContentType.Composition, 0 );

        assertThat( actual, is( expected ) );
    }

    @Test
    public void コンポジションを変更する() {
        String className = "ClassName";
        String firstInputClassComposition = "- composition";
        String expected = "- changedComposition";

        obj.createEdgeText( ContentType.Title, className );
        obj.createEdgeText( ContentType.Composition, firstInputClassComposition );
        obj.changeEdgeText( ContentType.Composition, 0, expected );
        String actual = obj.getEdgeContentText( ContentType.Composition, 0 );

        assertThat( actual, is( expected ) );
    }

    @Test
    public void コンポジションを削除する() {
        String className = "ClassName";
        String expected = "- composition";

        obj.createEdgeText( ContentType.Title, className );
        obj.createEdgeText( ContentType.Composition, expected );
        obj.deleteEdgeText( ContentType.Composition, 0 );

        // indexOutOfBoundsException.expect( IndexOutOfBoundsException.class );
        // obj.getNodeContentText( ContentType.Composition, 0 );
    }
}