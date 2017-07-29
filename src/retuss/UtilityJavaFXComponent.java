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

    /**
     * 選択されているボタンを返す。
     *
     * @param buttons ボタンリスト（1つだけがtrueになっている）
     */
    public Button getDefaultButtonIn( List< Button > buttons ) {
        int count = 0;

        for( int i = 0; i < buttons.size(); i++ ) {
            if( buttons.get( i ).isDefaultButton() ) {
                count = i;
                break;
            }
        }
        return buttons.get( count );
    }
}
