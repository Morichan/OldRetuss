package retuss;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;

public class Controller {
    @FXML
    Button normalButtonInCD;
    @FXML
    Button classButtonInCD;
    @FXML
    Button noteButtonInCD;
    @FXML
    Button compositionButtonInCD;
    @FXML
    ScrollPane classDiagramScrollPane;
    @FXML
    Canvas classDiagramCanvas;
    @FXML
    public void selectNormalInCD() {
        //buttonsInCD = util.setAllDefaultButtonIsFalseWithout( buttonsInCD, normalButtonInCD );
    }
    @FXML
    public void selectClassInCD() {
        //buttonsInCD = util.setAllDefaultButtonIsFalseWithout( buttonsInCD, classButtonInCD );
    }
    @FXML
    public void selectNoteInCD() {
        //buttonsInCD = util.setAllDefaultButtonIsFalseWithout( buttonsInCD, noteButtonInCD );
    }
    @FXML
    public void selectCompositionInCD() {
        //buttonsInCD = util.setAllDefaultButtonIsFalseWithout( buttonsInCD, compositionButtonInCD );
    }

    @FXML
    public void clickedCanvasInCD( MouseEvent event ) {
    }
}
