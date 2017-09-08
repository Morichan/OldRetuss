package retuss;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
        Stage stage;
        Point2D xButtonOnDialogBox;
        Point2D okButtonOnDialogBox;

        @Override
        public void start( Stage stage ) throws IOException {
            String fxmlFileName = "retussMain.fxml";
            Scene scene = new Scene( FXMLLoader.load( getClass().getResource( fxmlFileName ) ) );
            stage.setScene( scene );
            stage.show();
            this.stage = stage;
        }

        @Before
        public void setup() {
            xButtonOnDialogBox = new Point2D( 1050.0, 350.0 );
            okButtonOnDialogBox = new Point2D( 950.0, 500.0 );
        }

        @Test
        public void どのボタンもクリックしていない場合はどれも選択していない() {
            Button normalButton = ( Button ) lookup( "#normalButtonInCD" ).query();
            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();
            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();

            assertThat( normalButton.isDefaultButton(), is( false ) );
            assertThat( classButton.isDefaultButton(), is( false ) );
            assertThat( noteButton.isDefaultButton(), is( false ) );
        }

        @Test
        public void クラスボタンをクリックするとクラスボタンをオンにして他をオフにする() {
            clickOn( "#classButtonInCD" );

            Button normalButton = ( Button ) lookup( "#normalButtonInCD" ).query();
            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();
            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();

            assertThat( classButton.isDefaultButton(), is( true ) );
            assertThat( normalButton.isDefaultButton(), is( false ) );
            assertThat( noteButton.isDefaultButton(), is( false ) );
        }

        @Test
        public void ノートボタンをクリックするとノートボタンをオンにして他をオフにする() {
            clickOn( "#noteButtonInCD" );

            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();
            Button normalButton = ( Button ) lookup( "#normalButtonInCD" ).query();
            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();

            assertThat( noteButton.isDefaultButton(), is( true ) );
            assertThat( normalButton.isDefaultButton(), is( false ) );
            assertThat( classButton.isDefaultButton(), is( false ) );
        }

        @Test
        public void クラスボタンを選択している際にクラスボタンをクリックするとオンにして他をオフにする() {
            clickOn( "#classButtonInCD" );
            clickOn( "#classButtonInCD" );

            Button normalButton = ( Button ) lookup( "#normalButtonInCD" ).query();
            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();
            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();

            assertThat( classButton.isDefaultButton(), is( true ) );
            assertThat( normalButton.isDefaultButton(), is( false ) );
            assertThat( noteButton.isDefaultButton(), is( false ) );
        }

        @Test
        public void クラスアイコンを選択している際にキャンバスをクリックするとクラス名の入力ウィンドウを表示する() {
            clickOn( "#classButtonInCD" );
            clickOn( "#classDiagramCanvas" );

            clickOn( xButtonOnDialogBox );
        }

        @Test
        public void クラスアイコンを選択している際にキャンバスをクリックしクラス名を入力すると入力したクラス名のクラスを表示する() {
            clickOn( "#classButtonInCD" );
            clickOn( "#classDiagramCanvas" );

            write( "ClassName" );

            clickOn( okButtonOnDialogBox );
        }

        @Test
        public void クラスアイコンを選択している際にキャンバスをクリックしクラス名を入力しない場合は何も表示しない() {
            clickOn( "#classButtonInCD" );
            clickOn( "#classDiagramCanvas" );

            clickOn( okButtonOnDialogBox );
        }

        @Test
        public void クラスアイコンを選択している際に何も描画されていないキャンバスを右クリックした場合は何も表示しない() {
            clickOn( "#classButtonInCD" );
            rightClickOn( "#classDiagramCanvas" );

            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();

            assertThat( scrollPane.getContextMenu(), nullValue() );
        }

        @Test
        public void クラスアイコンを選択している際にキャンバスに描かれているClassNameクラスを右クリックした場合ClassNameクラスをモデルから削除メニューを表示する() {
            Point2D classDiagramCanvas = new Point2D( 900.0, 600.0 );

            clickOn( "#classButtonInCD" );
            clickOn( classDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );

            rightClickOn( classDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();

            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText(), is( "ClassNameクラスをモデルから削除" ) );
        }


        /**
         * クラス図キャンバス直下に存在するスクロールパネルを取得する。
         *
         * 具体的には、ステージ上のボーダーパネル上のアンカーパネル上のスプリットパネル上の2つ目のアンカーパネル上の
         * タブパネル上のアンカーパネル上のVボックス上の2つ目のアンカーパネル上のスクロールパネルを取得する。
         *
         * @return クラス図キャンバス直下のスクロールパネル
         */
        public ScrollPane getScrollPaneBelowClassDiagramCanvas() {
            BorderPane borderPaneOnStage = (BorderPane) stage.getScene().getRoot().getChildrenUnmodifiable().get( 0 );
            AnchorPane anchorPaneOnBorderPane = (AnchorPane) borderPaneOnStage.getCenter();
            SplitPane splitPaneOnAnchorPaneOnBorderPane = (SplitPane) anchorPaneOnBorderPane.getChildren().get( 0 );
            AnchorPane anchorPaneOnSplitPane = (AnchorPane) splitPaneOnAnchorPaneOnBorderPane.getItems().get( 1 );
            TabPane tabPaneOnAnchorPaneOnSplitPane = (TabPane) anchorPaneOnSplitPane.getChildren().get( 0 );
            AnchorPane anchorPaneOnTabPane = (AnchorPane) tabPaneOnAnchorPaneOnSplitPane.getTabs().get( 0 ).getContent();
            VBox vBoxOnAnchorPaneOnTabPane = (VBox) anchorPaneOnTabPane.getChildren().get( 0 );
            AnchorPane anchorPaneOnVBox = (AnchorPane) vBoxOnAnchorPaneOnTabPane.getChildren().get( 1 );
            return (ScrollPane) anchorPaneOnVBox.getChildren().get( 0 );
        }

        /**
         * クラス図キャンバスを取得する。
         *
         * getScrollPaneBelowClassDiagramCanvasに依存する。
         *
         * @return クラス図キャンバス
         */
        public Canvas getCanvasOnScrollPane() {
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            AnchorPane anchorPane = (AnchorPane) scrollPane.getContent();
            return (Canvas) anchorPane.getChildren().get( 0 );
        }
    }
}