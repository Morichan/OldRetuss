package retuss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start( Stage mainStage ) throws Exception {
        Parent root = FXMLLoader.load( getClass().getResource( "..\\..\\resources\\retussMain.fxml" ) );
        // mainStage.setTitle( "hoge" );
        mainStage.setScene( new Scene( root ) );
        mainStage.show();
    }

    public String testMethod( String hoge ) {
        return hoge;
    }
}
