package retuss;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
            clickedCanvasBySecondaryButtonInCD( event.getX(), event.getY() );
        }
    }

    //--------------------------//
    // シグナルハンドラここまで //
    //--------------------------//

    /**
     * クラス図キャンバス上で（通常）左クリックした際に実行する。
     * 操作ボタンにより動作が異なるが、通常操作以外は描画のみを行う。
     * また、通常操作時は何も動作しない。
     *
     * @param mouseX 左クリック時のマウス位置のX軸
     * @param mouseY 左クリック時のマウス位置のY軸
     */
    private void clickedCanvasByPrimaryButtonInCD( double mouseX, double mouseY ) {
        if( classDiagramDrawer.isAlreadyDrawnAnyDiagram( mouseX, mouseY ) ) return;

        if( util.getDefaultButtonIn( buttonsInCD ) == classButtonInCD ) {
            String className = showCreateClassNameInputDialog();
            classDiagramDrawer.setMouseCoordinates( mouseX, mouseY );
            classDiagramDrawer.setNodeText( className );
            classDiagramDrawer.addDrawnNode( buttonsInCD );
        }
    }

    private String showCreateClassNameInputDialog() {
        return showClassDiagramInputDialog( "クラス名", "クラス名を入力してください。" );
    }

    private String showChangeClassNameInputDialog() {
        return showClassDiagramInputDialog( "クラス名の変更", "変更後のクラス名を入力してください。" );
    }

    private String showAddClassAttributionInputDialog() {
        return showClassDiagramInputDialog( "属性の追加", "追加する属性を入力してください" );
    }

    private String showChangeClassAttributionInputDialog( String attribution ) {
        return showClassDiagramInputDialog( "属性の変更", attribution + "\n変更後の属性を入力してください" );
    }

    /**
     * クラス図のテキスト入力ダイアログを表示する。
     * 表示中はダイアログ以外の本機能の他ウィンドウは入力を受け付けない。
     * テキスト入力ダイアログを消したまたは入力を受け付けた場合は他ウィンドウの入力受付を再開する。
     *
     * @param title テキスト入力ダイアログのタイトル
     * @param headerText テキスト入力ダイアログのヘッダーテキスト
     * @return 入力された文字列 入力せずにOKボタンを押した場合や×ボタンを押した場合は空文字を返す。
     */
    private String showClassDiagramInputDialog( String title, String headerText ) {
        classNameInputDialog = new TextInputDialog();
        classNameInputDialog.setTitle( title );
        classNameInputDialog.setHeaderText( headerText );
        Optional< String > result = classNameInputDialog.showAndWait();

        if( result.isPresent() ) {
            return classNameInputDialog.getEditor().getText();
        } else {
            return "";
        }
    }

    /**
     * クラス図キャンバス上で（通常）右クリックした際に実行する。
     * 通常操作時のみ動作する。
     * メニュー表示を行うが、右クリックしたキャンバス上の位置により動作は異なる。
     *
     * @param mouseX 右クリック時のマウス位置のX軸
     * @param mouseY 右クリック時のマウス位置のY軸
     */
    private void clickedCanvasBySecondaryButtonInCD( double mouseX, double mouseY ) {
        classDiagramScrollPane.setContextMenu( null );

        if( ! classDiagramDrawer.isAlreadyDrawnAnyDiagram( mouseX, mouseY ) ) return;
        if( util.getDefaultButtonIn( buttonsInCD ) != normalButtonInCD ) return;

        NodeDiagram nodeDiagram = classDiagramDrawer.findNodeDiagram( mouseX, mouseY );
        ContextMenu contextMenu = util.getClassContextMenuInCD( nodeDiagram.getNodeText(), nodeDiagram.getNodeType(),
                classDiagramDrawer.getDrawnNodeTextList( classDiagramDrawer.getCurrentNodeNumber(), ContentType.Attribution ),
                classDiagramDrawer.getDrawnNodeTextList( classDiagramDrawer.getCurrentNodeNumber(), ContentType.Operation ) );

        classDiagramScrollPane.setContextMenu( formatClassContextMenuInCD( contextMenu, nodeDiagram.getNodeType(), mouseX, mouseY ) );
    }

    /**
     * クラス図キャンバス上での右クリックメニューの各メニューアイテムの動作を整形する。
     * 名前の変更と内容の追加と内容の変更メニューではテキスト入力ダイアログを表示するが、それ以外はメニューアイテムを選択して直後にキャンバスを再描画する。
     *
     * @param contextMenu 右クリックメニューの見た目が整形済みの右クリックメニュー UtilityJavaFXComponentクラスのgetClassContextMenuInCDメソッドで取得したインスタンスを入れる必要がある。
     * @param type 右クリックした要素の種類
     * @param mouseX 右クリック時のマウス位置のX軸
     * @param mouseY 右クリック時のマウス位置のY軸
     * @return 動作整形済みの右クリックメニュー UtilityJavaFXComponentクラスで整形していないメニューや未分類の要素の種類を{@code contextMenu}や{@code type}に入れた場合は{@code null}を返す。
     */
    private ContextMenu formatClassContextMenuInCD( ContextMenu contextMenu, ContentType type, double mouseX, double mouseY ) {
        if( type == ContentType.Class ) {
            if( contextMenu.getItems().size() != 5 ) return null;

            contextMenu.getItems().get( 0 ).setOnAction( event -> {
                String className = showChangeClassNameInputDialog();
                classDiagramDrawer.changeDrawnNodeText( classDiagramDrawer.getCurrentNodeNumber(), ContentType.Title, 0, className );
                classDiagramDrawer.allReDrawNode();
            } );
            contextMenu.getItems().get( 1 ).setOnAction( event -> {
                classDiagramDrawer.deleteDrawnNode( classDiagramDrawer.getCurrentNodeNumber() );
                classDiagramDrawer.allReDrawNode();
            } );
            ( ( Menu ) contextMenu.getItems().get( 3 ) ).getItems().get( 0 ).setOnAction( event -> {
                String addAttribution = showAddClassAttributionInputDialog();
                classDiagramDrawer.addDrawnNodeText( classDiagramDrawer.getCurrentNodeNumber(), ContentType.Attribution, addAttribution );
                classDiagramDrawer.allReDrawNode();
            } );
            List< String > attributions = classDiagramDrawer.getDrawnNodeTextList( classDiagramDrawer.getCurrentNodeNumber(), ContentType.Attribution );
            for( int i = 0; i < attributions.size(); i++ ) {
                int contentNumber = i;
                ( ( Menu ) ( ( Menu ) contextMenu.getItems().get( 3 ) ).getItems().get( 1 ) ).getItems().get( i ).setOnAction( event -> {
                    String changedAttribution = showChangeClassAttributionInputDialog( attributions.get( contentNumber ) );
                    classDiagramDrawer.changeDrawnNodeText( classDiagramDrawer.getCurrentNodeNumber(), ContentType.Attribution, contentNumber, changedAttribution );
                    classDiagramDrawer.allReDrawNode();
                } );
            }
        } else {
            return null;
        }

        return contextMenu;
    }
}
