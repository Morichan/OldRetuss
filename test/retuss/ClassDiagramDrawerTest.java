package retuss;

import javafx.scene.control.Button;
import javafx.scene.text.Text;
import mockit.*;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
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

            classNodeDiagram.resetNodeCount();
        }

        @Test
        public void キャンバスをクリックするとクラスを描画する() {
            cdd.addDrawnNode( buttons );

            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
        }

        @Test
        public void キャンバスを2回クリックするとクラスを2回描画する() {
            cdd.addDrawnNode( buttons );
            cdd.addDrawnNode( buttons );

            assertThat( cdd.getNodes().size(), is( 2 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 1 ).getNodeId(), is( 1 ) );
        }

        @Test
        public void 描画しているクラスを削除する() {
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
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0 );

            // クラスを描画する際の一連の動作
            cdd.setNodeText( "SecondClassName" );
            cdd.setMouseCoordinates( 500.0, 600.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 1 ) ).calculateWidthAndHeight( 100.0 );

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
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0 );
            cdd.setNodeText( "SecondClassName" );
            cdd.setMouseCoordinates( 500.0, 600.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 1 ) ).calculateWidthAndHeight( 100.0 );

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
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0 );
            cdd.setNodeText( "SecondClassName" );
            cdd.setMouseCoordinates( 500.0, 600.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 1 ) ).calculateWidthAndHeight( 100.0 );

            int id = cdd.getNodeDiagramId( 500.0, 600.0 );
            cdd.deleteDrawnNode( cdd.getCurrentNodeNumber() );

            cdd.setNodeText( "ThirdClassName" );
            cdd.setMouseCoordinates( 300.0, 400.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 1 ) ).calculateWidthAndHeight( 100.0 );

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
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0 );
            cdd.setNodeText( "SecondClassName" );
            cdd.setMouseCoordinates( 300.0, 400.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 1 ) ).calculateWidthAndHeight( 100.0 );
            cdd.setNodeText( "ThirdClassName" );
            cdd.setMouseCoordinates( 500.0, 600.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 2 ) ).calculateWidthAndHeight( 100.0 );

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
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0 );
            cdd.setNodeText( "SecondClassName" );
            cdd.setMouseCoordinates( 300.0, 400.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 1 ) ).calculateWidthAndHeight( 100.0 );
            cdd.setNodeText( "ThirdClassName" );
            cdd.setMouseCoordinates( 500.0, 600.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 2 ) ).calculateWidthAndHeight( 100.0 );
            int id = cdd.getNodeDiagramId( 300.0, 400.0 );
            cdd.deleteDrawnNode( cdd.getCurrentNodeNumber() );

            cdd.setNodeText( "FourthClassName" );
            cdd.setMouseCoordinates( 700.0, 800.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 2 ) ).calculateWidthAndHeight( 100.0 );

            assertThat( id, is( 1 ) );
            assertThat( cdd.getNodes().size(), is( 3 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 2 ).getNodeId(), is( 3 ) );
            assertThat( cdd.getNodes().get( 2 ).getNodeText(), is( "FourthClassName" ) );
        }

        @Test
        public void 描画しているクラスのクラス名を変更する() {
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
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0 );

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
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0 );
            cdd.setNodeText( "NotChangingClassName" );
            cdd.setMouseCoordinates( 500.0, 600.0 );
            cdd.addDrawnNode( buttons );
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 1 ) ).calculateWidthAndHeight( 100.0 );

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
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0 );

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
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0 );
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
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0 );

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
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0 );

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
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0 );

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
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0 );

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
            ( ( ClassNodeDiagram ) cdd.getNodes().get( 0 ) ).calculateWidthAndHeight( 100.0 );

            cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.addDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Attribution, "- content1 : int" );
            cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.addDrawnNodeText( cdd.getCurrentNodeNumber(), ContentType.Attribution, "- content2 : double" );
            int id = cdd.getNodeDiagramId( 100.0, 200.0 );
            cdd.setDrawnNodeContentBoolean( cdd.getCurrentNodeNumber(), ContentType.Visibility, 0, false );

            assertThat( id, is( 0 ) );
            assertThat( cdd.getCurrentNodeNumber(), is( 0 ) );
            assertThat( cdd.getNodes().size(), is( 1 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeId(), is( 0 ) );
            assertThat( cdd.getNodes().get( 0 ).getNodeText(), is( "ClassName" ) );
            assertThat( cdd.getDrawnNodeContentsBooleanList( 0, ContentType.Attribution ).size(), is( 2 ) );
            assertThat( cdd.getDrawnNodeContentsBooleanList( 0, ContentType.Attribution ).get( 0 ), is( false ) );
            assertThat( cdd.getDrawnNodeContentsBooleanList( 0, ContentType.Attribution ).get( 1 ), is( true ) );
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

            cdd.addDrawnNode( buttons );

            assertThat( cdd.getNodes().size(), is( 0 ) );
        }
    }
}