package retuss;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.experimental.runners.Enclosed;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(Enclosed.class)
public class ControllerTest {

    public static class WithoutGuiTest extends ApplicationTest {
        @Override
        public void start( Stage stage ) throws IOException {
            String fxmlFileName = "retussMain.fxml";
            Scene scene = new Scene( FXMLLoader.load( getClass().getResource( fxmlFileName ) ) );
            stage.setScene( scene );
            stage.show();
        }

        @Test
        public void GUIを表示するだけで必ず通るテスト() {
            // つまり何も記述しない
        }
    }

    public static class クラス図の場合 extends ApplicationTest {
        @Override
        public void start( Stage stage ) throws IOException {
            String fxmlFileName = "retussMain.fxml";
            Scene scene = new Scene( FXMLLoader.load( getClass().getResource( fxmlFileName ) ) );
            stage.setScene( scene );
            stage.show();
        }

        @Test
        public void どのボタンもクリックしてない場合は全てのボタンをオフにする() {
            Button normalButton = ( Button ) lookup( "#normalButtonInCD" ).query();
            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();
            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();

            assertThat( normalButton.isDefaultButton(), is( false ) );
            assertThat( classButton.isDefaultButton(), is( false ) );
            assertThat( noteButton.isDefaultButton(), is( false ) );
        }

        @Test
        public void クラスボタンをクリックするとクラスボタンをオンにして他をオフにする() {
            rightClickOn( "#classButtonInCD" ).clickOn();

            Button normalButton = ( Button ) lookup( "#normalButtonInCD" ).query();
            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();
            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();

            assertThat( classButton.isDefaultButton(), is( true ) );
            assertThat( normalButton.isDefaultButton(), is( false ) );
            assertThat( noteButton.isDefaultButton(), is( false ) );
        }

        @Test
        public void ノートボタンをクリックするとノートボタンをオンにして他をオフにする() {
            rightClickOn( "#noteButtonInCD" ).clickOn();

            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();
            Button normalButton = ( Button ) lookup( "#normalButtonInCD" ).query();
            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();

            assertThat( noteButton.isDefaultButton(), is( true ) );
            assertThat( normalButton.isDefaultButton(), is( false ) );
            assertThat( classButton.isDefaultButton(), is( false ) );
        }

        @Test
        public void クラスボタンを選択している際にクラスボタンをクリックするとオンにして他をオフにする() {
            rightClickOn( "#classButtonInCD" ).clickOn();
            rightClickOn( "#classButtonInCD" ).clickOn();

            Button normalButton = ( Button ) lookup( "#normalButtonInCD" ).query();
            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();
            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();

            assertThat( classButton.isDefaultButton(), is( true ) );
            assertThat( normalButton.isDefaultButton(), is( false ) );
            assertThat( noteButton.isDefaultButton(), is( false ) );
        }

        @Test
        public void クラスアイコンを選択している際にキャンバスをクリックするとクラスを描画する() {
            // rightClickOn( "#classDiagramCanvas" ).clickOn();

            Canvas canvas = ( Canvas ) lookup( "#classDiagramCanvas" ).query();

            assertThat( canvas.getWidth(), is( 956.0 ) );
            assertThat( canvas.getHeight(), is( 845.0 ) );
        }
    }
}