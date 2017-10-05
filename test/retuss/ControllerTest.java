package retuss;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.experimental.runners.Enclosed;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.service.query.PointQuery;

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
        Point2D secondClickedClassDiagramCanvas;

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
            secondClickedClassDiagramCanvas = new Point2D( 1050.0, 300.0);
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
        public void クラスアイコンを選択していない際にキャンバスをクリックしても何も表示しない( @Mocked TextInputDialog mock ) {
            new Expectations( TextInputDialog.class ) {{
                mock.showAndWait();
                times = 0;
            }};

            clickOn( "#classDiagramCanvas" );
        }

        @Test
        public void クラスアイコンを選択している際にキャンバスをクリックするとクラス名の入力ウィンドウを表示する( @Mocked TextInputDialog mock ) {
            new Expectations( TextInputDialog.class ) {{
                mock.showAndWait();
                times = 1;
            }};

            clickOn( "#classButtonInCD" );
            clickOn( "#classDiagramCanvas" );
        }

        @Test
        public void クラスアイコンを選択している際にキャンバスをクリックしクラス名を入力すると入力したクラス名のクラスを表示する() {
            clickOn( "#classButtonInCD" );
            clickOn( "#classDiagramCanvas" );

            write( "ClassName" );
            clickOn( okButtonOnDialogBox );
            GraphicsContext gc = getGraphicsContext();
            Paint fillColor = gc.getFill();
            Paint strokeColor = gc.getStroke();
            TextAlignment textAlignment = gc.getTextAlign();

            assertThat( fillColor, is( Color.BLACK ) );
            assertThat( strokeColor, is( Color.BLACK ) );
            assertThat( textAlignment, is( TextAlignment.CENTER ) );
        }

        @Test
        public void クラスアイコンを選択している際に2つのクラスを表示する() {
            clickOn( "#classButtonInCD" );
            clickOn( "#classDiagramCanvas" );

            write( "FirstClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( secondClickedClassDiagramCanvas );
            write( "SecondClassName" );
            clickOn( okButtonOnDialogBox );
            GraphicsContext gc = getGraphicsContext();
            Paint fillColor = gc.getFill();
            Paint strokeColor = gc.getStroke();
            TextAlignment textAlignment = gc.getTextAlign();

            assertThat( fillColor, is( Color.BLACK ) );
            assertThat( strokeColor, is( Color.BLACK ) );
            assertThat( textAlignment, is( TextAlignment.CENTER ) );
        }

        @Test
        public void クラスアイコンを選択している際にキャンバスをクリックしクラス名を入力しない場合は何も表示しない() {
            clickOn( "#classButtonInCD" );
            clickOn( "#classDiagramCanvas" );

            clickOn( okButtonOnDialogBox );
            GraphicsContext gc = getGraphicsContext();
            Paint fillColor = gc.getFill();
            Paint strokeColor = gc.getStroke();
            TextAlignment textAlignment = gc.getTextAlign();

            assertThat( fillColor, is( Color.BLACK ) );
            assertThat( strokeColor, is( Color.BLACK ) );
            assertThat( textAlignment, is( TextAlignment.LEFT ) );
        }

        @Test
        public void クラスアイコンを選択している際に何も描画されていないキャンバスを右クリックした場合は何も表示しない() {
            clickOn( "#classButtonInCD" );
            rightClickOn( "#classDiagramCanvas" );

            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();

            assertThat( scrollPane.getContextMenu(), is( nullValue() ) );
        }

        @Test
        public void クラスアイコンを選択している際にキャンバスに描かれているClassNameクラスを右クリックした場合は何も表示しない() {
            Point2D clickedClassDiagramCanvas = new Point2D( 900.0, 600.0 );

            clickOn( "#classButtonInCD" );
            clickOn( clickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );

            rightClickOn( clickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();

            assertThat( scrollPane.getContextMenu(), is( nullValue() ) );
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれているClassNameクラスを右クリックした場合1番目にClassNameクラスをモデルから削除メニューを表示する() {
            Point2D clickedClassDiagramCanvas = new Point2D( 900.0, 600.0 );

            clickOn( "#classButtonInCD" );
            clickOn( clickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );

            rightClickOn( clickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();

            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText(), is( "ClassNameクラスをモデルから削除" ) );
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれているClassNameクラスのClassNameクラスをモデルから削除メニューを選択した場合描画していたClassNameクラスを削除する() {
            Point2D clickedClassDiagramCanvas = new Point2D( 900.0, 600.0 );
            Point2D deleteClassMenu = new Point2D( 910.0, 610.0 );

            clickOn( "#classButtonInCD" );
            clickOn( clickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );

            rightClickOn( clickedClassDiagramCanvas );
            clickOn( deleteClassMenu );
            GraphicsContext gc = getGraphicsContext();
            Paint fillColor = gc.getFill();
            Paint strokeColor = gc.getStroke();
            TextAlignment textAlignment = gc.getTextAlign();

            assertThat( fillColor, is( Color.BLACK ) );
            assertThat( strokeColor, is( Color.BLACK ) );
            assertThat( textAlignment, is( TextAlignment.CENTER ) );
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれている2つのクラスの内1つ目のクラスを削除する() {
            Point2D firstClickedClassDiagramCanvas = new Point2D( 900.0, 600.0 );
            Point2D deleteClassMenu = new Point2D( 910.0, 610.0 );

            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "FirstClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( secondClickedClassDiagramCanvas );
            write( "SecondClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );

            rightClickOn( firstClickedClassDiagramCanvas );
            clickOn( deleteClassMenu );
            GraphicsContext gc = getGraphicsContext();
            Paint fillColor = gc.getFill();
            Paint strokeColor = gc.getStroke();
            TextAlignment textAlignment = gc.getTextAlign();

            assertThat( fillColor, is( Color.BLACK ) );
            assertThat( strokeColor, is( Color.BLACK ) );
            assertThat( textAlignment, is( TextAlignment.CENTER ) );
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれているClassNameクラスを右クリックした後で何も描かれていない箇所を右クリックしても何も表示しない() {
            Point2D firstClickedClassDiagramCanvas = new Point2D( 900.0, 600.0 );

            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );

            rightClickOn( firstClickedClassDiagramCanvas );
            rightClickOn( secondClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();

            assertThat( scrollPane.getContextMenu(), is( nullValue() ) );
        }


        /**
         * クラス図キャンバス直下に存在するスクロールパネルを取得する。
         *
         * 具体的には、ステージ上のボーダーパネル上のアンカーパネル上のスプリットパネル上の2つ目のアンカーパネル上の
         * タブパネル上のアンカーパネル上のVボックス上の2つ目のアンカーパネル上のスクロールパネルを取得する。
         *
         * @return クラス図キャンバス直下のスクロールパネル
         */
        private ScrollPane getScrollPaneBelowClassDiagramCanvas() {
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
        private Canvas getCanvasOnScrollPane() {
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            AnchorPane anchorPane = (AnchorPane) scrollPane.getContent();
            return (Canvas) anchorPane.getChildren().get( 0 );
        }

        /**
         * クラス図キャンバスのグラフィックスコンテキストを取得する。
         *
         * getScrollPaneBelowClassDiagramCanvasとgetCanvasOnScrollPaneに依存する。
         *
         * @return クラス図キャンバス
         */
        public GraphicsContext getGraphicsContext() {
            Canvas canvas = getCanvasOnScrollPane();
            return canvas.getGraphicsContext2D();
        }
    }
}