package retuss;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * RETUSSウィンドウの動作管理クラス
 *
 * retussMain.fxmlとretussCode.fxmlにおけるシグナルハンドラを扱う。
 */
public class Controller {
    @FXML
    Button normalButtonInCD;
    @FXML
    Button classButtonInCD;
    @FXML
    Button noteButtonInCD;
    @FXML
    ScrollPane classDiagramScrollPane;
    @FXML
    Canvas classDiagramCanvas;

    TextInputDialog classNameInputDialog;

    UtilityJavaFXComponent util = new UtilityJavaFXComponent();
    ClassDiagramDrawer classDiagramDrawer = new ClassDiagramDrawer();

    List< Button > buttonsInCD = new ArrayList<>();

    private GraphicsContext gc = null;

    /**
     * コンストラクタ
     *
     * Javaにおける通常のコンストラクタは使えないため、initializeメソッドをFXML経由で読み込む仕様になっている。
     */
    @FXML
    void initialize() {
        buttonsInCD.addAll( Arrays.asList( normalButtonInCD, classButtonInCD, noteButtonInCD ) );
        // buttonsInCD = util.setAllDefaultButtonIsFalseWithout( buttonsInCD, normalButtonInCD );
    }

    @FXML
    public void selectNormalInCD() {
        buttonsInCD = util.setAllDefaultButtonIsFalseWithout( buttonsInCD, normalButtonInCD );
    }
    @FXML
    public void selectClassInCD() {
        buttonsInCD = util.setAllDefaultButtonIsFalseWithout( buttonsInCD, classButtonInCD );
    }
    @FXML
    public void selectNoteInCD() {
        buttonsInCD = util.setAllDefaultButtonIsFalseWithout( buttonsInCD, noteButtonInCD );
    }

    @FXML
    public void clickedCanvasInCD( MouseEvent event ) {
        if( gc == null ) {
            gc = classDiagramCanvas.getGraphicsContext2D();
            classDiagramDrawer.setGraphicsContext( gc );
        }

        if( event.getButton() == MouseButton.PRIMARY ) {
            clickedCanvasByPrimaryButtonInCD( event.getX(), event.getY() );
        } else if( event.getButton() == MouseButton.SECONDARY ) {
            clickedCanvasBySecondaryButtonInCD();
        }
    }

    //--------------------------//
    // シグナルハンドラここまで //
    //--------------------------//

    private void clickedCanvasByPrimaryButtonInCD( double mouseX, double mouseY ) {
        String className = showClassNameInputDialog();
        classDiagramDrawer.setMouseCoordinates( mouseX, mouseY );
        classDiagramDrawer.setNodeText( className );
        classDiagramDrawer.addDrawnNode( buttonsInCD );
    }

    private String showClassNameInputDialog() {
        classNameInputDialog = new TextInputDialog();
        classNameInputDialog.setTitle( "クラス名" );
        classNameInputDialog.setHeaderText( "クラス名を入力してください。" );
        Optional< String > result = classNameInputDialog.showAndWait();

        if( result.isPresent() ) {
            return classNameInputDialog.getEditor().getText();
        } else {
            return "";
        }
    }

    private void clickedCanvasBySecondaryButtonInCD() {
        ContextMenu popup = new ContextMenu();
        MenuItem item = new MenuItem( "処理" );
        popup.getItems().add( item );
        classDiagramScrollPane.setContextMenu( popup );
    }
}
