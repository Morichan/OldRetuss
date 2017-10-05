package retuss;

import javafx.scene.control.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsSame.sameInstance;
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
    public void クラスの右クリックする際のコンテキストメニューを整形する() {
        String classType = "クラス";
        String className = "ClassName";

        ContextMenu expected = new ContextMenu();
        Menu attributionMenu = new Menu( "属性" );
        Menu operationMenu = new Menu( "操作" );

        MenuItem addAttributionMenuItem = new MenuItem( "追加" );
        MenuItem changeAttributionMenuItem = new MenuItem( "変更" );
        MenuItem deleteAttributionMenuItem = new MenuItem( "削除" );
        MenuItem displayAttributionMenuItem = new MenuItem( "表示選択" );
        MenuItem addOperationMenuItem = new MenuItem( "追加" );
        MenuItem changeOperationMenuItem = new MenuItem( "変更" );
        MenuItem deleteOperationMenuItem = new MenuItem( "削除" );
        MenuItem displayOperationMenuItem = new MenuItem( "表示選択" );

        attributionMenu.getItems().add( addAttributionMenuItem );
        attributionMenu.getItems().add( changeAttributionMenuItem );
        attributionMenu.getItems().add( deleteAttributionMenuItem );
        attributionMenu.getItems().add( displayAttributionMenuItem );
        operationMenu.getItems().add( addOperationMenuItem );
        operationMenu.getItems().add( changeOperationMenuItem );
        operationMenu.getItems().add( deleteOperationMenuItem );
        operationMenu.getItems().add( displayOperationMenuItem );

        expected.getItems().add( new MenuItem( className + "クラスをモデルから削除" ) );
        expected.getItems().add( new MenuItem( "名前の変更" ) );
        expected.getItems().add( new SeparatorMenuItem() );
        expected.getItems().add( attributionMenu );
        expected.getItems().add( operationMenu );

        ContextMenu actual = util.getClassContextMenuInCD( className, classType );

        assertThat( actual.getItems().size(), is( expected.getItems().size() ) );
        assertThat( actual.getItems().size(), is( 5 ) );
        for( int i = 0; i < actual.getItems().size(); i++ ) {
            assertThat( actual.getItems().get( i ).getText(), is( expected.getItems().get( i ).getText() ) );
        }

        assertThat( actual.getItems().get( 0 ), instanceOf( MenuItem.class) );
        assertThat( actual.getItems().get( 1 ), instanceOf( MenuItem.class) );
        assertThat( actual.getItems().get( 2 ), instanceOf( SeparatorMenuItem.class) );
        assertThat( actual.getItems().get( 3 ), instanceOf( Menu.class) );
        assertThat( actual.getItems().get( 4 ), instanceOf( Menu.class) );

        assertThat( ( ( Menu ) actual.getItems().get( 3 ) ).getItems().size(), is( attributionMenu.getItems().size() ) );
        for( int i = 0; i < ( ( Menu ) actual.getItems().get( 3 ) ).getItems().size(); i++ ) {
            assertThat( ( ( Menu ) actual.getItems().get( 3 ) ).getItems().get( i ).getText(), is( attributionMenu.getItems().get( i ).getText() ) );
            assertThat( ( ( Menu ) actual.getItems().get( 3 ) ).getItems().get( i ), instanceOf( MenuItem.class ) );
        }

        assertThat( ( ( Menu ) actual.getItems().get( 4 ) ).getItems().size(), is( operationMenu.getItems().size() ) );
        for( int i = 0; i < ( ( Menu ) actual.getItems().get( 4 ) ).getItems().size(); i++ ) {
            assertThat( ( ( Menu ) actual.getItems().get( 4 ) ).getItems().get( i ).getText(), is( operationMenu.getItems().get( i ).getText() ) );
            assertThat( ( ( Menu ) actual.getItems().get( 4 ) ).getItems().get( i ), instanceOf( MenuItem.class ) );
        }
    }
}