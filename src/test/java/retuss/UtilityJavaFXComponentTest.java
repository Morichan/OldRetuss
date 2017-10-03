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
}