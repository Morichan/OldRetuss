package retuss;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    Canvas classDiagramCanvas;

    UtilityJavaFXComponent util = new UtilityJavaFXComponent();

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
        if( gc == null ) gc = classDiagramCanvas.getGraphicsContext2D();

        if( event.getButton() == MouseButton.PRIMARY ) {
            clickedCanvasByPrimaryButtonInCD();
        } else if( event.getButton() == MouseButton.SECONDARY ) {
            clickedCanvasBySecondaryButtonInCD();
        }
    }

    //--------------------------//
    // シグナルハンドラここまで //
    //--------------------------//

    private void clickedCanvasByPrimaryButtonInCD() {

    }

    private void clickedCanvasBySecondaryButtonInCD() {

    }
}
