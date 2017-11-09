package retuss;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import mockit.*;
import org.junit.jupiter.api.*;
import org.testfx.api.FxRobotException;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    @Nested
    public class クラス図の場合 extends ApplicationTest {
        Stage stage;
        Point2D xButtonOnDialogBox;
        String okButtonOnDialogBox;
        Point2D firstClickedClassDiagramCanvas;
        Point2D secondClickedClassDiagramCanvas;
        Point2D thirdClickedClassDiagramCanvas;
        Point2D betweenFirstAndSecondClickedClassDiagramCanvas;
        Point2D betweenFirstAndThirdClickedClassDiagramCanvas;
        Point2D betweenSecondAndThirdClickedClassDiagramCanvas;
        String changeClassMenu;
        String deleteClassMenu;
        String classAttributionMenu;
        String classOperationMenu;
        String addMenu;
        String changeMenu;
        String deleteMenu;
        String checkMenu;

        @Override
        public void start( Stage stage ) throws IOException {
            String fxmlFileName = "retussMain.fxml";
            Scene scene = new Scene( FXMLLoader.load( getClass().getResource( fxmlFileName ) ) );
            stage.setScene( scene );
            stage.show();
            this.stage = stage;
        }

        @BeforeEach
        public void setup() {
            xButtonOnDialogBox = new Point2D( 1050.0, 350.0 );
            okButtonOnDialogBox = "OK";
            firstClickedClassDiagramCanvas = new Point2D( 900.0, 600.0 );
            secondClickedClassDiagramCanvas = new Point2D( 1050.0, 300.0 );
            thirdClickedClassDiagramCanvas = new Point2D( 800.0, 450.0 );
            betweenFirstAndSecondClickedClassDiagramCanvas = new Point2D(
                    firstClickedClassDiagramCanvas.getX() + ( secondClickedClassDiagramCanvas.getX() - firstClickedClassDiagramCanvas.getX() ) / 2,
                    firstClickedClassDiagramCanvas.getY() - ( firstClickedClassDiagramCanvas.getY() - secondClickedClassDiagramCanvas.getY() ) / 2
            );
            betweenFirstAndThirdClickedClassDiagramCanvas = new Point2D(
                    firstClickedClassDiagramCanvas.getX() + ( thirdClickedClassDiagramCanvas.getX() - firstClickedClassDiagramCanvas.getX() ) / 2,
                    firstClickedClassDiagramCanvas.getY() - ( firstClickedClassDiagramCanvas.getY() - thirdClickedClassDiagramCanvas.getY() ) / 2
            );
            betweenSecondAndThirdClickedClassDiagramCanvas = new Point2D(
                    thirdClickedClassDiagramCanvas.getX() + ( secondClickedClassDiagramCanvas.getX() - thirdClickedClassDiagramCanvas.getX() ) / 2,
                    thirdClickedClassDiagramCanvas.getY() - ( thirdClickedClassDiagramCanvas.getY() - secondClickedClassDiagramCanvas.getY() ) / 2
            );
            changeClassMenu = "クラスの名前の変更";
            deleteClassMenu = "クラスをモデルから削除";
            classAttributionMenu = "属性";
            classOperationMenu = "操作";
            addMenu = "追加";
            changeMenu = "変更";
            deleteMenu = "削除";
            checkMenu = "表示選択";
        }

        @BeforeEach
        public void reset() {
            ClassNodeDiagram.resetNodeCount();
        }

        @Test
        public void どのボタンもクリックしていない場合はどれも選択していない() {
            Button normalButton = ( Button ) lookup( "#normalButtonInCD" ).query();
            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();
            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();

            assertThat( normalButton.isDefaultButton() ).isFalse();
            assertThat( classButton.isDefaultButton() ).isFalse();
            assertThat( noteButton.isDefaultButton() ).isFalse();
        }

        @Test
        public void ノーマルボタンをクリックするとノーマルボタンをオンにして他をオフにする() {
            clickOn( "#normalButtonInCD" );

            Button normalButton = ( Button ) lookup( "#normalButtonInCD" ).query();
            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();
            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();

            assertThat( normalButton.isDefaultButton() ).isTrue();
            assertThat( classButton.isDefaultButton() ).isFalse();
            assertThat( noteButton.isDefaultButton() ).isFalse();
        }

        @Test
        public void クラスボタンをクリックするとクラスボタンをオンにして他をオフにする() {
            clickOn( "#classButtonInCD" );

            Button normalButton = ( Button ) lookup( "#normalButtonInCD" ).query();
            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();
            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();

            assertThat( normalButton.isDefaultButton() ).isFalse();
            assertThat( classButton.isDefaultButton() ).isTrue();
            assertThat( noteButton.isDefaultButton() ).isFalse();
        }

        @Test
        public void ノートボタンをクリックするとノートボタンをオンにして他をオフにする() {
            clickOn( "#noteButtonInCD" );

            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();
            Button normalButton = ( Button ) lookup( "#normalButtonInCD" ).query();
            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();

            assertThat( normalButton.isDefaultButton() ).isFalse();
            assertThat( classButton.isDefaultButton() ).isFalse();
            assertThat( noteButton.isDefaultButton() ).isTrue();
        }

        @Test
        public void クラスボタンを選択している際にクラスボタンをクリックするとオンにして他をオフにする() {
            clickOn( "#classButtonInCD" );
            clickOn( "#classButtonInCD" );

            Button normalButton = ( Button ) lookup( "#normalButtonInCD" ).query();
            Button classButton = ( Button ) lookup( "#classButtonInCD" ).query();
            Button noteButton = ( Button ) lookup( "#noteButtonInCD" ).query();

            assertThat( normalButton.isDefaultButton() ).isFalse();
            assertThat( classButton.isDefaultButton() ).isTrue();
            assertThat( noteButton.isDefaultButton() ).isFalse();
        }

        @Test
        public void クラスアイコンを選択していない際にキャンバスをクリックしても何も表示しない( @Mocked TextInputDialog mock ) {
            clickOn( "#classDiagramCanvas" );

            assertThrows( FxRobotException.class, () -> clickOn( okButtonOnDialogBox ) );

            new Verifications() {{
                mock.showAndWait();
                times = 0;
            }};
        }

        @Test
        public void クラスアイコンを選択している際にキャンバスをクリックするとクラス名の入力ウィンドウを表示する( @Mocked TextInputDialog mock ) {
            clickOn( "#classButtonInCD" );
            clickOn( "#classDiagramCanvas" );

            new Verifications() {{
                mock.showAndWait();
                times = 1;
            }};
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

            assertThat( fillColor ).isEqualTo( Color.BLACK );
            assertThat( strokeColor ).isEqualTo( Color.BLACK );
            assertThat( textAlignment ).isEqualTo( TextAlignment.CENTER );

            clickOn( "#normalButtonInCD" );
            rightClickOn( "#classDiagramCanvas" );

            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "ClassName" );
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

            clickOn( "#normalButtonInCD" );
            rightClickOn( "#classDiagramCanvas" );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "FirstClassName" );
            rightClickOn( secondClickedClassDiagramCanvas );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "SecondClassName" );
        }

        @Test
        public void クラスアイコンを選択している際にキャンバスをクリックしクラス名を入力しない場合は何も表示しない() {
            clickOn( "#classButtonInCD" );
            clickOn( "#classDiagramCanvas" );

            clickOn( okButtonOnDialogBox );

            clickOn( "#normalButtonInCD" );
            rightClickOn( "#classDiagramCanvas" );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
        }

        @Test
        public void クラスアイコンを選択している際に何も描画されていないキャンバスを右クリックした場合は何も表示しない() {
            clickOn( "#classButtonInCD" );
            rightClickOn( "#classDiagramCanvas" );

            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();

            assertThat( scrollPane.getContextMenu() ).isNull();
        }

        @Test
        public void クラスアイコンを選択している際にキャンバスに描かれているClassNameクラスを右クリックした場合は何も表示しない() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );

            rightClickOn( firstClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();

            assertThat( scrollPane.getContextMenu() ).isNull();
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

            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).isEqualTo( "ClassNameクラスの名前の変更" );
            assertThat( scrollPane.getContextMenu().getItems().get( 1 ).getText() ).isEqualTo( "ClassNameクラスをモデルから削除" );
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

            clickOn( "#normalButtonInCD" );
            rightClickOn( firstClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
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

            clickOn( "#normalButtonInCD" );
            rightClickOn( firstClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( secondClickedClassDiagramCanvas );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "SecondClassName" );
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

            clickOn( "#normalButtonInCD" );
            rightClickOn( firstClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( secondClickedClassDiagramCanvas );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "SecondClassName" );
            rightClickOn( thirdClickedClassDiagramCanvas );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "ThirdClassName" );
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

            clickOn( "#normalButtonInCD" );
            rightClickOn( firstClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "ThirdClassName" );
            rightClickOn( secondClickedClassDiagramCanvas );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "SecondClassName" );
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

            assertThat( scrollPane.getContextMenu() ).isNull();
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
            clickOn( "#normalButtonInCD" );

            rightClickOn( firstClickedClassDiagramCanvas );

            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "ChangedClassName" );
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
            clickOn( "#normalButtonInCD" );

            rightClickOn( firstClickedClassDiagramCanvas );

            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "ClassName" );
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
            clickOn( addMenu );
            write( "- attribution : int" );
            clickOn( okButtonOnDialogBox );

            GraphicsContext gc = getGraphicsContext();
            Paint fillColor = gc.getFill();
            Paint strokeColor = gc.getStroke();
            TextAlignment textAlignment = gc.getTextAlign();

            assertThat( fillColor ).isEqualTo( Color.BLACK );
            assertThat( strokeColor ).isEqualTo( Color.BLACK );
            assertThat( textAlignment ).isEqualTo( TextAlignment.LEFT );

            rightClickOn( firstClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( ( ( Menu ) ( ( Menu ) scrollPane.getContextMenu().getItems().get( 3 ) ).getItems().get( 1 ) ).getItems().get( 0 ).getText() ).isEqualTo( "- attribution : int" );
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

            rightClickOn( firstClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( ( ( Menu ) ( ( Menu ) scrollPane.getContextMenu().getItems().get( 3 ) ).getItems().get( 1 ) ).getItems().get( 0 ).getText() ).isEqualTo( "- attribution : double" );
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

            rightClickOn( firstClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( ( ( Menu ) ( ( Menu ) scrollPane.getContextMenu().getItems().get( 3 ) ).getItems().get( 1 ) ).getItems().get( 0 ).getText() ).isEqualTo( "なし" );
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれているClassNameクラスに追加した属性の表示選択チェックメニューのチェックを外した場合描画していたClassNameクラスの属性を非表示にする() {
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
            moveTo( checkMenu );
            clickOn( "- attribution : int" );

            rightClickOn( firstClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( ( ( CheckMenuItem ) ( ( Menu ) ( ( Menu ) scrollPane.getContextMenu().getItems().get( 3 ) ).getItems().get( 3 ) ).getItems().get( 0 ) ).isSelected() ).isFalse();

            GraphicsContext gc = getGraphicsContext();
            TextAlignment textAlignment = gc.getTextAlign();
            assertThat( textAlignment ).isEqualTo( TextAlignment.CENTER );
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれているClassNameクラスに追加した非表示の属性の表示選択チェックメニューのチェックを付けた場合描画していたClassNameクラスの属性を表示する() {
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
            moveTo( checkMenu );
            clickOn( "- attribution : int" );

            rightClickOn( firstClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( ( ( CheckMenuItem ) ( ( Menu ) ( ( Menu ) scrollPane.getContextMenu().getItems().get( 3 ) ).getItems().get( 3 ) ).getItems().get( 0 ) ).isSelected() ).isFalse();
            moveTo( classAttributionMenu );
            moveTo( addMenu );
            moveTo( checkMenu );
            clickOn( "- attribution : int" );

            rightClickOn( firstClickedClassDiagramCanvas );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( ( ( CheckMenuItem ) ( ( Menu ) ( ( Menu ) scrollPane.getContextMenu().getItems().get( 3 ) ).getItems().get( 3 ) ).getItems().get( 0 ) ).isSelected() ).isTrue();
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれているClassNameクラスの操作の追加メニューを選択した場合描画していたClassNameクラスに操作を追加する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classOperationMenu );
            clickOn( addMenu );
            write( "+ operation() : void" );
            clickOn( okButtonOnDialogBox );

            GraphicsContext gc = getGraphicsContext();
            Paint fillColor = gc.getFill();
            Paint strokeColor = gc.getStroke();
            TextAlignment textAlignment = gc.getTextAlign();

            assertThat( fillColor ).isEqualTo( Color.BLACK );
            assertThat( strokeColor ).isEqualTo( Color.BLACK );
            assertThat( textAlignment ).isEqualTo( TextAlignment.LEFT );

            rightClickOn( firstClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( ( ( Menu ) ( ( Menu ) scrollPane.getContextMenu().getItems().get( 4 ) ).getItems().get( 1 ) ).getItems().get( 0 ).getText() ).isEqualTo( "+ operation() : void" );
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれているClassNameクラスに追加した操作の変更メニューを選択した場合描画していたClassNameクラスに変更した操作を描画する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classOperationMenu );
            clickOn( addMenu );
            write( "+ operation() : int" );
            clickOn( okButtonOnDialogBox );

            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classOperationMenu );
            moveTo( addMenu );
            moveTo( changeMenu );
            clickOn( "+ operation() : int" );
            write( "+ operation() : double" );
            clickOn( okButtonOnDialogBox );

            rightClickOn( firstClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( ( ( Menu ) ( ( Menu ) scrollPane.getContextMenu().getItems().get( 4 ) ).getItems().get( 1 ) ).getItems().get( 0 ).getText() ).isEqualTo( "+ operation() : double" );
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれているClassNameクラスに追加した操作の削除メニューを選択した場合描画していたClassNameクラスの操作を削除する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classOperationMenu );
            clickOn( addMenu );
            write( "+ operation() : int" );
            clickOn( okButtonOnDialogBox );

            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classOperationMenu );
            moveTo( addMenu );
            moveTo( deleteMenu );
            clickOn( "+ operation() : int" );

            rightClickOn( firstClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( ( ( Menu ) ( ( Menu ) scrollPane.getContextMenu().getItems().get( 4 ) ).getItems().get( 1 ) ).getItems().get( 0 ).getText() ).isEqualTo( "なし" );
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれているClassNameクラスに追加した操作の表示選択チェックメニューのチェックを外した場合描画していたClassNameクラスの操作を非表示にする() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classOperationMenu );
            clickOn( addMenu );
            write( "+ operation() : int" );
            clickOn( okButtonOnDialogBox );

            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classOperationMenu );
            moveTo( addMenu );
            moveTo( checkMenu );
            clickOn( "+ operation() : int" );

            rightClickOn( firstClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( ( ( CheckMenuItem ) ( ( Menu ) ( ( Menu ) scrollPane.getContextMenu().getItems().get( 4 ) ).getItems().get( 3 ) ).getItems().get( 0 ) ).isSelected() ).isFalse();

            GraphicsContext gc = getGraphicsContext();
            TextAlignment textAlignment = gc.getTextAlign();
            assertThat( textAlignment ).isEqualTo( TextAlignment.CENTER );
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれているClassNameクラスに追加した非表示の操作の表示選択チェックメニューのチェックを付けた場合描画していたClassNameクラスの操作を表示する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classOperationMenu );
            clickOn( addMenu );
            write( "+ operation() : int" );
            clickOn( okButtonOnDialogBox );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classOperationMenu );
            moveTo( addMenu );
            moveTo( checkMenu );
            clickOn( "+ operation() : int" );

            rightClickOn( firstClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( ( ( CheckMenuItem ) ( ( Menu ) ( ( Menu ) scrollPane.getContextMenu().getItems().get( 4 ) ).getItems().get( 3 ) ).getItems().get( 0 ) ).isSelected() ).isFalse();
            moveTo( classOperationMenu );
            moveTo( addMenu );
            moveTo( checkMenu );
            clickOn( "+ operation() : int" );

            rightClickOn( firstClickedClassDiagramCanvas );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( ( ( CheckMenuItem ) ( ( Menu ) ( ( Menu ) scrollPane.getContextMenu().getItems().get( 4 ) ).getItems().get( 3 ) ).getItems().get( 0 ) ).isSelected() ).isTrue();
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれているTestクラスの4隅を右クリックすると各箇所でメニューを表示する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "Test" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );

            rightClickOn( firstClickedClassDiagramCanvas.getX() + 49, firstClickedClassDiagramCanvas.getY() + 39 );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "Test" );
            rightClickOn( firstClickedClassDiagramCanvas.getX() - 49, firstClickedClassDiagramCanvas.getY() + 39 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "Test" );
            rightClickOn( firstClickedClassDiagramCanvas.getX() + 49, firstClickedClassDiagramCanvas.getY() - 39 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "Test" );
            rightClickOn( firstClickedClassDiagramCanvas.getX() - 49, firstClickedClassDiagramCanvas.getY() - 39 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "Test" );
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれているTestクラスの4隅より広い8箇所を右クリックしても何も表示しない() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "Test" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );

            rightClickOn( firstClickedClassDiagramCanvas.getX() + 51, firstClickedClassDiagramCanvas.getY() + 41 );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX(), firstClickedClassDiagramCanvas.getY() + 41 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX() - 51, firstClickedClassDiagramCanvas.getY() + 41 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX() - 51, firstClickedClassDiagramCanvas.getY() );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX() + 51, firstClickedClassDiagramCanvas.getY() );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX() + 51, firstClickedClassDiagramCanvas.getY() - 41 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX(), firstClickedClassDiagramCanvas.getY() - 41 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX() - 51, firstClickedClassDiagramCanvas.getY() - 41 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれている属性と操作を2つづつ持つTestクラスの4隅を右クリックすると各箇所でメニューを表示する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "Test" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classAttributionMenu );
            clickOn( addMenu );
            write( "- x : int" );
            clickOn( okButtonOnDialogBox );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classAttributionMenu );
            clickOn( addMenu );
            write( "- y : int" );
            clickOn( okButtonOnDialogBox );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classOperationMenu );
            clickOn( addMenu );
            write( "+ z() : T" );
            clickOn( okButtonOnDialogBox );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classOperationMenu );
            clickOn( addMenu );
            write( "+ w() : T" );
            clickOn( okButtonOnDialogBox );

            rightClickOn( firstClickedClassDiagramCanvas.getX() + 49, firstClickedClassDiagramCanvas.getY() + 59 );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "Test" );
            rightClickOn( firstClickedClassDiagramCanvas.getX() - 49, firstClickedClassDiagramCanvas.getY() + 59 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "Test" );
            rightClickOn( firstClickedClassDiagramCanvas.getX() + 49, firstClickedClassDiagramCanvas.getY() - 59 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "Test" );
            rightClickOn( firstClickedClassDiagramCanvas.getX() - 49, firstClickedClassDiagramCanvas.getY() - 59 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "Test" );
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれている属性と操作を2つづつ持つTestクラスの4隅より広い8箇所を右クリックしても何も表示しない() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "Test" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classAttributionMenu );
            clickOn( addMenu );
            write( "- x : int" );
            clickOn( okButtonOnDialogBox );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classAttributionMenu );
            clickOn( addMenu );
            write( "- y : int" );
            clickOn( okButtonOnDialogBox );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classOperationMenu );
            clickOn( addMenu );
            write( "+ z() : T" );
            clickOn( okButtonOnDialogBox );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classOperationMenu );
            clickOn( addMenu );
            write( "+ w() : T" );
            clickOn( okButtonOnDialogBox );

            rightClickOn( firstClickedClassDiagramCanvas.getX() + 51, firstClickedClassDiagramCanvas.getY() + 61 );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX(), firstClickedClassDiagramCanvas.getY() + 61 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX() - 51, firstClickedClassDiagramCanvas.getY() + 61 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX() - 51, firstClickedClassDiagramCanvas.getY() );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX() + 51, firstClickedClassDiagramCanvas.getY() );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX() + 51, firstClickedClassDiagramCanvas.getY() - 61 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX(), firstClickedClassDiagramCanvas.getY() - 61 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX() - 51, firstClickedClassDiagramCanvas.getY() - 61 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれている非表示の属性と操作を2つづつ持つTestクラスの4隅を右クリックするとメニューを表示しそれより広い8箇所を右クリックしても何も表示しない() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "Test" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#normalButtonInCD" );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classAttributionMenu );
            clickOn( addMenu );
            write( "- longAttribution1 : int" );
            clickOn( okButtonOnDialogBox );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classAttributionMenu );
            clickOn( addMenu );
            write( "- longAttribution2 : double" );
            clickOn( okButtonOnDialogBox );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classOperationMenu );
            clickOn( addMenu );
            write( "+ longOperation1() : void" );
            clickOn( okButtonOnDialogBox );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classOperationMenu );
            clickOn( addMenu );
            write( "+ longOperation2() : double" );
            clickOn( okButtonOnDialogBox );

            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classAttributionMenu );
            moveTo( addMenu );
            moveTo( checkMenu );
            clickOn( "- longAttribution1 : int" );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classAttributionMenu );
            moveTo( addMenu );
            moveTo( checkMenu );
            clickOn( "- longAttribution2 : double" );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classOperationMenu );
            moveTo( addMenu );
            moveTo( checkMenu );
            clickOn( "+ longOperation1() : void" );
            rightClickOn( firstClickedClassDiagramCanvas );
            moveTo( classOperationMenu );
            moveTo( addMenu );
            moveTo( checkMenu );
            clickOn( "+ longOperation2() : double" );

            rightClickOn( firstClickedClassDiagramCanvas.getX() + 49, firstClickedClassDiagramCanvas.getY() + 39 );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "Test" );
            rightClickOn( firstClickedClassDiagramCanvas.getX() - 49, firstClickedClassDiagramCanvas.getY() + 39 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "Test" );
            rightClickOn( firstClickedClassDiagramCanvas.getX() + 49, firstClickedClassDiagramCanvas.getY() - 39 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "Test" );
            rightClickOn( firstClickedClassDiagramCanvas.getX() - 49, firstClickedClassDiagramCanvas.getY() - 39 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "Test" );

            rightClickOn( firstClickedClassDiagramCanvas.getX() - 51, firstClickedClassDiagramCanvas.getY() - 41 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX() + 51, firstClickedClassDiagramCanvas.getY() + 41 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX(), firstClickedClassDiagramCanvas.getY() + 41 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX() - 51, firstClickedClassDiagramCanvas.getY() + 41 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX() - 51, firstClickedClassDiagramCanvas.getY() );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX() + 51, firstClickedClassDiagramCanvas.getY() );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX() + 51, firstClickedClassDiagramCanvas.getY() - 41 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( firstClickedClassDiagramCanvas.getX(), firstClickedClassDiagramCanvas.getY() - 41 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
        }

        @Test
        public void コンポジションアイコンを選択している際にキャンバスに描かれているClassNameクラスをクリックするとClassNameクラスの縁の色を変更する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#compositionButtonInCD" );

            clickOn( firstClickedClassDiagramCanvas );
            GraphicsContext gc = getGraphicsContext();
            Paint fillColor = gc.getFill();
            Paint strokeColor = gc.getStroke();
            TextAlignment textAlignment = gc.getTextAlign();

            assertThat( fillColor ).isEqualTo( Color.BLACK );
            assertThat( strokeColor ).isEqualTo( Color.RED );
            assertThat( textAlignment ).isEqualTo( TextAlignment.CENTER );
        }

        @Test
        public void コンポジションアイコンを選択している際にキャンバスに描かれているClassNameクラスをクリックした後にクラスを描画していない箇所をクリックするとClassNameクラスの縁の色を元に戻す() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "ClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#compositionButtonInCD" );

            clickOn( firstClickedClassDiagramCanvas );
            clickOn( secondClickedClassDiagramCanvas );
            GraphicsContext gc = getGraphicsContext();
            Paint fillColor = gc.getFill();
            Paint strokeColor = gc.getStroke();
            TextAlignment textAlignment = gc.getTextAlign();

            assertThat( fillColor ).isEqualTo( Color.BLACK );
            assertThat( strokeColor ).isEqualTo( Color.BLACK );
            assertThat( textAlignment ).isEqualTo( TextAlignment.CENTER );
        }

        @Test
        public void コンポジションアイコンを選択している際にキャンバスに描かれている2つのクラスをクリックしDialogに関係属性を記述するとコンポジション関係を描画する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "FirstClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( secondClickedClassDiagramCanvas );
            write( "SecondClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#compositionButtonInCD" );

            clickOn( firstClickedClassDiagramCanvas );
            clickOn( secondClickedClassDiagramCanvas );
            write( "- composition" );
            clickOn( okButtonOnDialogBox );

            clickOn( "#normalButtonInCD" );
            rightClickOn( betweenFirstAndSecondClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "- composition" );
        }

        @Test
        public void コンポジションアイコンを選択している際にキャンバスに描かれているFirstClassNameクラスをクリックし描画されていない箇所をクリックした後でFirstClassNameクラスをクリックすると縁の色を変更するがDialogは表示しない() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "FirstClassName" );
            clickOn( okButtonOnDialogBox );

            clickOn( "#compositionButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            clickOn( secondClickedClassDiagramCanvas );
            clickOn( firstClickedClassDiagramCanvas );
            GraphicsContext gc = getGraphicsContext();
            Paint strokeColor = gc.getStroke();

            assertThat( strokeColor ).isEqualTo( Color.RED );
            assertThrows( FxRobotException.class, () -> clickOn( okButtonOnDialogBox ) );
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれている関係属性の範囲外を右クリックしても右クリックメニューは表示しない() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "FirstClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( secondClickedClassDiagramCanvas );
            write( "SecondClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#compositionButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            clickOn( secondClickedClassDiagramCanvas );
            write( "- composition" );
            clickOn( okButtonOnDialogBox );

            Point2D noDrawnPoint1 = new Point2D( secondClickedClassDiagramCanvas.getX() + 10.0, firstClickedClassDiagramCanvas.getY() + 10.0 );
            Point2D noDrawnPoint2 = new Point2D( secondClickedClassDiagramCanvas.getX() - 50.0, firstClickedClassDiagramCanvas.getY() - 100.0 );
            Point2D noDrawnPoint3 = new Point2D( firstClickedClassDiagramCanvas.getX() + 50.0, secondClickedClassDiagramCanvas.getY() + 100.0 );
            Point2D noDrawnPoint4 = new Point2D( firstClickedClassDiagramCanvas.getX() - 10.0, secondClickedClassDiagramCanvas.getY() - 10.0 );

            clickOn( "#normalButtonInCD" );
            rightClickOn( noDrawnPoint1 );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( noDrawnPoint2 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( noDrawnPoint3 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
            rightClickOn( noDrawnPoint4 );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれている関係属性を右クリックし変更を選択すると変更したコンポジション関係を描画する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "FirstClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( secondClickedClassDiagramCanvas );
            write( "SecondClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#compositionButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            clickOn( secondClickedClassDiagramCanvas );
            write( "- composition" );
            clickOn( okButtonOnDialogBox );

            clickOn( "#normalButtonInCD" );
            rightClickOn( betweenFirstAndSecondClickedClassDiagramCanvas );
            clickOn( "- composition の変更" );

            write( "+ changedComposition");
            clickOn( okButtonOnDialogBox );
            rightClickOn( betweenFirstAndSecondClickedClassDiagramCanvas );

            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "+ changedComposition" );
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれている関係属性を右クリックし削除を選択するとコンポジション関係を削除する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "FirstClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( secondClickedClassDiagramCanvas );
            write( "SecondClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#compositionButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            clickOn( secondClickedClassDiagramCanvas );
            write( "- composition" );
            clickOn( okButtonOnDialogBox );

            clickOn( "#normalButtonInCD" );
            rightClickOn( betweenFirstAndSecondClickedClassDiagramCanvas );
            clickOn( "- composition の削除" );

            rightClickOn( betweenFirstAndSecondClickedClassDiagramCanvas );

            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu() ).isNull();
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれている2つのクラス間に関係属性を2つ描画すると右クリックメニュー上では最後に描画したメニュー内容を返す() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "FirstClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( secondClickedClassDiagramCanvas );
            write( "SecondClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#compositionButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            clickOn( secondClickedClassDiagramCanvas );
            write( "- composition1" );
            clickOn( okButtonOnDialogBox );
            clickOn( firstClickedClassDiagramCanvas );
            clickOn( secondClickedClassDiagramCanvas );
            write( "- composition2" );
            clickOn( okButtonOnDialogBox );

            clickOn( "#normalButtonInCD" );
            rightClickOn( betweenFirstAndSecondClickedClassDiagramCanvas );

            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "- composition2" );
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれている3つのクラス間に関係属性を2つ描画する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "FirstClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( secondClickedClassDiagramCanvas );
            write( "SecondClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( thirdClickedClassDiagramCanvas );
            write( "ThirdClassName" );
            clickOn( okButtonOnDialogBox );

            clickOn( "#compositionButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            clickOn( secondClickedClassDiagramCanvas );
            write( "- composition1" );
            clickOn( okButtonOnDialogBox );
            clickOn( firstClickedClassDiagramCanvas );
            clickOn( thirdClickedClassDiagramCanvas );
            write( "- composition2" );
            clickOn( okButtonOnDialogBox );

            clickOn( "#normalButtonInCD" );
            rightClickOn( betweenFirstAndSecondClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "- composition1" );

            rightClickOn( betweenFirstAndThirdClickedClassDiagramCanvas );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "- composition2" );
        }

        @Test
        public void ノーマルアイコンを選択している際にキャンバスに描かれている3つのクラス間に関係属性を3つ描画する() {
            clickOn( "#classButtonInCD" );
            clickOn( firstClickedClassDiagramCanvas );
            write( "FirstClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( secondClickedClassDiagramCanvas );
            write( "SecondClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( thirdClickedClassDiagramCanvas );
            write( "ThirdClassName" );
            clickOn( okButtonOnDialogBox );
            clickOn( "#compositionButtonInCD" );

            clickOn( firstClickedClassDiagramCanvas );
            clickOn( secondClickedClassDiagramCanvas );
            write( "- compositionFromFirstToSecond" );
            clickOn( okButtonOnDialogBox );
            clickOn( secondClickedClassDiagramCanvas );
            clickOn( thirdClickedClassDiagramCanvas );
            write( "- compositionFromSecondToThird" );
            clickOn( okButtonOnDialogBox );
            clickOn( thirdClickedClassDiagramCanvas );
            clickOn( firstClickedClassDiagramCanvas );
            write( "- compositionFromThirdToFirst" );
            clickOn( okButtonOnDialogBox );

            clickOn( "#normalButtonInCD" );
            rightClickOn( betweenFirstAndSecondClickedClassDiagramCanvas );
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "- compositionFromFirstToSecond" );

            rightClickOn( betweenSecondAndThirdClickedClassDiagramCanvas );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "- compositionFromSecondToThird" );

            rightClickOn( betweenFirstAndThirdClickedClassDiagramCanvas );
            scrollPane = getScrollPaneBelowClassDiagramCanvas();
            assertThat( scrollPane.getContextMenu().getItems().get( 0 ).getText() ).startsWith( "- compositionFromThirdToFirst" );
        }


        /**
         * クラス図キャンバス直下に存在するスクロールパネルを取得する。
         *
         * 具体的には、ステージ上のボーダーパネル上のアンカーパネル上のスプリットパネル上の2つ目のアンカーパネル上の
         * タブパネル上のアンカーパネル上のボーダーパネル上のセンターのアンカーパネル上のスクロールパネルを取得する。
         *
         * 右クリックメニューを表示する大元のパネルがこのスクロールパネルであるため、主に右クリックメニューのテストに利用する。
         *
         * @return クラス図キャンバス直下のスクロールパネル FXMLファイルを書き換えるか実行中にどこかのパネルを消さない限り{@code null}になる可能性はない
         */
        private ScrollPane getScrollPaneBelowClassDiagramCanvas() {
            BorderPane borderPaneOnStage = ( BorderPane ) stage.getScene().getRoot().getChildrenUnmodifiable().get( 0 );
            AnchorPane anchorPaneOnBorderPane = ( AnchorPane ) borderPaneOnStage.getCenter();
            SplitPane splitPaneOnAnchorPaneOnBorderPane = ( SplitPane ) anchorPaneOnBorderPane.getChildren().get( 0 );
            AnchorPane anchorPaneOnSplitPane = ( AnchorPane ) splitPaneOnAnchorPaneOnBorderPane.getItems().get( 1 );
            TabPane tabPaneOnAnchorPaneOnSplitPane = ( TabPane ) anchorPaneOnSplitPane.getChildren().get( 0 );
            AnchorPane anchorPaneOnTabPane = ( AnchorPane ) tabPaneOnAnchorPaneOnSplitPane.getTabs().get( 0 ).getContent();
            BorderPane borderPaneOnAnchorPaneOnTabPane = ( BorderPane ) anchorPaneOnTabPane.getChildren().get( 0 );
            AnchorPane anchorPaneOnVBox = ( AnchorPane ) borderPaneOnAnchorPaneOnTabPane.getCenter();
            return ( ScrollPane ) anchorPaneOnVBox.getChildren().get( 0 );
        }

        /**
         * クラス図キャンバスのグラフィックスコンテキストを取得する。
         * getScrollPaneBelowClassDiagramCanvasに依存する。
         *
         * クラス図のグラフィックをセットする際の色などを取得するために用いる。
         *
         * @return クラス図キャンバスのグラフィックスコンテキスト FXMLファイルを書き換えるか実行中にどこかのパネルを消さない限り{@code null}になる可能性はない
         */
        private GraphicsContext getGraphicsContext() {
            ScrollPane scrollPane = getScrollPaneBelowClassDiagramCanvas();
            AnchorPane anchorPane = ( AnchorPane ) scrollPane.getContent();
            Canvas canvas = ( Canvas ) anchorPane.getChildren().get( 0 );
            return canvas.getGraphicsContext2D();
        }
    }
}