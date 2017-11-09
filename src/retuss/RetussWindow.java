package retuss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RetussWindow extends Application {

    /**
     * RETUSSを開始する。
     *
     * @param mainStage 初期ステージ：Applicationクラス参照
     * @throws IOException decorateStageメソッド参照
     */
    @Override
    public void start( Stage mainStage ) throws IOException {
        String mainFxmlFileName = "retussMain.fxml";
        String mainTitle = "RETUSS : Real-time Ensure Traceability between UML and Source-code System";
        String codeFxmlFileName = "retussCode.fxml";
        String codeTitle = "new project";

        mainStage = decorateStage( mainStage, mainFxmlFileName, mainTitle );
        Stage codeStage = makeAdditionalStage( mainStage, codeFxmlFileName, codeTitle );

        mainStage.show();
        codeStage.show();
    }

    /**
     * 追加ウィンドウのステージを作る。
     *
     * @param ownerStage 土台となるステージ：土台ステージを消すと追加ステージも消える。
     * @param fxmlFileName 入力FXMLファイル名：{@code null}不可
     * @param title ウィンドウタイトル
     * @return 追加するステージ
     * @throws IOException decorateStageメソッド参照
     */
    public Stage makeAdditionalStage( Stage ownerStage, String fxmlFileName, String title ) throws IOException {
        Stage addStage = new Stage();
        addStage.initOwner( ownerStage );
        addStage.setScene( new Scene( new BorderPane() ) );

        addStage = decorateStage( addStage, fxmlFileName, title );

        return addStage;
    }

    /**
     * 表示するステージを装飾する。
     *
     * @param stage 表示するステージ：特にメインウィンドウを表示する際は{@code null}不可
     * @param fxmlFileName 入力FXMLファイル名：{@code null}不可
     * @param title ウィンドウタイトル
     * @return 装飾されたステージ
     * @throws IOException FXMLファイルの入力エラーの場合
     */
    private Stage decorateStage( Stage stage, String fxmlFileName, String title ) throws IOException {
        String resourcesPath = "";
        Parent root = FXMLLoader.load( getClass().getResource( resourcesPath + fxmlFileName ) );
        stage.setTitle( title );
        stage.setScene( new Scene( root ) );

        return stage;
    }


    /**
     * メインメソッド
     * ここから全てが始まる。
     *
     * @param args コマンドライン引数
     */
    public static void main( String[] args ) {
        launch( args );
    }
}
