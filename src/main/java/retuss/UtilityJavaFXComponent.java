package retuss;

import javafx.scene.control.Button;

import java.util.List;

/**
 * JavaFXコンポーネントにまつわる様々なユーティリティを提供する。
 */
public class UtilityJavaFXComponent {

    /**
     * 表示しているボタンの内、選択したボタンをtrueに、それ以外のボタンをfalseにする。
     *
     * @param buttonSetsToTrue trueにセットするボタン
     */
    public List< Button > setAllDefaultButtonIsFalseWithout( List< Button > buttons, Button buttonSetsToTrue ) {
        for( Button button : buttons ) {
            if( button.equals( buttonSetsToTrue ) ) {
                button.setDefaultButton( true );
            } else {
                button.setDefaultButton( false );
            }
        }

        return buttons;
    }
}
