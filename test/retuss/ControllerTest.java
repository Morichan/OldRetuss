package retuss;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

    public static class クラス図の場合 extends ApplicationTest {
        @Override
        public void start( Stage stage ) throws IOException {
            String fxmlFileName = "retussMain.fxml";
            Scene scene = new Scene( FXMLLoader.load( getClass().getResource( fxmlFileName ) ) );
            stage.setScene( scene );
            stage.show();
        }

        @Test
        public void クラスボタンをクリックするとクラスボタンをオンにして他をオフにする() {
            rightClickOn( "#classButtonInCD" ).clickOn();

            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();
            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();

            assertThat( classButton.isDefaultButton(), is( true ) );
            assertThat( noteButton.isDefaultButton(), is( false ) );
        }

        @Test
        public void ノートボタンをクリックするとノートボタンをオンにして他をオフにする() {
            rightClickOn( "#noteButtonInCD" ).clickOn();

            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();
            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();

            assertThat( noteButton.isDefaultButton(), is( true ) );
            assertThat( classButton.isDefaultButton(), is( false ) );

        }

        @Test
        public void クラスボタンを選択している際にクラスボタンをクリックするとオンにして他をオフにする() {
            rightClickOn( "#classButtonInCD" ).clickOn();
            rightClickOn( "#classButtonInCD" ).clickOn();

            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();
            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();

            assertThat( classButton.isDefaultButton(), is( true ) );
            assertThat( noteButton.isDefaultButton(), is( false ) );
        }
    }
}