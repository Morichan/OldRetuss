package retuss;

import javafx.scene.control.*;

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

    public ContextMenu getClassContextMenuInCD( String nodeName, ContentType nodeType, List< String > nodeContents1, List< String > nodeContents2, List< Boolean > nodeContents3, List< Boolean > nodeContents4 ) {
        ContextMenu popup = new ContextMenu();

        if( nodeType == ContentType.Class ) {
            popup.getItems().add( new MenuItem( nodeName + "クラスの名前の変更") );
            popup.getItems().add( new MenuItem( nodeName + "クラスをモデルから削除" ) );
            popup.getItems().add( new SeparatorMenuItem() );

            Menu attributionMenu = new Menu( "属性" );
            Menu operationMenu = new Menu( "操作" );
            MenuItem addAttributionMenuItem = new MenuItem( "追加" );
            Menu changeAttributionMenu = new Menu( "変更" );
            Menu deleteAttributionMenu = new Menu( "削除" );
            Menu displayAttributionMenu = new Menu( "表示選択" );
            MenuItem addOperationMenuItem = new MenuItem( "追加" );
            Menu changeOperationMenu = new Menu( "変更" );
            Menu deleteOperationMenu = new Menu( "削除" );
            Menu displayOperationMenu = new Menu( "表示選択" );

            if( nodeContents1.size() > 0 ) {
                for( int i = 0; i < nodeContents1.size(); i++ ) {
                    changeAttributionMenu.getItems().add( new MenuItem( nodeContents1.get( i ) ) );
                    deleteAttributionMenu.getItems().add( new MenuItem( nodeContents1.get( i ) ) );
                    CheckMenuItem checkMenuItem = new CheckMenuItem( nodeContents1.get( i ) );
                    checkMenuItem.setSelected( nodeContents3.get( i ) );
                    displayAttributionMenu.getItems().add( checkMenuItem );
                }
            } else {
                MenuItem hasNotChangeAttributionMenuItem = new MenuItem( "なし" );
                MenuItem hasNotDeleteAttributionMenuItem = new MenuItem( "なし" );
                CheckMenuItem hasNotDisplayAttributionCheckMenuItem = new CheckMenuItem( "なし" );
                hasNotChangeAttributionMenuItem.setDisable( true );
                hasNotDeleteAttributionMenuItem.setDisable( true );
                hasNotDisplayAttributionCheckMenuItem.setDisable( true );
                changeAttributionMenu.getItems().add( hasNotChangeAttributionMenuItem );
                deleteAttributionMenu.getItems().add( hasNotDeleteAttributionMenuItem );
                displayAttributionMenu.getItems().add( hasNotDisplayAttributionCheckMenuItem );
            }

            if( nodeContents2.size() > 0 ) {
                for(int i = 0; i < nodeContents2.size(); i++ ) {
                    changeOperationMenu.getItems().add( new MenuItem( nodeContents2.get( i ) ) );
                    deleteOperationMenu.getItems().add( new MenuItem( nodeContents2.get( i ) ) );
                    CheckMenuItem checkMenuItem = new CheckMenuItem( nodeContents2.get( i ) );
                    checkMenuItem.setSelected( nodeContents4.get( i ) );
                    displayOperationMenu.getItems().add( checkMenuItem );
                }
            } else {
                MenuItem hasNotChangeOperationMenuItem = new MenuItem( "なし" );
                MenuItem hasNotDeleteOperationMenuItem = new MenuItem( "なし" );
                CheckMenuItem hasNotDisplayOperationCheckMenuItem = new CheckMenuItem( "なし" );
                hasNotChangeOperationMenuItem.setDisable( true );
                hasNotDeleteOperationMenuItem.setDisable( true );
                hasNotDisplayOperationCheckMenuItem.setDisable( true );
                changeOperationMenu.getItems().add( hasNotChangeOperationMenuItem );
                deleteOperationMenu.getItems().add( hasNotDeleteOperationMenuItem );
                displayOperationMenu.getItems().add( hasNotDisplayOperationCheckMenuItem );
            }

            attributionMenu.getItems().add( addAttributionMenuItem );
            attributionMenu.getItems().add( changeAttributionMenu );
            attributionMenu.getItems().add( deleteAttributionMenu );
            attributionMenu.getItems().add( displayAttributionMenu );
            operationMenu.getItems().add( addOperationMenuItem );
            operationMenu.getItems().add( changeOperationMenu );
            operationMenu.getItems().add( deleteOperationMenu );
            operationMenu.getItems().add( displayOperationMenu );

            popup.getItems().add( attributionMenu );
            popup.getItems().add( operationMenu );
        } else {
            //popup.getItems().add( new MenuItem( "モデルから削除" ) );
            //popup.getItems().add( new MenuItem( "内容の変更" ) );
        }

        return popup;
    }
}
