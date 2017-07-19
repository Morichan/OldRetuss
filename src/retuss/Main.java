package retuss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start( Stage primaryStage ) throws Exception{
        String fxmlFileName = "retussMain.fxml";

        Parent root = FXMLLoader.load( getClass().getResource( fxmlFileName ) );
        primaryStage.setTitle( "RETUSS : Real-time Ensure Traceability between UML and Source-code System" );
        primaryStage.setScene( new Scene( root, 300, 275 ) );
        primaryStage.show();
    }


    public static void main( String[] args ) {
        launch( args );
    }
}
