package retuss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start( Stage primaryStage ) throws IOException {
        String mainFxmlFileName = "retussMain.fxml";
        String mainTitle = "RETUSS : Real-time Ensure Traceability between UML and Source-code System";
        String codeFxmlFileName = "retussCode.fxml";
        String codeTitle = "new project";

        Stage mainStage = makeStage( primaryStage, mainFxmlFileName, mainTitle );
        Stage codeStage = addOpenWindow( mainStage, codeFxmlFileName, codeTitle );

        mainStage.show();
        codeStage.show();
    }

    public Stage addOpenWindow( Stage ownerStage, String fxmlFileName, String title ) throws IOException {
        Stage addStage = new Stage();
        addStage.initOwner( ownerStage );
        addStage.setScene( new Scene( new BorderPane() ) );

        addStage = makeStage( addStage, fxmlFileName, title );

        return addStage;
    }

    public Stage makeStage( Stage stage, String fxmlFileName, String title ) throws IOException {
        Parent root = FXMLLoader.load( getClass().getResource( fxmlFileName ) );
        stage.setTitle( title );
        stage.setScene( new Scene( root ) );

        return stage;
    }


    public static void main( String[] args ) {
        launch( args );
    }
}
