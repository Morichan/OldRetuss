package retuss;

import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import mockit.*;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith( Enclosed.class )
public class ClassDiagramDrawerTest {

    @RunWith( JMockit.class )
    public static class クラスアイコンを選択している場合 {
        @Mocked
        ClassNodeDiagram classNodeDiagram;

        @Tested
        ClassDiagramDrawer cdd;

        List< Button > buttons = new ArrayList<>();
        Button normalButton;
        Button classButton;
        Button noteButton;

        UtilityJavaFXComponent util;

        @Before
        public void setUp() {
            cdd = new ClassDiagramDrawer();
            normalButton = new Button( "Normal" );
            classButton = new Button( "Class" );
            noteButton = new Button( "Note" );
            buttons = Arrays.asList( normalButton, classButton, noteButton );

            util = new UtilityJavaFXComponent();

            buttons = util.setAllDefaultButtonIsFalseWithout( buttons, classButton );

            new Expectations( ClassNodeDiagram.class ) {{
                classNodeDiagram.draw();
            }};

            ClassNodeDiagram.resetNodeCount();
        }

        @Test
        public void キャンバスをクリックするとクラスを描画する() {
            cdd.setNodeText( "ClassName" );
            cdd.addDrawnNode( buttons );

            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
        }

        @Test
        public void キャンバスを2回クリックするとクラスを2回描画する() {
            cdd.setNodeText( "ClassName" );
            cdd.addDrawnNode( buttons );
            cdd.addDrawnNode( buttons );

            assertThat( cdd.getNodes().size(), is( 2 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 1 ).getNodeId(), is( 1 ) );
        }

        @Test
        public void 描画しているクラスを削除する() {
            cdd.setNodeText( "ClassName" );
            cdd.addDrawnNode( buttons );
            cdd.deleteDrawnNode( 0 );

            assertThat( cdd.getNodes().size(), is( 0 ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
        }

        @Test
        public void 描画しているクラス2つの内前半のクラスを削除する() {
            // クラスを描画する際の一連の動作
            cdd.setNodeText( "FirstClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            // クラスを描画する際の一連の動作
            cdd.setNodeText( "SecondClassName" );
            cdd.setMouseCoordinates( 500.0, 600.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 1 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            int id = cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.deleteDrawnNode( cdd.getCurrentNodeNumber() );

            assertThat( id, is( 0 ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "SecondClassName" ) );
        }

        @Test
        public void 描画しているクラス2つの内後半のクラスを削除する() {
            cdd.setNodeText( "FirstClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );
            cdd.setNodeText( "SecondClassName" );
            cdd.setMouseCoordinates( 500.0, 600.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 1 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            int id = cdd.getNodeDiagramId( 500.0, 600.0 );
            cdd.deleteDrawnNode( cdd.getCurrentNodeNumber() );

            assertThat( id, is( 1 ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 1 ) );
            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "FirstClassName" ) );
        }

        @Test
        public void 描画しているクラス2つの内後半のクラスを削除した後3つ目のクラスを描画する() {
            cdd.setNodeText( "FirstClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );
            cdd.setNodeText( "SecondClassName" );
            cdd.setMouseCoordinates( 500.0, 600.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 1 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            int id = cdd.getNodeDiagramId( 500.0, 600.0 );
            cdd.deleteDrawnNode( cdd.getCurrentNodeNumber() );

            cdd.setNodeText( "ThirdClassName" );
            cdd.setMouseCoordinates( 300.0, 400.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 1 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            assertThat( id, is( 1 ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 1 ) );
            assertThat( cdd.getNodes().size(), is( 2 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "FirstClassName" ) );
            assertThat( cdd.getNodes().get( 1 ).getNodeId(), is( 2 ) );
            assertThat( cdd.getNodes().get( 1 ).getNodeText(), is( "ThirdClassName" ) );
        }

        @Test
        public void 描画しているクラス3つの内2つ目のクラスを削除する() {
            cdd.setNodeText( "FirstClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );
            cdd.setNodeText( "SecondClassName" );
            cdd.setMouseCoordinates( 300.0, 400.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 1 ) ).calculateWidthAndHeight( 100.0, 80.0 );
            cdd.setNodeText( "ThirdClassName" );
            cdd.setMouseCoordinates( 500.0, 600.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 2 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            int id = cdd.getNodeDiagramId( 300.0, 400.0 );
            cdd.deleteDrawnNode( cdd.getCurrentNodeNumber() );

            assertThat( id, is( 1 ) );
            assertThat( cdd.getNodes().size(), is( 2 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "FirstClassName" ) );
            assertThat( cdd.getNodes().get( 1 ).getNodeId(), is( 2 ) );
            assertThat( cdd.getNodes().get( 1 ).getNodeText(), is( "ThirdClassName" ) );
        }

        @Test
        public void 描画しているクラス3つの内2つ目のクラスを削除した後4つ目のクラスを描画する() {
            cdd.setNodeText( "FirstClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );
            cdd.setNodeText( "SecondClassName" );
            cdd.setMouseCoordinates( 300.0, 400.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 1 ) ).calculateWidthAndHeight( 100.0, 80.0 );
            cdd.setNodeText( "ThirdClassName" );
            cdd.setMouseCoordinates( 500.0, 600.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 2 ) ).calculateWidthAndHeight( 100.0, 80.0 );
            int id = cdd.getNodeDiagramId( 300.0, 400.0 );
            cdd.deleteDrawnNode( cdd.getCurrentNodeNumber() );

            cdd.setNodeText( "FourthClassName" );
            cdd.setMouseCoordinates( 700.0, 800.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 2 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            assertThat( id, is( 1 ) );
            assertThat( cdd.getNodes().size(), is( 3 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 2 ).getNodeId(), is( 3 ) );
            assertThat( cdd.getNodes().get( 2 ).getNodeText(), is( "FourthClassName" ) );
        }

        @Test
        public void クラスを追加する際に空文字を入力した場合は追加しない() {
            cdd.setNodeText( "" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );

            new Expectations( ClassNodeDiagram.class ) {{
                classNodeDiagram.draw();
                times = 0;
            }};
            assertThat( cdd.getNodes().size(), is( 0 ) );
        }

        @Test
        public void 描画しているクラスのクラス名を変更する() {
            cdd.setNodeText( "ClassName" );
            cdd.addDrawnNode( buttons );
            cdd.changeDrawnNodeText( 0, ContentType.Title, 0, "ChangedClassName" );

            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "ChangedClassName" ) );
        }

        @Test
        public void 描画しているクラスのクラス名を変更する際に空文字を入力した場合は変更しない() {
            cdd.setNodeText( "NotChangedClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.changeDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Title, 0, "" );

            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "NotChangedClassName" ) );
        }

        @Test
        public void 描画しているクラス2つの内1つ目のクラス名を変更する() {
            cdd.setNodeText( "ClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );
            cdd.setNodeText( "NotChangingClassName" );
            cdd.setMouseCoordinates( 500.0, 600.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 1 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            int id = cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.changeDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Title, 0, "ChangedClassName" );

            assertThat( id, is( 0 ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
            assertThat( cdd.getNodes().size(), is( 2 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "ChangedClassName" ) );
            assertThat( cdd.getNodes().get( 1 ).getNodeId(), is( 1 ) );
            assertThat( cdd.getNodes().get( 1 ).getNodeText(), is( "NotChangingClassName" ) );
        }

        @Test
        public void 描画しているクラスに属性を追加する() {
            cdd.setNodeText( "ClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            int id = -1;
            List< String > attributions = Arrays.asList( "- content1 : int", "- content2 : double", "- content3 : char" );

            for( String attribution: attributions ) {
                id = cdd.getNodeDiagramId( 100.0, 200.0 );
                cdd.addDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Attribution, attribution );
            }

            assertThat( id, is( 0 ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "ClassName" ) );
            assertThat( cdd.getDrawnNodeTextList( 0, ContentType.Attribution ).size(), is( attributions.size() ) );
            for( int i = 0; i < attributions.size(); i++ ) {
                assertThat( cdd.getDrawnNodeTextList( 0, ContentType.Attribution ).get( i ), is( attributions.get( i ) ) );
            }
        }

        @Test
        public void 描画しているクラスに属性を追加する際に空文字を入力した場合は追加しない() {
            cdd.setNodeText( "ClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );
            String attribution = "";

            int id = cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.addDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Attribution, attribution );


            assertThat( id, is( 0 ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "ClassName" ) );
            assertThat( cdd.getDrawnNodeTextList( 0, ContentType.Attribution ).size(), is( 0 ) );
        }

        @Test
        public void 描画しているクラスから属性リストを取得する() {
            cdd.setNodeText( "ClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            int id = cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.addDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Attribution, "- content : int" );

            assertThat( id, is( 0 ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "ClassName" ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeContentText( ContentType.Attribution, 0 ), is( "- content : int" ) );
        }

        @Test
        public void 描画しているクラスに追加した属性を変更する() {
            cdd.setNodeText( "ClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.addDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Attribution, "- content : int" );
            int id = cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.changeDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Attribution, 0, "- content : double" );

            assertThat( id, is( 0 ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "ClassName" ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeContentText( ContentType.Attribution, 0 ), is( "- content : double" ) );
        }

        @Test
        public void 描画しているクラスに追加した属性を変更する際に空文字を入力した場合は変更しない() {
            cdd.setNodeText( "ClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.addDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Attribution, "- content : int" );
            int id = cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.changeDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Attribution, 0, "" );

            assertThat( id, is( 0 ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "ClassName" ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeContentText( ContentType.Attribution, 0 ), is( "- content : int" ) );
        }

        @Test
        public void 描画しているクラスに追加した属性を削除する() {
            cdd.setNodeText( "ClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.addDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Attribution, "- content : int" );
            int id = cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.deleteDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Attribution, 0 );

            assertThat( id, is( 0 ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "ClassName" ) );
            assertThat( cdd.getDrawnNodeTextList( 0, ContentType.Attribution ).size(), is( 0 ) );
        }

        @Test
        public void 描画しているクラスに追加した属性を非表示にする() {
            cdd.setNodeText( "ClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.addDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Attribution, "- content1 : int" );
            cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.addDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Attribution, "- content2 : double" );
            int id = cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.setDrawnNodeContentBoolean( cdd.getCurrentNodeNumber(), ContentType.Attribution, ContentType.Indication, 0, false );

            assertThat( id, is( 0 ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "ClassName" ) );
            assertThat( cdd.getDrawnNodeContentsBooleanList( 0, ContentType.Attribution, ContentType.Indication).size(), is( 2 ) );
            assertThat( cdd.getDrawnNodeContentsBooleanList( 0, ContentType.Attribution, ContentType.Indication).get( 0 ), is( false ) );
            assertThat( cdd.getDrawnNodeContentsBooleanList( 0, ContentType.Attribution, ContentType.Indication).get( 1 ), is( true ) );
        }

        @Test
        public void 描画しているクラスに操作を追加する() {
            cdd.setNodeText( "ClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            int id = -1;
            List< String > operations = Arrays.asList( "- content1 : int", "- content2 : double", "- content3 : char" );

            for( String operation : operations ) {
                id = cdd.getNodeDiagramId( 100.0, 200.0 );
                cdd.addDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Operation, operation );
            }

            assertThat( id, is( 0 ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "ClassName" ) );
            assertThat( cdd.getDrawnNodeTextList( 0, ContentType.Operation ).size(), is( operations.size() ) );
            for( int i = 0; i < operations.size(); i++ ) {
                assertThat( cdd.getDrawnNodeTextList( 0, ContentType.Operation ).get( i ), is( operations.get( i ) ) );
            }
        }

        @Test
        public void 描画しているクラスに操作を追加する際に空文字を入力した場合は追加しない() {
            cdd.setNodeText( "ClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );
            String attribution = "";

            int id = cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.addDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Operation, attribution );


            assertThat( id, is( 0 ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "ClassName" ) );
            assertThat( cdd.getDrawnNodeTextList( 0, ContentType.Operation ).size(), is( 0 ) );
        }

        @Test
        public void 描画しているクラスに追加した操作を変更する() {
            cdd.setNodeText( "ClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.addDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Operation, "+ content() : int" );
            int id = cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.changeDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Operation, 0, "+ content() : double" );

            assertThat( id, is( 0 ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "ClassName" ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeContentText( ContentType.Operation, 0 ), is( "+ content() : double" ) );
        }

        @Test
        public void 描画しているクラスに追加した操作を変更する際に空文字を入力した場合は変更しない() {
            cdd.setNodeText( "ClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.addDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Operation, "+ content() : int" );
            int id = cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.changeDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Operation, 0, "" );

            assertThat( id, is( 0 ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "ClassName" ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeContentText( ContentType.Operation, 0 ), is( "+ content() : int" ) );
        }

        @Test
        public void 描画しているクラスに追加した操作を削除する() {
            cdd.setNodeText( "ClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.addDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Operation, "- content() : int" );
            int id = cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.deleteDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Operation, 0 );

            assertThat( id, is( 0 ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "ClassName" ) );
            assertThat( cdd.getDrawnNodeTextList( 0, ContentType.Operation ).size(), is( 0 ) );
        }

        @Test
        public void 描画しているクラスに追加した操作を非表示にする() {
            cdd.setNodeText( "ClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );

            cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.addDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Operation, "- content1() : int" );
            cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.addDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Operation, "- content2() : double" );
            int id = cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.setDrawnNodeContentBoolean( cdd.getCurrentNodeNumber(), ContentType.Operation, ContentType.Indication, 0, false );

            assertThat( id, is( 0 ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "ClassName" ) );
            assertThat( cdd.getDrawnNodeContentsBooleanList( 0, ContentType.Operation, ContentType.Indication).size(), is( 2 ) );
            assertThat( cdd.getDrawnNodeContentsBooleanList( 0, ContentType.Operation, ContentType.Indication).get( 0 ), is( false ) );
            assertThat( cdd.getDrawnNodeContentsBooleanList( 0, ContentType.Operation, ContentType.Indication).get( 1 ), is( true ) );
        }

        @Test
        public void 描画しているクラスの幅と高さを返す() {
            double expectedWidth = 100.0;
            double expectedHeight = 80.0;
            cdd.setNodeText( "ClassName" );
            cdd.setMouseCoordinates( 100.0, 200.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( expectedWidth, expectedHeight );

            double actualWidth = cdd.getNodeWidth( 0 );
            double actualHeight = cdd.getNodeHeight( 0 );

            assertThat( actualWidth, is( expectedWidth ) );
            assertThat( actualHeight, is( expectedHeight ) );
        }
    }

    @RunWith( JMockit.class )
    public static class ノートアイコンを選択している場合 {
        @Mocked
        NoteNodeDiagram noteNodeDiagram;

        @Tested
        ClassDiagramDrawer cdd;

        List< Button > buttons = new ArrayList<>();
        Button normalButton;
        Button classButton;
        Button noteButton;

        UtilityJavaFXComponent util;

        @Before
        public void setUp() {
            cdd = new ClassDiagramDrawer();
            normalButton = new Button( "Normal" );
            classButton = new Button( "Class" );
            noteButton = new Button( "Note" );
            buttons = Arrays.asList( normalButton, classButton, noteButton );

            util = new UtilityJavaFXComponent();

            buttons = util.setAllDefaultButtonIsFalseWithout( buttons, noteButton );
        }

        @Test
        public void キャンバスをクリックするとノートを描画する() {
            new Expectations() {{
                noteNodeDiagram.draw();
                times = 1;
            }};

            cdd.setNodeText( "Note" );
            cdd.addDrawnNode( buttons );

            assertThat( cdd.getNodes().size(), is( 1 ) );
        }
    }

    @RunWith( JMockit.class )
    public static class ノーマルアイコンを選択している場合 {
        @Mocked
        NoteNodeDiagram noteNodeDiagram;
        @Mocked
        ClassNodeDiagram classNodeDiagram;

        @Tested
        ClassDiagramDrawer cdd;

        List< Button > buttons = new ArrayList<>();
        Button normalButton;
        Button classButton;
        Button noteButton;

        UtilityJavaFXComponent util;

        @Before
        public void setUp() {
            cdd = new ClassDiagramDrawer();
            normalButton = new Button( "Normal" );
            classButton = new Button( "Class" );
            noteButton = new Button( "Note" );
            buttons = Arrays.asList( normalButton, classButton, noteButton );

            util = new UtilityJavaFXComponent();

            buttons = util.setAllDefaultButtonIsFalseWithout( buttons, normalButton );
        }

        @Test
        public void キャンバスをクリックしても何も描画しない() {
            new Expectations() {{
                classNodeDiagram.draw();
                times = 0;
            }};
            new Expectations() {{
                noteNodeDiagram.draw();
                times = 0;
            }};

            cdd.setNodeText( "Item" );
            cdd.addDrawnNode( buttons );

            assertThat( cdd.getNodes().size(), is( 0 ) );
        }
    }

    @RunWith( JMockit.class )
    public static class コンポジションアイコンを選択している場合 {
        @Mocked
        NoteNodeDiagram noteNodeDiagram;
        @Mocked
        ClassNodeDiagram classNodeDiagram;

        @Tested
        ClassDiagramDrawer cdd;

        List< Button > buttons = new ArrayList<>();
        Button normalButton;
        Button classButton;
        Button noteButton;
        Button compositionButton;

        UtilityJavaFXComponent util;

        @Before
        public void setUp() {
            cdd = new ClassDiagramDrawer();
            normalButton = new Button( "Normal" );
            classButton = new Button( "Class" );
            noteButton = new Button( "Note" );
            compositionButton = new Button( "Composition" );
            buttons = Arrays.asList( normalButton, classButton, noteButton, compositionButton );

            util = new UtilityJavaFXComponent();

            buttons = util.setAllDefaultButtonIsFalseWithout( buttons, compositionButton );
        }

        @Test
        public void キャンバスをクリックしても何も描画しない() {
            new Expectations() {{
                classNodeDiagram.draw();
                times = 0;
            }};
            new Expectations() {{
                noteNodeDiagram.draw();
                times = 0;
            }};

            cdd.setNodeText( "Item" );
            cdd.addDrawnNode( buttons );

            assertThat( cdd.getNodes().size(), is( 0 ) );
        }
    }

    @RunWith( JMockit.class )
    public static class クラスを3つ記述後にコンポジションアイコンを選択している場合 {
        @Mocked
        NoteNodeDiagram noteNodeDiagram;
        @Mocked
        ClassNodeDiagram classNodeDiagram;

        @Tested
        ClassDiagramDrawer cdd;

        List< Button > buttons = new ArrayList<>();
        Button normalButton;
        Button classButton;
        Button noteButton;
        Button compositionButton;
        Point2D firstClass;
        Point2D secondClass;
        Point2D thirdClass;
        Point2D betweenFirstAndSecondClass;
        Point2D betweenFirstAndThirdClass;

        UtilityJavaFXComponent util;

        @Before
        public void setUp() {
            cdd = new ClassDiagramDrawer();
            normalButton = new Button( "Normal" );
            classButton = new Button( "Class" );
            noteButton = new Button( "Note" );
            compositionButton = new Button( "Composition" );
            buttons = Arrays.asList( normalButton, classButton, noteButton, compositionButton );
            firstClass = new Point2D(100.0, 200.0);
            secondClass = new Point2D( 500.0, 600.0 );
            thirdClass = new Point2D( 100.0, 600.0 );
            betweenFirstAndSecondClass = new Point2D( 300.0, 400.0 );
            betweenFirstAndThirdClass = new Point2D( 100.0, 400.0 );

            util = new UtilityJavaFXComponent();
            buttons = util.setAllDefaultButtonIsFalseWithout( buttons, classButton );

            new Expectations( ClassNodeDiagram.class ) {{
                classNodeDiagram.draw();
            }};

            ClassNodeDiagram.resetNodeCount();

            cdd.setNodeText( "FirstClassName" );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0, 80.0 );
            cdd.setNodeText( "SecondClassName" );
            cdd.setMouseCoordinates( secondClass.getX(), secondClass.getY() );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 1 ) ).calculateWidthAndHeight( 100.0, 80.0 );
            cdd.setNodeText( "ThirdClassName" );
            cdd.setMouseCoordinates( thirdClass.getX(), thirdClass.getY() );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 2 ) ).calculateWidthAndHeight( 100.0, 80.0 );
            buttons = util.setAllDefaultButtonIsFalseWithout( buttons, compositionButton );
        }

        @Test
        public void キャンバスに描画しているクラスの1つ目をクリックするとコンポジションの描画を待機し正規ノードを待機していなかったとする() {
            boolean expected = false;

            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );

            assertThat( actual, is( expected ) );
            assertThat( cdd.getNowStateType(), is( ContentType.Composition ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
        }

        @Test
        public void キャンバスに描画しているクラスの1つ目をクリックした後に2つ目をクリックするとコンポジションの描画待機を解除し正規ノードを待機していたとする() {
            boolean expected = true;

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );

            assertThat( actual, is( expected ) );
            assertThat( cdd.getNowStateType(), is( ContentType.Undefined ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
        }

        @Test
        public void キャンバスに描画しているクラスの1つ目をクリックした後にクラスを描画していない箇所をクリックするとコンポジションの描画待機を解除し正規ノードを待機していなかったとする() {
            boolean expected = false;

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX() + 100.0, secondClass.getY() + 100.0 );

            assertThat( actual, is( expected ) );
            assertThat( cdd.getNowStateType(), is( ContentType.Undefined ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
        }

        @Test
        public void キャンバスに描画しているクラスの1つ目をクリックした後に1つ目をクリックするとコンポジションの描画待機を解除し正規ノードを待機していたとする() {
            boolean expected = true;

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );

            assertThat( actual, is( expected ) );
            assertThat( cdd.getNowStateType(), is( ContentType.Undefined ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
        }

        @Test
        public void キャンバスに描画しているクラスの1つ目をコンポジションとしてクリックした後に2つ目を汎化としてクリックすると汎化の描画を待機し正規ノードを待機していたとする() {
            boolean expected = true;

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, secondClass.getX(), secondClass.getY() );

            assertThat( actual, is( expected ) );
            assertThat( cdd.getNowStateType(), is( ContentType.Generalization ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 1 ) );
        }

        @Test
        public void キャンバスに描画しているクラスの1つ目をコンポジションとしてクリックして2つ目を汎化としてクリックした後に3回目と4回目をコンポジションとしてクリックするとコンポジションの描画待機を解除し正規ノードを待機していたとする() {
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, secondClass.getX(), secondClass.getY() );

            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            assertThat( actual, is( true ) );
            assertThat( cdd.getNowStateType(), is( ContentType.Composition ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );

            actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            assertThat( actual, is( true ) );
            assertThat( cdd.getNowStateType(), is( ContentType.Undefined ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
        }

        @Test
        public void キャンバスに描画しているクラスの1つ目をコンポジションとしてクリックした後に2回目と3回目を汎化としてクリックすると汎化の描画を待機し正規ノードを待機していたとする() {
            boolean expected = true;

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            boolean actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, firstClass.getX(), firstClass.getY() );
            assertThat( actual, is( expected ) );
            assertThat( cdd.getNowStateType(), is( ContentType.Generalization ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );

            actual = cdd.hasWaitedCorrectDrawnDiagram( ContentType.Generalization, secondClass.getX(), secondClass.getY() );
            assertThat( actual, is( expected ) );
            assertThat( cdd.getNowStateType(), is( ContentType.Undefined ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
        }

        @Test
        public void キャンバスに描画しているクラスを2つクリックするとコンポジション関係を描画する() {
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition", secondClass.getX(), secondClass.getY() );

            assertThat( cdd.getCurrentNodeNumber(), is( 1 ) );
            assertThat( cdd.getEdgeDiagram().getEdgeContentText( ContentType.Composition, 0 ), is( "- composition" ) );
            assertThat( cdd.getEdgeDiagram().getRelationId( ContentType.Composition, 0 ), is( 1 ) );
            assertThat( cdd.getEdgeDiagram().getRelationSourceId( ContentType.Composition, 0 ), is( 0 ) );
        }

        @Test
        public void キャンバスに関係を描画している2つのクラス間をクリックすると真を返すがそれ以外の箇所では偽を返す() {
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition", secondClass.getX(), secondClass.getY() );

            boolean actualTrue = cdd.isAlreadyDrawnAnyDiagram( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() );
            boolean actualFalse = cdd.isAlreadyDrawnAnyDiagram( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() + 100.0 );

            assertThat( actualTrue, is( true ) );
            assertThat( actualFalse, is( false ) );
        }

        @Test
        public void キャンバスに描画している一番上の関係の内容を返す() {
            RelationshipAttribution expected = new RelationshipAttribution( "- composition" );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition", secondClass.getX(), secondClass.getY() );
            cdd.searchDrawnAnyDiagramType( secondClass.getX(), secondClass.getY() );

            RelationshipAttribution actual = cdd.searchDrawnEdge( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() );

            assertThat( actual.getName(), is( expected.getName() ) );
        }

        @Test
        public void キャンバスに描画している一番上の関係の内容を変更する() {
            RelationshipAttribution expected = new RelationshipAttribution( "- changedComposition" );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition", secondClass.getX(), secondClass.getY() );
            cdd.searchDrawnAnyDiagramType( secondClass.getX(), secondClass.getY() );

            cdd.changeDrawnEdge( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY(), expected.getName() );
            RelationshipAttribution actual = cdd.searchDrawnEdge( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() );

            assertThat( actual.getName(), is( expected.getName() ) );
        }

        @Test
        public void キャンバスに描画している一番上の関係の内容を削除する() {
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition", secondClass.getX(), secondClass.getY() );
            cdd.searchDrawnAnyDiagramType( secondClass.getX(), secondClass.getY() );

            cdd.deleteDrawnEdge( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() );
            RelationshipAttribution actual = cdd.searchDrawnEdge( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() );

            assertThat( actual, is( nullValue() ) );
        }

        @Test
        public void キャンバスに描画しているクラス2つのコンポジション関係を複数描画する() {
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition1", secondClass.getX(), secondClass.getY() );

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition2", secondClass.getX(), secondClass.getY() );

            assertThat( cdd.getEdgeDiagram().getEdgeContentText( ContentType.Composition, 0 ), is( "- composition1" ) );
            assertThat( cdd.getEdgeDiagram().getRelationId( ContentType.Composition, 0 ), is( 1 ) );
            assertThat( cdd.getEdgeDiagram().getRelationSourceId( ContentType.Composition, 0 ), is( 0 ) );
            assertThat( cdd.getEdgeDiagram().getEdgeContentText( ContentType.Composition, 1 ), is( "- composition2" ) );
            assertThat( cdd.getEdgeDiagram().getRelationId( ContentType.Composition, 1 ), is( 1 ) );
            assertThat( cdd.getEdgeDiagram().getRelationSourceId( ContentType.Composition, 1 ), is( 0 ) );
        }

        @Test
        public void キャンバスに描画しているクラス2つのコンポジション関係を描画する途中で一度描画していない箇所を選択してから描画する() {
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() );
            cdd.setMouseCoordinates( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() );

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition", secondClass.getX(), secondClass.getY() );

            assertThat( cdd.getEdgeDiagram().getEdgeContentText( ContentType.Composition, 0 ), is( "- composition" ) );
            assertThat( cdd.getEdgeDiagram().getRelationId( ContentType.Composition, 0 ), is( 1 ) );
            assertThat( cdd.getEdgeDiagram().getRelationSourceId( ContentType.Composition, 0 ), is( 0 ) );
        }

        @Test
        public void キャンバスに描画しているクラス3つのコンポジション関係を2つ描画する() {
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, secondClass.getX(), secondClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition1", secondClass.getX(), secondClass.getY() );

            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, firstClass.getX(), firstClass.getY() );
            cdd.setMouseCoordinates( firstClass.getX(), firstClass.getY() );
            cdd.hasWaitedCorrectDrawnDiagram( ContentType.Composition, thirdClass.getX(), thirdClass.getY() );
            cdd.addDrawnEdge( buttons, "- composition2", thirdClass.getX(), thirdClass.getY() );

            assertThat( cdd.getEdgeDiagram().getEdgeContentText( ContentType.Composition, 0 ), is( "- composition1" ) );
            assertThat( cdd.getEdgeDiagram().getRelationId( ContentType.Composition, 0 ), is( 1 ) );
            assertThat( cdd.getEdgeDiagram().getRelationSourceId( ContentType.Composition, 0 ), is( 0 ) );
            assertThat( cdd.getEdgeDiagram().getEdgeContentText( ContentType.Composition, 1 ), is( "- composition2" ) );
            assertThat( cdd.getEdgeDiagram().getRelationId( ContentType.Composition, 1 ), is( 2 ) );
            assertThat( cdd.getEdgeDiagram().getRelationSourceId( ContentType.Composition, 1 ), is( 0 ) );
            assertThat( cdd.searchDrawnEdge( betweenFirstAndSecondClass.getX(), betweenFirstAndSecondClass.getY() ).getName(), is( "- composition1" ) );
            assertThat( cdd.searchDrawnEdge( betweenFirstAndThirdClass.getX(), betweenFirstAndThirdClass.getY() ).getName(), is( "- composition2" ) );
        }
    }
}