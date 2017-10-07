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

        ContextMenu actual = util.getClassContextMenuInCD( className, ContentType.Class, attributions, operations );

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

        ContextMenu actual = util.getClassContextMenuInCD( className, ContentType.Class, attributions, operations );

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
}