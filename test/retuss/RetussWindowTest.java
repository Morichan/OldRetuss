package retuss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mockit.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class RetussWindowTest {
    @Mocked
    private Application app;

    @Tested
    RetussWindow retussWindow = new RetussWindow();

    @Test
    public void main文を実行するとlaunchメソッドを1回実行する() {
        // 初期化
        new Expectations() {{
            String[] args = {};
            app.launch( args );
            times = 1;
        }};

        // 実行
        String[] args = {};

        // 検証
        retussWindow.main( args );
    }

    @Test
    public void メインステージにステージを追加する際はFXMLLoaderのloadを1回行う( @Mocked FXMLLoader fxmlLoader, @Mocked Stage stage, @Mocked Scene scene, @Mocked BorderPane borderPane ) throws IOException {
        String fileName = "FxmlFile.fxml";
        String title = "WindowTitle";
        new Expectations( fxmlLoader ) {{
            fxmlLoader.load( getClass().getResource( fileName ) );
            result = borderPane;
            times = 1;
        }};

        retussWindow.makeAdditionalStage( stage, fileName, title );
    }

    @Test
    public void mainStageとcodeStageを各1回ずつ描画するため合計2回showメソッドを実行する( @Mocked FXMLLoader fxmlLoader, @Mocked Stage mock, @Mocked Scene scene, @Mocked BorderPane borderPane ) throws IOException {
        new Expectations( mock ) {{
            mock.show();
            times = 2;
        }};

        retussWindow = new MockUp< RetussWindow >() {
            @Mock
            public Stage decorateStage( Stage stage, String fxmlFileName, String title ) {
                return mock;
            }
            @Mock
            public Stage makeAdditionalStage( Stage ownerStage, String fxmlFileName, String title ) {
                return mock;
            }
        }.getMockInstance();

        retussWindow.start( mock );
    }
}
