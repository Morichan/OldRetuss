package retuss;

import javafx.geometry.Point2D;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;

import static jdk.nashorn.internal.objects.Global.Infinity;

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

    @Test
    public void コンポジションの関係先と関係元の中間点から余地の長さ以上離れた点は関係を描画していない() {
        firstClassPoint = new Point2D( 100.0, 200.0 );
        secondClassPoint = new Point2D( 300.0, 400.0 );
        double beyondMarginLength = 10.0;
        Point2D checkPoint = new Point2D(
                firstClassPoint.getX() + ( secondClassPoint.getX() - firstClassPoint.getX() ) / 2 + beyondMarginLength,
                firstClassPoint.getY() + ( secondClassPoint.getY() - firstClassPoint.getY() ) / 2 - beyondMarginLength );
        boolean expected = false;

        obj.createEdgeText( ContentType.Composition, "- composition" );
        obj.setRelationPoint( ContentType.Composition, 0, secondClassPoint );
        obj.setRelationSourcePoint( ContentType.Composition, 0, firstClassPoint );
        boolean actual = obj.isAlreadyDrawnAnyEdge( ContentType.Composition, 0, checkPoint );

        assertThat( actual, is( expected ) );
    }

    @Test
    public void 関係先と関係元のポイントを入れると余地を含んだ右回りで4隅のポイントを持つ範囲を返す() {
        Point2D centerPoint = new Point2D( 100.0, 100.0 );
        Point2D upperPoint = new Point2D( 100.0, 0.0 );
        Point2D righterPoint = new Point2D( 200.0, 100.0 );
        Point2D bottomPoint = new Point2D( 100.0, 200.0 );
        Point2D lefterPoint = new Point2D( 0.0, 100.0 );
        Point2D upperRightPoint = new Point2D( 200.0, 0.0 );
        Point2D bottomRightPoint = new Point2D( 200.0, 200.0 );
        Point2D bottomLeftPoint = new Point2D( 0.0, 200.0 );
        Point2D upperLeftPoint = new Point2D( 0.0, 0.0 );
        double root2 = Math.sqrt( 2.0 );
        List< Point2D > expected1 = Arrays.asList(
                new Point2D( 98.0, 100.0 ), new Point2D( 98.0, 0.0 ),
                new Point2D( 102.0, 0.0 ), new Point2D( 102.0, 100.0 )
        );
        List< Point2D > expected2 = Arrays.asList(
                new Point2D( 100.0, 98.0 ), new Point2D( 200.0, 98.0 ),
                new Point2D( 200.0, 102.0 ), new Point2D( 100.0, 102.0 )
        );
        List< Point2D > expected3 = Arrays.asList(
                new Point2D( 102.0, 100.0 ), new Point2D( 102.0, 200.0 ),
                new Point2D( 98.0, 200.0 ), new Point2D( 98.0, 100.0 )
        );
        List< Point2D > expected4 = Arrays.asList(
                new Point2D( 100.0, 102.0 ), new Point2D( 0.0, 102.0 ),
                new Point2D( 0.0, 98.0 ), new Point2D( 100.0, 98.0 )
        );
        List< Point2D > expected5 = Arrays.asList(
                new Point2D( 100.0 - root2, 100.0 - root2 ), new Point2D( 200.0 - root2, -root2 ),
                new Point2D( 200.0 + root2, root2 ), new Point2D( 100.0 + root2, 100.0 + root2 )
        );
        List< Point2D > expected6 = Arrays.asList(
                new Point2D( 100.0 + root2, 100.0 - root2 ), new Point2D( 200.0 + root2, 200.0 - root2 ),
                new Point2D( 200.0 - root2, 200.0 + root2 ), new Point2D( 100.0 - root2, 100.0 + root2 )
        );
        List< Point2D > expected7 = Arrays.asList(
                new Point2D( 100.0 + root2, 100.0 + root2 ), new Point2D( root2, 200.0 + root2 ),
                new Point2D( -root2, 200.0 - root2 ), new Point2D( 100.0 - root2, 100.0 - root2 )
        );
        List< Point2D > expected8 = Arrays.asList(
                new Point2D( 100.0 - root2, 100.0 + root2 ), new Point2D( -root2, root2 ),
                new Point2D( root2, -root2 ), new Point2D( 100.0 + root2, 100.0 - root2 )
        );

        List< Point2D > actual1 = obj.createOneEdgeQuadrangleWithMargin( 2.0, centerPoint, upperPoint );
        List< Point2D > actual2 = obj.createOneEdgeQuadrangleWithMargin( 2.0, centerPoint, righterPoint );
        List< Point2D > actual3 = obj.createOneEdgeQuadrangleWithMargin( 2.0, centerPoint, bottomPoint );
        List< Point2D > actual4 = obj.createOneEdgeQuadrangleWithMargin( 2.0, centerPoint, lefterPoint );
        List< Point2D > actual5 = obj.createOneEdgeQuadrangleWithMargin( 2.0, centerPoint, upperRightPoint );
        List< Point2D > actual6 = obj.createOneEdgeQuadrangleWithMargin( 2.0, centerPoint, bottomRightPoint );
        List< Point2D > actual7 = obj.createOneEdgeQuadrangleWithMargin( 2.0, centerPoint, bottomLeftPoint );
        List< Point2D > actual8 = obj.createOneEdgeQuadrangleWithMargin( 2.0, centerPoint, upperLeftPoint );

        assertThat( actual1, is( expected1 ) );
        assertThat( actual2, is( expected2 ) );
        assertThat( actual3, is( expected3 ) );
        assertThat( actual4, is( expected4 ) );
        assertThat( actual5, is( expected5 ) );
        assertThat( actual6, is( expected6 ) );
        assertThat( actual7, is( expected7 ) );
        assertThat( actual8, is( expected8 ) );
    }

    @Test
    public void 関係先と関係元のポイントを入れると法線の傾きを計算する() {
        Point2D centerPoint = new Point2D( 100.0, 100.0 );
        Point2D upperPoint = new Point2D( 100.0, 0.0 );
        Point2D righterPoint = new Point2D( 200.0, 100.0 );
        Point2D bottomPoint = new Point2D( 100.0, 200.0 );
        Point2D lefterPoint = new Point2D( 0.0, 100.0 );
        Point2D upperRightPoint = new Point2D( 200.0, 0.0 );
        Point2D bottomRightPoint = new Point2D( 200.0, 200.0 );
        Point2D bottomLeftPoint = new Point2D( 0.0, 200.0 );
        Point2D upperLeftPoint = new Point2D( 0.0, 0.0 );

        double actual1 = obj.calculateNormalLineInclination( centerPoint, upperPoint ); // 北
        double actual2 = obj.calculateNormalLineInclination( centerPoint, righterPoint ); // 東
        double actual3 = obj.calculateNormalLineInclination( centerPoint, bottomPoint ); // 南
        double actual4 = obj.calculateNormalLineInclination( centerPoint, lefterPoint ); // 西
        double actual5 = obj.calculateNormalLineInclination( centerPoint, upperRightPoint ); // 北東
        double actual6 = obj.calculateNormalLineInclination( centerPoint, bottomRightPoint ); // 南東
        double actual7 = obj.calculateNormalLineInclination( centerPoint, bottomLeftPoint ); // 南西
        double actual8 = obj.calculateNormalLineInclination( centerPoint, upperLeftPoint ); // 北西

        assertThat( actual1, is( 0.0 ) );
        assertThat( actual2, is( Infinity ) );
        assertThat( actual3, is( 0.0 ) );
        assertThat( actual4, is( Infinity ) );
        assertThat( actual5, is( -1.0 ) );
        assertThat( actual6, is( 1.0 ) );
        assertThat( actual7, is( -1.0 ) );
        assertThat( actual8, is( 1.0 ) );
    }

    @Test
    public void 関係のタイプを返す() {
        ContentType expected = ContentType.Composition;

        obj.createEdgeText( expected, "- composition" );
        ContentType actual = obj.getContentType( 0 );

        assertThat( actual, is( expected ) );
    }

    @Test
    public void 関係の内容を返す() {
        RelationshipAttribution expected = new RelationshipAttribution( "- composition" );
        firstClassPoint = new Point2D( 100.0, 200.0 );
        secondClassPoint = new Point2D( 300.0, 400.0 );

        obj.createEdgeText( ContentType.Composition, "- composition" );
        obj.setRelationPoint( ContentType.Composition, 0, secondClassPoint );
        obj.setRelationSourcePoint( ContentType.Composition, 0, firstClassPoint );
        RelationshipAttribution actual = obj.searchCurrentRelation( firstClassPoint );

        assertThat( actual.getName(), is( expected.getName() ) );
    }

    @Test
    public void 関係の内容名を変更する() {
        RelationshipAttribution expected = new RelationshipAttribution( "+ changedComposition" );
        firstClassPoint = new Point2D( 100.0, 200.0 );
        secondClassPoint = new Point2D( 300.0, 400.0 );
        Point2D betweenFirstAndSecondClassPoint = new Point2D( 200.0, 300.0 );

        obj.createEdgeText( ContentType.Composition, "- composition" );
        obj.setRelationPoint( ContentType.Composition, 0, secondClassPoint );
        obj.setRelationSourcePoint( ContentType.Composition, 0, firstClassPoint );
        obj.changeCurrentRelation( betweenFirstAndSecondClassPoint, "+ changedComposition" );
        RelationshipAttribution actual = obj.searchCurrentRelation( betweenFirstAndSecondClassPoint );

        assertThat( actual.getName(), is( expected.getName() ) );
    }

    @Test
    public void 関係の内容を削除する() {
        RelationshipAttribution expected = null;
        firstClassPoint = new Point2D( 100.0, 200.0 );
        secondClassPoint = new Point2D( 300.0, 400.0 );
        Point2D betweenFirstAndSecondClassPoint = new Point2D( 200.0, 300.0 );

        obj.createEdgeText( ContentType.Composition, "- composition" );
        obj.setRelationPoint( ContentType.Composition, 0, secondClassPoint );
        obj.setRelationSourcePoint( ContentType.Composition, 0, firstClassPoint );
        obj.deleteCurrentRelation( betweenFirstAndSecondClassPoint );
        RelationshipAttribution actual = obj.searchCurrentRelation( betweenFirstAndSecondClassPoint );

        assertThat( actual, is( expected ) );
    }

    @Test
    public void コンポジションを複数追加する() {
        String expected1 = "- composition1";
        String expected2 = "- composition2";
        String expected3 = "- composition3";

        obj.createEdgeText( ContentType.Composition, expected1 );
        obj.createEdgeText( ContentType.Composition, expected2 );
        obj.createEdgeText( ContentType.Composition, expected3 );
        String actual1 = obj.getEdgeContentText( ContentType.Composition, 0 );
        String actual2 = obj.getEdgeContentText( ContentType.Composition, 1 );
        String actual3 = obj.getEdgeContentText( ContentType.Composition, 2 );

        assertThat( actual1, is( expected1 ) );
        assertThat( actual2, is( expected2 ) );
        assertThat( actual3, is( expected3 ) );
        assertThat( obj.getCompositionsCount(), is( 3 ));
    }

    @Test
    public void 関係先のポイントと関係元のポイントと関係先の幅を入力すると関係と関係先の側面との接点のポイントを返す() {
        Point2D centerPoint = new Point2D( 100.0, 100.0 );
        Point2D upperPoint = new Point2D( 100.0, 0.0 );
        Point2D righterPoint = new Point2D( 200.0, 100.0 );
        Point2D bottomPoint = new Point2D( 100.0, 200.0 );
        Point2D lefterPoint = new Point2D( 0.0, 100.0 );
        Point2D upperRightPoint = new Point2D( 200.0, 0.0 );
        Point2D bottomRightPoint = new Point2D( 200.0, 200.0 );
        Point2D bottomLeftPoint = new Point2D( 0.0, 200.0 );
        Point2D upperLeftPoint = new Point2D( 0.0, 0.0 );

        Point2D actual1 = obj.calculateIntersectionPointLineAndEndNodeSide( upperPoint, centerPoint, 100.0, 80.0 );
        Point2D actual2 = obj.calculateIntersectionPointLineAndEndNodeSide( righterPoint, centerPoint, 100.0, 80.0 );
        Point2D actual3 = obj.calculateIntersectionPointLineAndEndNodeSide( bottomPoint, centerPoint, 100.0, 80.0 );
        Point2D actual4 = obj.calculateIntersectionPointLineAndEndNodeSide( lefterPoint, centerPoint, 100.0, 80.0 );
        Point2D actual5 = obj.calculateIntersectionPointLineAndEndNodeSide( upperRightPoint, centerPoint, 100.0, 80.0 );
        Point2D actual6 = obj.calculateIntersectionPointLineAndEndNodeSide( bottomRightPoint, centerPoint, 100.0, 80.0 );
        Point2D actual7 = obj.calculateIntersectionPointLineAndEndNodeSide( bottomLeftPoint, centerPoint, 100.0, 80.0 );
        Point2D actual8 = obj.calculateIntersectionPointLineAndEndNodeSide( upperLeftPoint, centerPoint, 100.0, 80.0 );

        Point2D actual9 = obj.calculateIntersectionPointLineAndEndNodeSide( upperRightPoint, centerPoint, 100.0, 100.0 );
        Point2D actual10 = obj.calculateIntersectionPointLineAndEndNodeSide( bottomRightPoint, centerPoint, 100.0, 100.0 );
        Point2D actual11 = obj.calculateIntersectionPointLineAndEndNodeSide( bottomLeftPoint, centerPoint, 100.0, 100.0 );
        Point2D actual12 = obj.calculateIntersectionPointLineAndEndNodeSide( upperLeftPoint, centerPoint, 100.0, 100.0 );
        Point2D actual13 = obj.calculateIntersectionPointLineAndEndNodeSide( upperRightPoint, centerPoint, 100.0, 150.0 );
        Point2D actual14 = obj.calculateIntersectionPointLineAndEndNodeSide( upperLeftPoint, centerPoint, 100.0, 150.0 );

        assertThat( actual1, is( new Point2D( 100.0, 60.0 ) ) );
        assertThat( actual2, is( new Point2D( 150.0, 100.0 ) ) );
        assertThat( actual3, is( new Point2D( 100.0, 140.0 ) ) );
        assertThat( actual4, is( new Point2D( 50.0, 100.0 ) ) );
        assertThat( actual5, is( new Point2D( 140.0, 60.0 ) ) );
        assertThat( actual6, is( new Point2D( 140.0, 140.0 ) ) );
        assertThat( actual7, is( new Point2D( 60.0, 140.0 ) ) );
        assertThat( actual8, is( new Point2D( 60.0, 60.0 ) ) );

        assertThat( actual9, is( new Point2D( 150.0, 50.0 ) ) );
        assertThat( actual10, is( new Point2D( 150.0, 150.0 ) ) );
        assertThat( actual11, is( new Point2D( 50.0, 150.0 ) ) );
        assertThat( actual12, is( new Point2D( 50.0, 50.0 ) ) );
        assertThat( actual13, is( new Point2D( 150.0, 50.0 ) ) );
        assertThat( actual14, is( new Point2D( 50.0, 50.0 ) ) );
    }

    @Test
    public void 関係先のポイントと関係元のポイントを入力すると最初のポイントが高い場合は真を返す() {
        Point2D centerPoint = new Point2D( 100.0, 100.0 );
        Point2D upperPoint = new Point2D( 100.0, 0.0 );
        Point2D righterPoint = new Point2D( 200.0, 100.0 );
        Point2D bottomPoint = new Point2D( 100.0, 200.0 );
        Point2D lefterPoint = new Point2D( 0.0, 100.0 );
        Point2D upperRightPoint = new Point2D( 200.0, 0.0 );
        Point2D bottomRightPoint = new Point2D( 200.0, 200.0 );
        Point2D bottomLeftPoint = new Point2D( 0.0, 200.0 );
        Point2D upperLeftPoint = new Point2D( 0.0, 0.0 );

        boolean actual1 = obj.isHigherThanSecondNodeThatFirstNode( centerPoint, upperPoint );
        boolean actual2 = obj.isHigherThanSecondNodeThatFirstNode( centerPoint, righterPoint );
        boolean actual3 = obj.isHigherThanSecondNodeThatFirstNode( centerPoint, bottomPoint );
        boolean actual4 = obj.isHigherThanSecondNodeThatFirstNode( centerPoint, lefterPoint );
        boolean actual5 = obj.isHigherThanSecondNodeThatFirstNode( centerPoint, upperRightPoint );
        boolean actual6 = obj.isHigherThanSecondNodeThatFirstNode( centerPoint, bottomRightPoint );
        boolean actual7 = obj.isHigherThanSecondNodeThatFirstNode( centerPoint, bottomLeftPoint );
        boolean actual8 = obj.isHigherThanSecondNodeThatFirstNode( centerPoint, upperLeftPoint );

        assertThat( actual1, is( false ) );
        assertThat( actual2, is( false ) );
        assertThat( actual3, is( true ) );
        assertThat( actual4, is( false ) );
        assertThat( actual5, is( false ) );
        assertThat( actual6, is( true ) );
        assertThat( actual7, is( true ) );
        assertThat( actual8, is( false ) );
    }

    @Test
    public void 関係先のポイントと関係元のポイントを入力すると最初のポイントが左側にある場合は真を返す() {
        Point2D centerPoint = new Point2D( 100.0, 100.0 );
        Point2D upperPoint = new Point2D( 100.0, 0.0 );
        Point2D righterPoint = new Point2D( 200.0, 100.0 );
        Point2D bottomPoint = new Point2D( 100.0, 200.0 );
        Point2D lefterPoint = new Point2D( 0.0, 100.0 );
        Point2D upperRightPoint = new Point2D( 200.0, 0.0 );
        Point2D bottomRightPoint = new Point2D( 200.0, 200.0 );
        Point2D bottomLeftPoint = new Point2D( 0.0, 200.0 );
        Point2D upperLeftPoint = new Point2D( 0.0, 0.0 );

        boolean actual1 = obj.isLefterThanSecondNodeThatFirstNode( centerPoint, upperPoint );
        boolean actual2 = obj.isLefterThanSecondNodeThatFirstNode( centerPoint, righterPoint );
        boolean actual3 = obj.isLefterThanSecondNodeThatFirstNode( centerPoint, bottomPoint );
        boolean actual4 = obj.isLefterThanSecondNodeThatFirstNode( centerPoint, lefterPoint );
        boolean actual5 = obj.isLefterThanSecondNodeThatFirstNode( centerPoint, upperRightPoint );
        boolean actual6 = obj.isLefterThanSecondNodeThatFirstNode( centerPoint, bottomRightPoint );
        boolean actual7 = obj.isLefterThanSecondNodeThatFirstNode( centerPoint, bottomLeftPoint );
        boolean actual8 = obj.isLefterThanSecondNodeThatFirstNode( centerPoint, upperLeftPoint );

        assertThat( actual1, is( false ) );
        assertThat( actual2, is( true ) );
        assertThat( actual3, is( false ) );
        assertThat( actual4, is( false ) );
        assertThat( actual5, is( true ) );
        assertThat( actual6, is( true ) );
        assertThat( actual7, is( false ) );
        assertThat( actual8, is( false ) );
    }

    @Test
    public void 関係先のポイントと関係元のポイントと関係先の幅と関係先の高さを入力すると関係先の上下の側辺と関係の線が交差している場合は真を返す() {
        Point2D centerPoint = new Point2D( 100.0, 100.0 );
        Point2D upperPoint = new Point2D( 100.0, 0.0 );
        Point2D righterPoint = new Point2D( 200.0, 100.0 );
        Point2D bottomPoint = new Point2D( 100.0, 200.0 );
        Point2D lefterPoint = new Point2D( 0.0, 100.0 );
        Point2D upperRightPoint = new Point2D( 200.0, 0.0 );
        Point2D bottomRightPoint = new Point2D( 200.0, 200.0 );
        Point2D bottomLeftPoint = new Point2D( 0.0, 200.0 );
        Point2D upperLeftPoint = new Point2D( 0.0, 0.0 );
        double wideWidth = 100.0;
        double narrowWidth = 10.0;
        double highHeight = 100.0;
        double lowHeight = 10.0;

        // 幅が広く高さが低い場合
        boolean actual1 = obj.isIntersectedFromUpperOrBottomSideInSecondNode( centerPoint, upperPoint, wideWidth, lowHeight );
        boolean actual2 = obj.isIntersectedFromUpperOrBottomSideInSecondNode( centerPoint, righterPoint, wideWidth, lowHeight );
        boolean actual3 = obj.isIntersectedFromUpperOrBottomSideInSecondNode( centerPoint, bottomPoint, wideWidth, lowHeight );
        boolean actual4 = obj.isIntersectedFromUpperOrBottomSideInSecondNode( centerPoint, lefterPoint, wideWidth, lowHeight );
        boolean actual5 = obj.isIntersectedFromUpperOrBottomSideInSecondNode( centerPoint, upperRightPoint, wideWidth, lowHeight );
        boolean actual6 = obj.isIntersectedFromUpperOrBottomSideInSecondNode( centerPoint, bottomRightPoint, wideWidth, lowHeight );
        boolean actual7 = obj.isIntersectedFromUpperOrBottomSideInSecondNode( centerPoint, bottomLeftPoint, wideWidth, lowHeight );
        boolean actual8 = obj.isIntersectedFromUpperOrBottomSideInSecondNode( centerPoint, upperLeftPoint, wideWidth, lowHeight );

        // 幅が狭く高さが高い場合
        boolean actual9 = obj.isIntersectedFromUpperOrBottomSideInSecondNode( centerPoint, upperPoint, narrowWidth, highHeight );;
        boolean actual10 = obj.isIntersectedFromUpperOrBottomSideInSecondNode( centerPoint, righterPoint, narrowWidth, highHeight );
        boolean actual11 = obj.isIntersectedFromUpperOrBottomSideInSecondNode( centerPoint, bottomPoint, narrowWidth, highHeight );
        boolean actual12 = obj.isIntersectedFromUpperOrBottomSideInSecondNode( centerPoint, lefterPoint, narrowWidth, highHeight );
        boolean actual13 = obj.isIntersectedFromUpperOrBottomSideInSecondNode( centerPoint, upperRightPoint, narrowWidth, highHeight );
        boolean actual14 = obj.isIntersectedFromUpperOrBottomSideInSecondNode( centerPoint, bottomRightPoint, narrowWidth, highHeight );
        boolean actual15 = obj.isIntersectedFromUpperOrBottomSideInSecondNode( centerPoint, bottomLeftPoint, narrowWidth, highHeight );
        boolean actual16 = obj.isIntersectedFromUpperOrBottomSideInSecondNode( centerPoint, upperLeftPoint, narrowWidth, highHeight );

        assertThat( actual1, is( true ) );
        assertThat( actual2, is( false ) );
        assertThat( actual3, is( true ) );
        assertThat( actual4, is( false ) );
        assertThat( actual5, is( true ) );
        assertThat( actual6, is( true ) );
        assertThat( actual7, is( true ) );
        assertThat( actual8, is( true ) );

        assertThat( actual9, is( true ) );
        assertThat( actual10, is( false ) );
        assertThat( actual11, is( true ) );
        assertThat( actual12, is( false ) );
        assertThat( actual13, is( false ) );
        assertThat( actual14, is( false ) );
        assertThat( actual15, is( false ) );
        assertThat( actual16, is( false ) );
    }
}