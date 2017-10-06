package retuss;

import javafx.scene.control.*;

import javax.naming.Context;
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

    public ContextMenu getClassContextMenuInCD( String nodeName, String nodeType ) {
        ContextMenu popup = new ContextMenu();

        if( nodeType.equals( "クラス" ) ) {
            popup.getItems().add( new MenuItem( nodeName + "クラスをモデルから削除" ) );
            popup.getItems().add( new MenuItem( "名前の変更") );
            popup.getItems().add( new SeparatorMenuItem() );

            Menu attributionMenu = new Menu( "属性" );
            Menu operationMenu = new Menu( "操作" );
            MenuItem addAttributionMenuItem = new MenuItem( "追加" );
            MenuItem changeAttributionMenuItem = new MenuItem( "変更" );
            MenuItem deleteAttributionMenuItem = new MenuItem( "削除" );
            MenuItem displayAttributionMenuItem = new MenuItem( "表示選択" );
            MenuItem addOperationMenuItem = new MenuItem( "追加" );
            MenuItem changeOperationMenuItem = new MenuItem( "変更" );
            MenuItem deleteOperationMenuItem = new MenuItem( "削除" );
            MenuItem displayOperationMenuItem = new MenuItem( "表示選択" );

            attributionMenu.getItems().add( addAttributionMenuItem );
            attributionMenu.getItems().add( changeAttributionMenuItem );
            attributionMenu.getItems().add( deleteAttributionMenuItem );
            attributionMenu.getItems().add( displayAttributionMenuItem );
            operationMenu.getItems().add( addOperationMenuItem );
            operationMenu.getItems().add( changeOperationMenuItem );
            operationMenu.getItems().add( deleteOperationMenuItem );
            operationMenu.getItems().add( displayOperationMenuItem );

            popup.getItems().add( attributionMenu );
            popup.getItems().add( operationMenu );
        } else {
            //popup.getItems().add( new MenuItem( "モデルから削除" ) );
            //popup.getItems().add( new MenuItem( "内容の変更" ) );
        }

        return popup;
    }
}
