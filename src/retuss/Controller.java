package retuss;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {
    @FXML
    Button classButtonInCD;
    @FXML
    Button noteButtonInCD;

    List< Button > buttonsInCD = new ArrayList<>();

    @FXML
    void initialize() {
        buttonsInCD.addAll( Arrays.asList( classButtonInCD, noteButtonInCD ) );
    }

    @FXML
    public void selectClassInCD() {
        setAllDefaultButtonIsFalseWithout( classButtonInCD );
    }

    @FXML
    public void selectNoteInCD() {
        setAllDefaultButtonIsFalseWithout( noteButtonInCD );
    }

    private void setAllDefaultButtonIsFalseWithout( Button buttonSetsToTrue ) {
        for( Button button : buttonsInCD ) {
            if( button.equals( buttonSetsToTrue ) ) {
                button.setDefaultButton( true );
            } else {
                button.setDefaultButton( false );
            }
        }
    }
}
