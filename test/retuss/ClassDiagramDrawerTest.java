package retuss;

import javafx.scene.control.Button;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

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
        }

        @Test
        public void キャンバスをクリックするとクラスを描画する() {
            new Expectations() {{
                classNodeDiagram.draw();
                times = 1;
            }};

            buttons = util.setAllDefaultButtonIsFalseWithout( buttons, classButton );

            cdd.drawNode( buttons );
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
        }

        @Test
        public void キャンバスをクリックするとノートを描画する() {
            new Expectations() {{
                noteNodeDiagram.draw();
                times = 1;
            }};

            buttons = util.setAllDefaultButtonIsFalseWithout( buttons, noteButton );

            cdd.drawNode( buttons );
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

            buttons = util.setAllDefaultButtonIsFalseWithout( buttons, normalButton );

            cdd.drawNode( buttons );
        }
    }
}