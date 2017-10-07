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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.experimental.runners.Enclosed;
import org.testfx.api.FxRobotException;
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
        @Rule
        public ExpectedException fxRobotException = ExpectedException.none();

        Stage stage;
        Point2D xButtonOnDialogBox;
        String okButtonOnDialogBox;
        Point2D firstClickedClassDiagramCanvas;
        Point2D secondClickedClassDiagramCanvas;
        Point2D thirdClickedClassDiagramCanvas;
        String changeClassMenu;
        String deleteClassMenu;
        String classAttributionMenu;
        String addMenu;
        String changeMenu;
        String deleteMenu;

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
            okButtonOnDialogBox = "OK";
            firstClickedClassDiagramCanvas = new Point2D( 900.0, 600.0 );
            secondClickedClassDiagramCanvas = new Point2D( 1050.0, 300.0 );
            thirdClickedClassDiagramCanvas = new Point2D( 800.0, 450.0 );
            changeClassMenu = "クラスの名前の変更";
            deleteClassMenu = "クラスをモデルから削除";
            classAttributionMenu = "属性";
            addMenu = "追加";
            changeMenu = "変更";
            deleteMenu = "削除";
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
        public void ノーマルボタンをクリックするとノーマルボタンをオンにして他をオフにする() {
            clickOn( "#normalButtonInCD" );

            Button normalButton = ( Button ) lookup( "#normalButtonInCD" ).query();
            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();
            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();

            assertThat( normalButton.isDefaultButton(), is( true ) );
            assertThat( classButton.isDefaultButton(), is( false ) );
            assertThat( noteButton.isDefaultButton(), is( false ) );
        }

        @Test
        public void クラスボタンをクリックするとクラスボタンをオンにして他をオフにする() {
            clickOn( "#classButtonInCD" );

            Button normalButton = ( Button ) lookup( "#normalButtonInCD" ).query();
            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();
            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();

            assertThat( normalButton.isDefaultButton(), is( false ) );
            assertThat( classButton.isDefaultButton(), is( true ) );
            assertThat( noteButton.isDefaultButton(), is( false ) );
        }

        @Test
        public void ノートボタンをクリックするとノートボタンをオンにして他をオフにする() {
            clickOn( "#noteButtonInCD" );

            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();
            Button normalButton = ( Button ) lookup( "#normalButtonInCD" ).query();
            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();

            assertThat( normalButton.isDefaultButton(), is( false ) );
            assertThat( classButton.isDefaultButton(), is( false ) );
            assertThat( noteButton.isDefaultButton(), is( true ) );
        }

        @Test
        public void クラスボタンを選択している際にクラスボタンをクリックするとオンにして他をオフにする() {
            clickOn( "#classButtonInCD" );
            clickOn( "#classButtonInCD" );

            Button normalButton = ( Button ) lookup( "#normalButtonInCD" ).query();
            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();
            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();

            assertThat( normalButton.isDefaultButton(), is( false ) );
            assertThat( classButton.isDefaultButton(), is( true ) );
            assertThat( noteButton.isDefaultButton(), is( false ) );
        }

        @Test
        public void クラスアイコンを選択していない際にキャンバスをクリックしても何も表示しない( @Mocked TextInputDialog mock ) {
            new Expectations( TextInputDialog.class ) {{
                mock.showAndWait();
                times = 0;
            }};

            clickOn( "#classDiagramCanvas" );

            fxRobotException.expect( FxRobotException.class );
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
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );

            rightClickOn( firstClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();

            assertThat( scrollPane.getContextMenu(), is( nullValue() ) );
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれているClassNameクラスを右クリックした場合1番目と2番目にClassNameクラスの名前の変更メニューとClassNameクラスをモデルから削除メニューを表示する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );

            rightClickOn( firstClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();

            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText(), is( "ClassNameクラスの名前の変更" ) );
            assertThat( scrollPane.getContextMenu().getItems().get( 1 ).getText(), is( "ClassNameクラスをモデルから削除" ) );
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれているClassNameクラスのClassNameクラスをモデルから削除メニューを選択した場合描画していたClassNameクラスを削除する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );

            rightClickOn( firstClickedClassDiagramCanvas );
            clickOn( "ClassName" + deleteClassMenu );
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
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "FirstClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( secondClickedClassDiagramCanvas );
            write( "SecondClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );

            rightClickOn( firstClickedClassDiagramCanvas );
            clickOn( "FirstClassName" + deleteClassMenu );
            GraphicsContext gc = getGraphicsContext();
            Paint fillColor = gc.getFill();
            Paint strokeColor = gc.getStroke();
            TextAlignment textAlignment = gc.getTextAlign();

            assertThat( fillColor, is( Color.BLACK ) );
            assertThat( strokeColor, is( Color.BLACK ) );
            assertThat( textAlignment, is( TextAlignment.CENTER ) );
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれている2つのクラスの内1つ目のクラスを削除した後3つ目のクラスを描画する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "FirstClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( secondClickedClassDiagramCanvas );
            write( "SecondClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );

            rightClickOn( firstClickedClassDiagramCanvas );
            clickOn( "FirstClassName" + deleteClassMenu );

            clickOn( "#classButtonInCD" );
            clickOn( thirdClickedClassDiagramCanvas );
            write( "ThirdClassName" );
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
        public void ノーマルアイコンを選択している際にキャンバスに描かれている2つのクラスの内1つ目のクラスを削除した後3つ目のクラスを削除した場所に描画する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "FirstClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( secondClickedClassDiagramCanvas );
            write( "SecondClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );

            rightClickOn( firstClickedClassDiagramCanvas );
            clickOn( "FirstClassName" + deleteClassMenu );

            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "ThirdClassName" );
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
        public void ノーマルアイコンを選択している際にキャンバスに描かれているClassNameクラスを右クリックした後で何も描かれていない箇所を右クリックしても何も表示しない() {
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

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれているClassNameクラスのClassNameクラスの変更メニューを選択した場合描画していたClassNameクラスのクラス名を変更する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );
            rightClickOn( firstClickedClassDiagramCanvas );
            clickOn( "ClassName" + changeClassMenu );
            write( "ChangedClassName" );
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
        public void ノーマルアイコンを選択している際にキャンバスに描かれているClassNameクラスのClassNameクラスの変更メニューを選択し何も入力しなかった場合描画していたClassNameクラスのクラス名は変更しない() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );
            rightClickOn( firstClickedClassDiagramCanvas );
            clickOn( "ClassName" + changeClassMenu );
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
        public void ノーマルアイコンを選択している際にキャンバスに描かれているClassNameクラスの属性の追加メニューを選択した場合描画していたClassNameクラスに属性を追加する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classAttributionMenu );
            clickOn(addMenu);
            write( "- attribution : int" );
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
        public void ノーマルアイコンを選択している際にキャンバスに描かれているClassNameクラスに追加した属性の変更メニューを選択した場合描画していたClassNameクラスに変更した属性を描画する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classAttributionMenu );
            clickOn( addMenu );
            write( "- attribution : int" );
            clickOn( okButtonOnDialogBox );
            rightClickOn( firstClickedClassDiagramCanvas );

            moveTo( classAttributionMenu );
            moveTo( addMenu );
            moveTo( changeMenu );
            clickOn( "- attribution : int" );
            write( "- attribution : double" );
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
        public void ノーマルアイコンを選択している際にキャンバスに描かれているClassNameクラスに追加した属性の削除メニューを選択した場合描画していたClassNameクラスの属性を削除する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classAttributionMenu );
            clickOn( addMenu );
            write( "- attribution : int" );
            clickOn( okButtonOnDialogBox );
            rightClickOn( firstClickedClassDiagramCanvas );

            moveTo( classAttributionMenu );
            moveTo( addMenu );
            moveTo( deleteMenu );
            clickOn( "- attribution : int" );

            GraphicsContext gc = getGraphicsContext();
            Paint fillColor = gc.getFill();
            Paint strokeColor = gc.getStroke();
            TextAlignment textAlignment = gc.getTextAlign();

            assertThat( fillColor, is( Color.BLACK ) );
            assertThat( strokeColor, is( Color.BLACK ) );
            assertThat( textAlignment, is( TextAlignment.LEFT ) );
        }


        /**
         * クラス図キャンバス直下に存在するスクロールパネルを取得する。
         *
         * 具体的には、ステージ上のボーダーパネル上のアンカーパネル上のスプリットパネル上の2つ目のアンカーパネル上の
         * タブパネル上のアンカーパネル上のVボックス上の2つ目のアンカーパネル上のスクロールパネルを取得する。
         *
         * 右クリックメニューを表示する大元のパネルがこのスクロールパネルであるため、主に右クリックメニューのテストに利用する。
         *
         * @return クラス図キャンバス直下のスクロールパネル
         */
        private ScrollPane getScrollPaneBelowClassDiagramCanvas() {
            BorderPane borderPaneOnStage = ( BorderPane ) stage.getScene().getRoot().getChildrenUnmodifiable().get( 0 );
            AnchorPane anchorPaneOnBorderPane = ( AnchorPane ) borderPaneOnStage.getCenter();
            SplitPane splitPaneOnAnchorPaneOnBorderPane = ( SplitPane ) anchorPaneOnBorderPane.getChildren().get( 0 );
            AnchorPane anchorPaneOnSplitPane = ( AnchorPane ) splitPaneOnAnchorPaneOnBorderPane.getItems().get( 1 );
            TabPane tabPaneOnAnchorPaneOnSplitPane = ( TabPane ) anchorPaneOnSplitPane.getChildren().get( 0 );
            AnchorPane anchorPaneOnTabPane = ( AnchorPane ) tabPaneOnAnchorPaneOnSplitPane.getTabs().get( 0 ).getContent();
            VBox vBoxOnAnchorPaneOnTabPane = ( VBox ) anchorPaneOnTabPane.getChildren().get( 0 );
            AnchorPane anchorPaneOnVBox = ( AnchorPane ) vBoxOnAnchorPaneOnTabPane.getChildren().get( 1 );
            return ( ScrollPane ) anchorPaneOnVBox.getChildren().get( 0 );
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
            AnchorPane anchorPane = ( AnchorPane ) scrollPane.getContent();
            return ( Canvas ) anchorPane.getChildren().get( 0 );
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