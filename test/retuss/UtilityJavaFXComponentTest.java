package retuss;

import javafx.scene.control.Button;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
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
}