package retuss;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CompositionEdgeDiagramTest {
    @Rule
    public ExpectedException indexOutOfBoundsException = ExpectedException.none();

    CompositionEdgeDiagram obj;

    @Before
    public void setObj() {
        obj = new CompositionEdgeDiagram();
    }

    @Test
    public void コンポジションを追加する() {
        String expected = "- composition";

        obj.createEdgeText( ContentType.Composition, expected );
        String actual = obj.getEdgeContentText( ContentType.Composition, 0 );

        assertThat( actual, is( expected ) );
        assertThat( obj.getCompositionsCount(), is( 1 ));
    }

    @Test
    public void コンポジションを変更する() {
        String firstInputClassComposition = "- composition";
        String expected = "- changedComposition";

        obj.createEdgeText( ContentType.Composition, firstInputClassComposition );
        obj.changeEdgeText( ContentType.Composition, 0, expected );
        String actual = obj.getEdgeContentText( ContentType.Composition, 0 );

        assertThat( actual, is( expected ) );
    }

    @Test
    public void コンポジションを削除する() {
        String expected = "- composition";

        obj.createEdgeText( ContentType.Composition, expected );
        obj.deleteEdgeText( ContentType.Composition, 0 );

        indexOutOfBoundsException.expect( IndexOutOfBoundsException.class );
        obj.getEdgeContentText( ContentType.Composition, 0 );
    }

    @Test
    public void コンポジションの関連先を設定する() {
        int expected = 1;
        obj.createEdgeText( ContentType.Composition, "- composition" );

        obj.setRelationId( ContentType.Composition, 0, expected );

        assertThat( obj.getRelationId( ContentType.Composition, 0 ), is( expected ) );
    }

    @Test
    public void コンポジションの関連元を設定する() {
        int expected = 0;
        obj.createEdgeText( ContentType.Composition, "- composition" );

        obj.setRelationSourceId( ContentType.Composition, 0, expected );

        assertThat( obj.getRelationSourceId( ContentType.Composition, 0 ), is( expected ) );
    }
}