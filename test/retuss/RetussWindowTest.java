package retuss;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mockit.*;

import javafx.application.Application;
import mockit.integration.junit4.JMockit;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(JMockit.class)
public class RetussWindowTest {
    @Mocked
    private Application app;

    @Tested
    private RetussWindow retussWindow = new RetussWindow();

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
    public void FXMLLoaderのloadを1回行う( @Mocked FXMLLoader fxmlLoader, @Mocked Stage stage, @Mocked Scene scene, @Mocked BorderPane borderPane ) throws IOException {
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
    public void mainStageとcodeStageを2回描画する( @Mocked FXMLLoader fxmlLoader, @Mocked Stage mock, @Mocked Scene scene, @Mocked BorderPane borderPane ) throws IOException {
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