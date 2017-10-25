package retuss;

import javafx.geometry.Point2D;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CompositionEdgeDiagramTest {
    @Rule
    public ExpectedException indexOutOfBoundsException = ExpectedException.none();

    CompositionEdgeDiagram obj;

    Point2D firstClassPoint;
    Point2D secondClassPoint;

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

    @Test
    public void 関連の関係元を選択しているかどうかを確認する() {
        boolean firstExpected = false;
        boolean secondExpected = true;
        boolean thirdExpected = false;

        boolean firstActual = obj.hasRelationSourceNodeSelected();
        obj.changeRelationSourceNodeSelectedState();
        boolean secondActual = obj.hasRelationSourceNodeSelected();
        obj.changeRelationSourceNodeSelectedState();
        boolean thirdActual = obj.hasRelationSourceNodeSelected();

        assertThat( firstActual, is( firstExpected ) );
        assertThat( secondActual, is( secondExpected ) );
        assertThat( thirdActual, is( thirdExpected ) );
    }

    @Test
    public void 関連の関係元を選択しているかどうかをリセットする() {
        boolean expected = false;

        obj.changeRelationSourceNodeSelectedState();
        obj.resetRelationSourceNodeSelectedState();
        boolean actual = obj.hasRelationSourceNodeSelected();

        assertThat( actual, is( expected ) );
    }

    @Test
    public void コンポジションの関係先のポイントを設定する() {
        Point2D expected = new Point2D( 100.0, 200.0 );

        obj.createEdgeText( ContentType.Composition, "- composition" );
        obj.setRelationPoint( ContentType.Composition, 0, expected );
        Point2D actual = obj.getRelationPoint( ContentType.Composition, 0 );

        assertThat( actual, is( expected ) );
    }

    @Test
    public void コンポジションの関係元のポイントを設定する() {
        Point2D expected = new Point2D( 300.0, 400.0 );

        obj.createEdgeText( ContentType.Composition, "- composition" );
        obj.setRelationSourcePoint( ContentType.Composition, 0, expected );
        Point2D actual = obj.getRelationSourcePoint( ContentType.Composition, 0 );

        assertThat( actual, is( expected ) );
    }

    @Test
    public void コンポジションの関係先と関係元のX軸が同じ場合その中間点は関係を描画している() {
        firstClassPoint = new Point2D( 100.0, 200.0 );
        secondClassPoint = new Point2D( 100.0, 400.0 );
        Point2D checkPoint = new Point2D( firstClassPoint.getX(), secondClassPoint.getY() - ( secondClassPoint.getY() - firstClassPoint.getY() ) / 2 );
        boolean expected = true;

        obj.createEdgeText( ContentType.Composition, "- composition" );
        obj.setRelationPoint( ContentType.Composition, 0, secondClassPoint );
        obj.setRelationSourcePoint( ContentType.Composition, 0, firstClassPoint );
        boolean actual = obj.isAlreadyDrawnAnyEdge( ContentType.Composition, 0, checkPoint );

        assertThat( actual, is( expected ) );
    }

    @Test
    public void コンポジションの関係先と関係元のY軸が同じ場合その中間点は関係を描画している() {
        firstClassPoint = new Point2D( 100.0, 200.0 );
        secondClassPoint = new Point2D( 300.0, 200.0 );
        Point2D checkPoint = new Point2D( firstClassPoint.getX() + ( secondClassPoint.getX() - firstClassPoint.getX() ) / 2, firstClassPoint.getY() );
        boolean expected = true;

        obj.createEdgeText( ContentType.Composition, "- composition" );
        obj.setRelationPoint( ContentType.Composition, 0, secondClassPoint );
        obj.setRelationSourcePoint( ContentType.Composition, 0, firstClassPoint );
        boolean actual = obj.isAlreadyDrawnAnyEdge( ContentType.Composition, 0, checkPoint );

        assertThat( actual, is( expected ) );
    }

    @Ignore( "関係を描画している箇所か否かを確認できていない" )
    @Test
    public void コンポジションの関係先と関係元の中間点は関係を描画している() {
        firstClassPoint = new Point2D( 100.0, 200.0 );
        secondClassPoint = new Point2D( 300.0, 400.0 );
        Point2D checkPoint = new Point2D(
                firstClassPoint.getX() + ( secondClassPoint.getX() - firstClassPoint.getX() ) / 2,
                firstClassPoint.getY() + ( secondClassPoint.getY() - firstClassPoint.getY() ) / 2 );
        boolean expected = true;

        obj.createEdgeText( ContentType.Composition, "- composition" );
        obj.setRelationPoint( ContentType.Composition, 0, secondClassPoint );
        obj.setRelationSourcePoint( ContentType.Composition, 0, firstClassPoint );
        boolean actual = obj.isAlreadyDrawnAnyEdge( ContentType.Composition, 0, checkPoint );

        assertThat( actual, is( expected ) );
    }

    @Ignore( "関係を描画している箇所か否かを確認できていない" )
    @Test
    public void コンポジションの関係先と関係元の中間点から余地の長さ以上離れた点は関係を描画していない() {
        firstClassPoint = new Point2D( 100.0, 200.0 );
        secondClassPoint = new Point2D( 300.0, 400.0 );
        double beyondMarginLength = 10.0;
        Point2D checkPoint = new Point2D(
                firstClassPoint.getX() + ( secondClassPoint.getX() - firstClassPoint.getX() ) / 2 + beyondMarginLength,
                firstClassPoint.getY() + ( secondClassPoint.getY() - firstClassPoint.getY() ) / 2 + beyondMarginLength );
        boolean expected = false;

        obj.createEdgeText( ContentType.Composition, "- composition" );
        obj.setRelationPoint( ContentType.Composition, 0, secondClassPoint );
        obj.setRelationSourcePoint( ContentType.Composition, 0, firstClassPoint );
        boolean actual = obj.isAlreadyDrawnAnyEdge( ContentType.Composition, 0, checkPoint );

        assertThat( actual, is( expected ) );
    }
}