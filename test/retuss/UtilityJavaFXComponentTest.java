package retuss;

import javafx.geometry.Point2D;
import javafx.scene.control.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

public class UtilityJavaFXComponentTest {
    UtilityJavaFXComponent util;

    @Before
    public void setUp() {
        util = new UtilityJavaFXComponent();
    }

    @Test
    public void ボタンリストに1つ存在するボタンをtrueにする() {
        Button normalButton = new Button();
        List< Button > buttons = new ArrayList<>();
        buttons.add( normalButton );

        buttons = util.setAllDefaultButtonIsFalseWithout( buttons, normalButton );

        assertThat( buttons.get( 0 ).isDefaultButton(), is( true ) );
    }

    @Test
    public void ボタンリストに2つ存在するボタンの内1つ目をtrueにする() {
        Button normalButton = new Button();
        Button classButton = new Button();
        List< Button > buttons;
        buttons = Arrays.asList( normalButton, classButton );

        buttons = util.setAllDefaultButtonIsFalseWithout( buttons, normalButton );

        assertThat( buttons.get( 0 ).isDefaultButton(), is( true ) );
        assertThat( buttons.get( 1 ).isDefaultButton(), is( false ) );
    }

    @Test
    public void ボタンリストに3つ存在するボタンの内2つ目をtrueにする() {
        Button normalButton = new Button();
        Button classButton = new Button();
        Button noteButton = new Button();
        List< Button > buttons;
        buttons = Arrays.asList( normalButton, classButton, noteButton );

        buttons = util.setAllDefaultButtonIsFalseWithout( buttons, classButton );

        assertThat( buttons.get( 0 ).isDefaultButton(), is( false ) );
        assertThat( buttons.get( 1 ).isDefaultButton(), is( true ) );
        assertThat( buttons.get( 2 ).isDefaultButton(), is( false ) );
    }

    @Test
    public void ボタンリストに3つ存在するボタンの内3つ目をtrueにする() {
        Button normalButton = new Button();
        Button classButton = new Button();
        Button noteButton = new Button();
        List< Button > buttons;
        buttons = Arrays.asList( normalButton, classButton, noteButton );

        buttons = util.setAllDefaultButtonIsFalseWithout( buttons, noteButton );

        assertThat( buttons.get( 0 ).isDefaultButton(), is( false ) );
        assertThat( buttons.get( 1 ).isDefaultButton(), is( false ) );
        assertThat( buttons.get( 2 ).isDefaultButton(), is( true ) );
    }

    @Test
    public void ボタンリストに2つ存在するボタンの内1つ目をtrueにした後2つ目をtrueにする() {
        Button normalButton = new Button();
        Button classButton = new Button();
        List< Button > buttons;
        buttons = Arrays.asList( normalButton, classButton );

        buttons = util.setAllDefaultButtonIsFalseWithout( buttons, normalButton );
        buttons = util.setAllDefaultButtonIsFalseWithout( buttons, classButton );

        assertThat( buttons.get( 0 ).isDefaultButton(), is( false ) );
        assertThat( buttons.get( 1 ).isDefaultButton(), is( true ) );
    }

    @Test
    public void ボタンリストに2つ存在するボタンの内trueになっている1つ目のボタンを返す() {
        Button normalButton = new Button();
        Button classButton = new Button();
        List< Button > buttons;
        buttons = Arrays.asList( normalButton, classButton );

        buttons = util.setAllDefaultButtonIsFalseWithout( buttons, normalButton );
        Button clickedButton = util.getDefaultButtonIn( buttons );

        assertThat( clickedButton, is( normalButton ) );
    }

    @Test
    public void ボタンリストに2つ存在するボタンの内trueになっている2つ目のボタンを返す() {
        Button normalButton = new Button();
        Button classButton = new Button();
        List< Button > buttons;
        buttons = Arrays.asList( normalButton, classButton );

        buttons = util.setAllDefaultButtonIsFalseWithout( buttons, classButton );
        Button clickedButton = util.getDefaultButtonIn( buttons );

        assertThat( clickedButton, is( classButton ) );
    }

    @Test
    public void クラスの右クリックする際に表示するコンテキストメニューを整形する() {
        String className = "ClassName";

        ContextMenu expected = new ContextMenu();
        Menu attributionMenu = new Menu( "属性" );
        Menu operationMenu = new Menu( "操作" );

        MenuItem addAttributionMenuItem = new MenuItem( "追加" );
        Menu changeAttributionMenu = new Menu( "変更" );
        Menu deleteAttributionMenu = new Menu( "削除" );
        Menu displayAttributionMenu = new Menu( "表示選択" );
        MenuItem addOperationMenuItem = new MenuItem( "追加" );
        Menu changeOperationMenu = new Menu( "変更" );
        Menu deleteOperationMenu = new Menu( "削除" );
        Menu displayOperationMenu = new Menu( "表示選択" );

        List< String > attributions = Arrays.asList( "- content1 : int", "- content2 : double", "- content3 : char" );
        List< String > operations = Arrays.asList( "- content1() : int", "- content2( argv : int ) : double", "- content3( argv1 : int, argv2 : double ) : char" );
        List< Boolean > attributionsVisibility = Arrays.asList( true, true, true );
        List< Boolean > operationsVisibility = Arrays.asList( true, true, true );

        for( String attribution: attributions ) {
            changeAttributionMenu.getItems().add( new MenuItem( attribution ) );
            deleteAttributionMenu.getItems().add( new MenuItem( attribution ) );
            displayAttributionMenu.getItems().add( new CheckMenuItem( attribution ) );
        }
        for( String operation: operations ) {
            changeOperationMenu.getItems().add( new MenuItem( operation ) );
            deleteOperationMenu.getItems().add( new MenuItem( operation ) );
            displayOperationMenu.getItems().add( new CheckMenuItem( operation ) );
        }

        attributionMenu.getItems().add( addAttributionMenuItem );
        attributionMenu.getItems().add( changeAttributionMenu );
        attributionMenu.getItems().add( deleteAttributionMenu );
        attributionMenu.getItems().add( displayAttributionMenu );
        operationMenu.getItems().add( addOperationMenuItem );
        operationMenu.getItems().add( changeOperationMenu );
        operationMenu.getItems().add( deleteOperationMenu );
        operationMenu.getItems().add( displayOperationMenu );

        expected.getItems().add( new MenuItem( className + "クラスの名前の変更" ) );
        expected.getItems().add( new MenuItem( className + "クラスをモデルから削除" ) );
        expected.getItems().add( new SeparatorMenuItem() );
        expected.getItems().add( attributionMenu );
        expected.getItems().add( operationMenu );

        ContextMenu actual = util.getClassContextMenuInCD( className, ContentType.Class, attributions, operations, attributionsVisibility, operationsVisibility );

        assertThat( actual.getItems().size(), is( expected.getItems().size() ) );
        assertThat( actual.getItems().size(), is( 5 ) );
        for( int i = 0; i < actual.getItems().size(); i++ ) {
            assertThat( actual.getItems().get( i ).getText(), is( expected.getItems().get( i ).getText() ) );
        }

        assertThat( actual.getItems().get( 0 ), instanceOf( expected.getItems().get( 0 ).getClass() ) );
        assertThat( actual.getItems().get( 1 ), instanceOf( expected.getItems().get( 1 ).getClass() ) );
        assertThat( actual.getItems().get( 2 ), instanceOf( expected.getItems().get( 2 ).getClass() ) );
        assertThat( actual.getItems().get( 3 ), instanceOf( expected.getItems().get( 3 ).getClass() ) );
        assertThat( actual.getItems().get( 4 ), instanceOf( expected.getItems().get( 4 ).getClass() ) );

        assertThat( ( ( Menu ) actual.getItems().get( 3 ) ).getItems().size(), is( ( ( Menu ) expected.getItems().get( 3 ) ).getItems().size() ) );
        for( int i = 0; i < ( ( Menu ) actual.getItems().get( 3 ) ).getItems().size(); i++ ) {
            assertThat( ( ( Menu ) actual.getItems().get( 3 ) ).getItems().get( i ).getText(), is( ( ( Menu ) expected.getItems().get( 3 ) ).getItems().get( i ).getText() ) );
            assertThat( ( ( Menu ) actual.getItems().get( 3 ) ).getItems().get( i ), instanceOf( ( ( Menu ) expected.getItems().get( 3 ) ).getItems().get( i ).getClass() ) );

            if( i == 0 ) continue;
            assertThat( ( ( Menu ) ( ( Menu ) actual.getItems().get( 3 ) ).getItems().get( i ) ).getItems().size(), is( ( ( Menu ) ( ( Menu ) expected.getItems().get( 3 ) ).getItems().get( i ) ).getItems().size() ) );
            for( int j = 0; j < ( ( Menu ) ( ( Menu ) actual.getItems().get( 3 ) ).getItems().get( i ) ).getItems().size(); j++ ) {
                assertThat( ( ( Menu ) ( ( Menu ) actual.getItems().get( 3 ) ).getItems().get( i ) ).getItems().get( j ).getText(), is( ( ( Menu ) ( ( Menu ) expected.getItems().get( 3 ) ).getItems().get( i ) ).getItems().get( j ).getText() ) );
                assertThat( ( ( Menu ) ( ( Menu ) actual.getItems().get( 3 ) ).getItems().get( i ) ).getItems().get( j ), instanceOf( ( ( Menu ) ( ( Menu ) expected.getItems().get( 3 ) ).getItems().get( i ) ).getItems().get( j ).getClass() ) );
            }

            if( i != 3 ) continue;
            for( int j = 0; j < ( ( Menu ) ( ( Menu ) actual.getItems().get( 3 ) ).getItems().get( i ) ).getItems().size(); j++ ) {
                assertThat( ( ( CheckMenuItem ) ( ( Menu ) ( ( Menu ) actual.getItems().get( 3 ) ).getItems().get( i ) ).getItems().get( j ) ).isSelected(), is( true ) );
            }
        }

        assertThat( ( ( Menu ) actual.getItems().get( 4 ) ).getItems().size(), is( ( ( Menu ) expected.getItems().get( 4 ) ).getItems().size() ) );
        for( int i = 0; i < ( ( Menu ) actual.getItems().get( 4 ) ).getItems().size(); i++ ) {
            assertThat( ( ( Menu ) actual.getItems().get( 4 ) ).getItems().get( i ).getText(), is( ( ( Menu ) expected.getItems().get( 4 ) ).getItems().get( i ).getText() ) );
            assertThat( ( ( Menu ) actual.getItems().get( 4 ) ).getItems().get( i ), instanceOf( ( ( Menu ) expected.getItems().get( 4 ) ).getItems().get( i ).getClass() ) );

            if( i == 0 ) continue;
            assertThat( ( ( Menu ) ( ( Menu ) actual.getItems().get( 4 ) ).getItems().get( i ) ).getItems().size(), is( ( ( Menu ) ( ( Menu ) expected.getItems().get( 3 ) ).getItems().get( i ) ).getItems().size() ) );
            for( int j = 0; j < ( ( Menu ) ( ( Menu ) actual.getItems().get( 4 ) ).getItems().get( i ) ).getItems().size(); j++ ) {
                assertThat( ( ( Menu ) ( ( Menu ) actual.getItems().get( 4 ) ).getItems().get( i ) ).getItems().get( j ).getText(), is( ( ( Menu ) ( ( Menu ) expected.getItems().get( 4 ) ).getItems().get( i ) ).getItems().get( j ).getText() ) );
                assertThat( ( ( Menu ) ( ( Menu ) actual.getItems().get( 4 ) ).getItems().get( i ) ).getItems().get( j ), instanceOf( ( ( Menu ) ( ( Menu ) expected.getItems().get( 4 ) ).getItems().get( i ) ).getItems().get( j ).getClass() ) );
            }

            if( i != 3 ) continue;
            for( int j = 0; j < ( ( Menu ) ( ( Menu ) actual.getItems().get( 3 ) ).getItems().get( i ) ).getItems().size(); j++ ) {
                assertThat( ( ( CheckMenuItem ) ( ( Menu ) ( ( Menu ) actual.getItems().get( 4 ) ).getItems().get( i ) ).getItems().get( j ) ).isSelected(), is( true ) );
            }
        }
    }

    @Test
    public void クラスの右クリックする際に表示するコンテキストメニューにおいて属性や操作が空の場合はなしと表示し操作もできない() {
        String className = "ClassName";

        ContextMenu expected = new ContextMenu();
        Menu attributionMenu = new Menu( "属性" );
        Menu operationMenu = new Menu( "操作" );

        MenuItem addAttributionMenuItem = new MenuItem( "追加" );
        Menu changeAttributionMenu = new Menu( "変更" );
        Menu deleteAttributionMenu = new Menu( "削除" );
        Menu displayAttributionMenu = new Menu( "表示選択" );
        MenuItem addOperationMenuItem = new MenuItem( "追加" );
        Menu changeOperationMenu = new Menu( "変更" );
        Menu deleteOperationMenu = new Menu( "削除" );
        Menu displayOperationMenu = new Menu( "表示選択" );

        String expectedMenuText = "なし";
        List< String > attributions = new ArrayList<>();
        List< String > operations = new ArrayList<>();
        List< Boolean > attributionsVisibility = new ArrayList<>();
        List< Boolean > operationsVisibility = new ArrayList<>();

        attributionMenu.getItems().add( addAttributionMenuItem );
        attributionMenu.getItems().add( changeAttributionMenu );
        attributionMenu.getItems().add( deleteAttributionMenu );
        attributionMenu.getItems().add( displayAttributionMenu );
        operationMenu.getItems().add( addOperationMenuItem );
        operationMenu.getItems().add( changeOperationMenu );
        operationMenu.getItems().add( deleteOperationMenu );
        operationMenu.getItems().add( displayOperationMenu );

        expected.getItems().add( new MenuItem( className + "クラスをモデルから削除" ) );
        expected.getItems().add( new MenuItem( className + "クラスの名前の変更" ) );
        expected.getItems().add( new SeparatorMenuItem() );
        expected.getItems().add( attributionMenu );
        expected.getItems().add( operationMenu );

        ContextMenu actual = util.getClassContextMenuInCD( className, ContentType.Class, attributions, operations, attributionsVisibility, operationsVisibility );

        assertThat( actual.getItems().size(), is( expected.getItems().size() ) );
        assertThat( actual.getItems().size(), is( 5 ) );

        assertThat( ( ( Menu ) actual.getItems().get( 3 ) ).getItems().size(), is( ( ( Menu ) expected.getItems().get( 3 ) ).getItems().size() ) );
        for( int i = 1; i < ( ( Menu ) actual.getItems().get( 3 ) ).getItems().size(); i++ ) {
            assertThat( ( ( Menu ) ( ( Menu ) actual.getItems().get( 3 ) ).getItems().get( i ) ).getItems().size(), is( 1 ) );
            assertThat( ( ( Menu ) ( ( Menu ) actual.getItems().get( 3 ) ).getItems().get( i ) ).getItems().get( 0 ).getText(), is( expectedMenuText ) );
            assertThat( ( ( Menu ) ( ( Menu ) actual.getItems().get( 3 ) ).getItems().get( i ) ).getItems().get( 0 ).isDisable(), is( true ) );
        }
        assertThat( ( ( Menu ) actual.getItems().get( 4 ) ).getItems().size(), is( ( ( Menu ) expected.getItems().get( 4 ) ).getItems().size() ) );
        for( int i = 1; i < ( ( Menu ) actual.getItems().get( 4 ) ).getItems().size(); i++ ) {
            assertThat( ( ( Menu ) ( ( Menu ) actual.getItems().get( 4 ) ).getItems().get( i ) ).getItems().size(), is( 1 ) );
            assertThat( ( ( Menu ) ( ( Menu ) actual.getItems().get( 4 ) ).getItems().get( i ) ).getItems().get( 0 ).getText(), is( expectedMenuText ) );
            assertThat( ( ( Menu ) ( ( Menu ) actual.getItems().get( 4 ) ).getItems().get( i ) ).getItems().get( 0 ).isDisable(), is( true ) );
        }
    }

    @Test
    public void コンポジション関係の右クリックする際に表示するコンテキストメニューを整形する() {
        String composition = "- composition";

        ContextMenu expected = new ContextMenu();

        expected.getItems().add( new MenuItem( composition + " の変更" ) );
        expected.getItems().add( new MenuItem( composition + " の削除" ) );

        ContextMenu actual = util.getClassContextMenuInCD( composition, ContentType.Composition );

        assertThat( actual.getItems().size(), is( expected.getItems().size() ) );
        assertThat( actual.getItems().size(), is( 2 ) );
        for( int i = 0; i < actual.getItems().size(); i++ ) {
            assertThat( actual.getItems().get( i ).getText(), is( expected.getItems().get( i ).getText() ) );
        }
    }

    @Test
    public void 旧版_角が複数存在する多角形の内側に点が存在するか否かを確認する() {
        List< Point2D > quadrangle1 = Arrays.asList(
                new Point2D( 100.0, 100.0 ), new Point2D( 100.0, 500.0 ),
                new Point2D( 200.0, 500.0 ), new Point2D( 200.0, 100.0 ) );
        Point2D insidePoint1 = new Point2D( 150.0, 300.0 );
        Point2D outsidePoint1 = new Point2D( 250.0, 300.0 );
        Point2D onLinePoint1 = new Point2D( 100.0, 300.0 );
        Point2D onEdgePoint1 = new Point2D( 100.0, 100.0 );
        List< Point2D > quadrangle2 = Arrays.asList(
                new Point2D( 200.0, 100.0 ), new Point2D( 400.0, 300.0 ),
                new Point2D( 300.0, 400.0 ), new Point2D( 100.0, 200.0 ) );
        Point2D insidePoint2 = new Point2D( 200.0, 200.0 );
        Point2D outsidePoint2 = new Point2D( 120.0, 120.0 );
        Point2D onLinePoint2 = new Point2D( 200.0, 300.0 );
        Point2D onEdgePoint2 = new Point2D( 400.0, 300.0 );

        boolean actual1 = util.isInsidePointFromPolygonUsingJavaAwtPolygonMethod( quadrangle1, insidePoint1 );
        boolean actual2 = util.isInsidePointFromPolygonUsingJavaAwtPolygonMethod( quadrangle1, outsidePoint1 );
        boolean actual3 = util.isInsidePointFromPolygonUsingJavaAwtPolygonMethod( quadrangle1, onLinePoint1 );
        boolean actual4 = util.isInsidePointFromPolygonUsingJavaAwtPolygonMethod( quadrangle1, onEdgePoint1 );
        boolean actual5 = util.isInsidePointFromPolygonUsingJavaAwtPolygonMethod( quadrangle2, insidePoint2 );
        boolean actual6 = util.isInsidePointFromPolygonUsingJavaAwtPolygonMethod( quadrangle2, outsidePoint2 );
        boolean actual7 = util.isInsidePointFromPolygonUsingJavaAwtPolygonMethod( quadrangle2, onLinePoint2 );
        boolean actual8 = util.isInsidePointFromPolygonUsingJavaAwtPolygonMethod( quadrangle2, onEdgePoint2 );

        assertThat( actual1, is( true ) );
        assertThat( actual2, is( false ) );
        assertThat( actual3, is( true ) );
        assertThat( actual4, is( true ) );
        assertThat( actual5, is( true ) );
        assertThat( actual6, is( false ) );
        assertThat( actual7, is( true ) );
        assertThat( actual8, is( true ) );
    }

    @Test
    public void 角が複数存在する多角形の内側に点が存在するか否かを確認する() {
        List< Point2D > quadrangle1 = Arrays.asList(
                new Point2D( 100.0, 100.0 ), new Point2D( 100.0, 500.0 ),
                new Point2D( 200.0, 500.0 ), new Point2D( 200.0, 100.0 ) );
        Point2D insidePoint1 = new Point2D( 150.0, 300.0 );
        Point2D outsidePoint1 = new Point2D( 250.0, 300.0 );
        Point2D onLinePoint1 = new Point2D( 100.0, 300.0 );
        Point2D onEdgePoint1 = new Point2D( 100.0, 100.0 );
        List< Point2D > quadrangle2 = Arrays.asList(
                new Point2D( 200.0, 100.0 ), new Point2D( 400.0, 300.0 ),
                new Point2D( 300.0, 400.0 ), new Point2D( 100.0, 200.0 ) );
        Point2D insidePoint2 = new Point2D( 200.0, 200.0 );
        Point2D outsidePoint2 = new Point2D( 120.0, 120.0 );
        Point2D onLinePoint2 = new Point2D( 200.0, 300.0 );
        Point2D onEdgePoint2 = new Point2D( 400.0, 300.0 );
        List< Point2D > polygonWithSelfCross = Arrays.asList(
                new Point2D( 200.0, 100.0 ), new Point2D( 300.0, 100.0 ),
                new Point2D( 300.0, 400.0 ), new Point2D( 400.0, 400.0 ),
                new Point2D( 400.0, 300.0 ), new Point2D( 100.0, 300.0 ),
                new Point2D( 100.0, 200.0 ), new Point2D( 500.0, 200.0 ),
                new Point2D( 200.0, 100.0 ), new Point2D( 300.0, 100.0 ),
                new Point2D( 500.0, 500.0 ), new Point2D( 200.0, 500.0 ) );
        Point2D holePoint = new Point2D( 350.0, 350.0 );
        Point2D selfCrossPoint = new Point2D( 250.0, 250.0 );

        boolean actual1 = util.isInsidePointFromPolygonUsingWNA( quadrangle1, insidePoint1 );
        boolean actual2 = util.isInsidePointFromPolygonUsingWNA( quadrangle1, outsidePoint1 );
        boolean actual3 = util.isInsidePointFromPolygonUsingWNA( quadrangle1, onLinePoint1 );
        boolean actual4 = util.isInsidePointFromPolygonUsingWNA( quadrangle1, onEdgePoint1 );
        boolean actual5 = util.isInsidePointFromPolygonUsingWNA( quadrangle2, insidePoint2 );
        boolean actual6 = util.isInsidePointFromPolygonUsingWNA( quadrangle2, outsidePoint2 );
        boolean actual7 = util.isInsidePointFromPolygonUsingWNA( quadrangle2, onLinePoint2 );
        boolean actual8 = util.isInsidePointFromPolygonUsingWNA( quadrangle2, onEdgePoint2 );
        boolean actual9 = util.isInsidePointFromPolygonUsingWNA( polygonWithSelfCross, holePoint );
        boolean actual10 = util.isInsidePointFromPolygonUsingWNA( polygonWithSelfCross, selfCrossPoint );

        assertThat( actual1, is( true ) );
        assertThat( actual2, is( false ) );
        assertThat( actual3, is( true ) );
        assertThat( actual4, is( true ) );
        assertThat( actual5, is( true ) );
        assertThat( actual6, is( false ) );
        assertThat( actual7, is( true ) );
        assertThat( actual8, is( true ) );
        assertThat( actual9, is( false ) );
        assertThat( actual10, is( true ) );
    }

    @Test
    public void ノードの中央のポイントと幅と高さを入力するとその頂点のリストを返す() {
        List< Point2D > expected = Arrays.asList(
                new Point2D( 150.0, 60.0 ), new Point2D( 150.0, 140.0 ),
                new Point2D( 50.0, 140.0 ), new Point2D( 50.0, 60.0 )
        );
        Point2D center = new Point2D( 100.0, 100.0 );
        double width = 100.0;
        double height = 80.0;

        List< Point2D > actual = util.calculateTopListFromNode( center, width, height );

        assertThat( actual, is( expected ) );
    }

    @Test
    public void 線分の始点と終点を2つ入力すると線分の交点が存在するか否かを確認する() {
        Point2D line1S = new Point2D( 100.0, 100.0 );
        Point2D line1E = new Point2D( 200.0, 200.0 );
        Point2D line2S = new Point2D( 200.0, 100.0 );
        Point2D line2E = new Point2D( 100.0, 200.0 );
        Point2D line3S = new Point2D( 300.0, 100.0 );
        Point2D line3E = new Point2D( 300.0, 300.0 );
        Point2D line4S = new Point2D( 300.0, 100.0 );
        Point2D line4E = new Point2D( 100.0, 300.0 );
        Point2D line5S = new Point2D( 200.0, 100.0 );
        Point2D line5E = new Point2D( 200.0, 200.0 );
        Point2D line6S = new Point2D( 200.0, 200.0 );
        Point2D line6E = new Point2D( 300.0, 300.0 );
        Point2D line7S = new Point2D( 200.0, 100.0 );
        Point2D line7E = new Point2D( 300.0, 200.0 );
        Point2D line8S = new Point2D( 100.0, 0.0 );
        Point2D line8E = new Point2D( 0.0, 100.0 );

        boolean actual1And2 = util.isIntersected( line1S, line1E, line2S, line2E );
        boolean actual1And3 = util.isIntersected( line1S, line1E, line3S, line3E );
        boolean actual1And4 = util.isIntersected( line1S, line1E, line4S, line4E );
        boolean actual1And5 = util.isIntersected( line1S, line1E, line5S, line5E );
        boolean actual1And6 = util.isIntersected( line1S, line1E, line6S, line6E );
        boolean actual1And7 = util.isIntersected( line1S, line1E, line7S, line7E );
        boolean actual1And8 = util.isIntersected( line1S, line1E, line8S, line8E );

        assertThat( actual1And2, is( true ) );
        assertThat( actual1And3, is( false ) );
        assertThat( actual1And4, is( true ) );
        assertThat( actual1And5, is( true ) );
        assertThat( actual1And6, is( true ) );
        assertThat( actual1And7, is( false ) );
        assertThat( actual1And8, is( false ) );
    }

    @Test
    public void 線分の始点と終点を2つ入力すると交点が存在する場合は線分の交点を返し存在しない場合はnullを返す() {
        Point2D line1S = new Point2D( 100.0, 100.0 );
        Point2D line1E = new Point2D( 200.0, 200.0 );
        Point2D line2S = new Point2D( 200.0, 100.0 );
        Point2D line2E = new Point2D( 100.0, 200.0 );
        Point2D line3S = new Point2D( 300.0, 100.0 );
        Point2D line3E = new Point2D( 300.0, 300.0 );
        Point2D line4S = new Point2D( 300.0, 100.0 );
        Point2D line4E = new Point2D( 100.0, 300.0 );
        Point2D line5S = new Point2D( 200.0, 100.0 );
        Point2D line5E = new Point2D( 200.0, 200.0 );
        Point2D line6S = new Point2D( 200.0, 200.0 );
        Point2D line6E = new Point2D( 300.0, 300.0 );
        Point2D line7S = new Point2D( 200.0, 100.0 );
        Point2D line7E = new Point2D( 300.0, 200.0 );
        Point2D line8S = new Point2D( 100.0, 0.0 );
        Point2D line8E = new Point2D( 0.0, 100.0 );
        Point2D expected1And2 = new Point2D( 150.0, 150.0 );
        Point2D expected1And3 = null;
        Point2D expected1And4 = new Point2D( 200.0, 200.0 );
        Point2D expected1And5 = new Point2D( 200.0, 200.0 );
        Point2D expected1And6 = new Point2D( 200.0, 200.0 );
        Point2D expected1And7 = null;
        Point2D expected1And8 = null;

        Point2D actual1And2 = util.calculateCrossPoint( line1S, line1E, line2S, line2E );
        Point2D actual1And3 = util.calculateCrossPoint( line1S, line1E, line3S, line3E );
        Point2D actual1And4 = util.calculateCrossPoint( line1S, line1E, line4S, line4E );
        Point2D actual1And5 = util.calculateCrossPoint( line1S, line1E, line5S, line5E );
        Point2D actual1And6 = util.calculateCrossPoint( line1S, line1E, line6S, line6E );
        Point2D actual1And7 = util.calculateCrossPoint( line1S, line1E, line7S, line7E );
        Point2D actual1And8 = util.calculateCrossPoint( line1S, line1E, line8S, line8E );

        assertThat( actual1And2, is( expected1And2 ) );
        assertThat( actual1And3, is( expected1And3 ) );
        assertThat( actual1And4, is( expected1And4 ) );
        assertThat( actual1And5, is( expected1And5 ) );
        assertThat( actual1And6, is( expected1And6 ) );
        assertThat( actual1And7, is( expected1And7 ) );
        assertThat( actual1And8, is( expected1And8 ) );
    }
}