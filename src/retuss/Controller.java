package retuss;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

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
    Button compositionButtonInCD;
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
        buttonsInCD.addAll( Arrays.asList( normalButtonInCD, classButtonInCD, noteButtonInCD, compositionButtonInCD ) );
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
    public void selectCompositionInCD() {
        buttonsInCD = util.setAllDefaultButtonIsFalseWithout( buttonsInCD, compositionButtonInCD );
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
        if( classDiagramDrawer.isAlreadyDrawnAnyDiagram( mouseX, mouseY ) ) {
            if( util.getDefaultButtonIn( buttonsInCD ) == compositionButtonInCD ) {
                if( ! classDiagramDrawer.hasWaitedCorrectDrawnDiagram( ContentType.Composition, mouseX, mouseY ) ) {
                    classDiagramDrawer.setMouseCoordinates( mouseX, mouseY );
                    classDiagramDrawer.allReDrawCanvas();
                } else {
                    String compositionName = showCreateCompositionNameInputDialog();
                    classDiagramDrawer.addDrawnEdge( buttonsInCD, compositionName, mouseX, mouseY );
                    classDiagramDrawer.allReDrawCanvas();
                }
            }
        } else {
            classDiagramDrawer.setMouseCoordinates( mouseX, mouseY );
            if( util.getDefaultButtonIn( buttonsInCD ) == classButtonInCD ) {
                String className = showCreateClassNameInputDialog();
                classDiagramDrawer.setNodeText( className );
                classDiagramDrawer.addDrawnNode( buttonsInCD );
                classDiagramDrawer.allReDrawCanvas();
            } else if( util.getDefaultButtonIn( buttonsInCD ) == compositionButtonInCD ) {
                classDiagramDrawer.resetNodeChosen( classDiagramDrawer.getCurrentNodeNumber() );
                classDiagramDrawer.allReDrawCanvas();
            }
        }
    }

    private String showCreateClassNameInputDialog() {
        return showClassDiagramInputDialog( "クラスの追加", "追加するクラスのクラス名を入力してください。", "" );
    }
    private String showChangeClassNameInputDialog( String className ) {
        return showClassDiagramInputDialog( "クラス名の変更", "変更後のクラス名を入力してください。", className );
    }
    private String showAddClassAttributionInputDialog() {
        return showClassDiagramInputDialog( "属性の追加", "追加する属性を入力してください。", "" );
    }
    private String showChangeClassAttributionInputDialog( String attribution ) {
        return showClassDiagramInputDialog( "属性の変更", "変更後の属性を入力してください。", attribution );
    }
    private String showAddClassOperationInputDialog() {
        return showClassDiagramInputDialog( "操作の追加", "追加する操作を入力してください。", "" );
    }
    private String showChangeClassOperationInputDialog( String operation ) {
        return showClassDiagramInputDialog( "操作の変更", "変更後の操作を入力してください。", operation );
    }
    private String showCreateCompositionNameInputDialog() {
        return showClassDiagramInputDialog( "コンポジションの追加", "コンポジション先の関連端名を入力してください。", "" );
    }
    private String showChangeCompositionNameInputDialog( String composition ) {
        return showClassDiagramInputDialog( "コンポジションの変更", "変更後のコンポジション先の関連端名を入力してください。", composition );
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
    private String showClassDiagramInputDialog( String title, String headerText, String contentText ) {
        classNameInputDialog = new TextInputDialog( contentText );
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

        ContentType currentType = classDiagramDrawer.searchDrawnAnyDiagramType( mouseX, mouseY );

        if( currentType == ContentType.Undefined ) return;
        if( util.getDefaultButtonIn( buttonsInCD ) != normalButtonInCD ) return;

        if( currentType == ContentType.Class ) {
            NodeDiagram nodeDiagram = classDiagramDrawer.findNodeDiagram( mouseX, mouseY );
            ContextMenu contextMenu = util.getClassContextMenuInCD( nodeDiagram.getNodeText(), nodeDiagram.getNodeType(),
                    classDiagramDrawer.getDrawnNodeTextList( classDiagramDrawer.getCurrentNodeNumber(), ContentType.Attribution ),
                    classDiagramDrawer.getDrawnNodeTextList( classDiagramDrawer.getCurrentNodeNumber(), ContentType.Operation ),
                    classDiagramDrawer.getDrawnNodeContentsBooleanList( classDiagramDrawer.getCurrentNodeNumber(), ContentType.Attribution, ContentType.Indication ),
                    classDiagramDrawer.getDrawnNodeContentsBooleanList( classDiagramDrawer.getCurrentNodeNumber(), ContentType.Operation, ContentType.Indication ) );

            classDiagramScrollPane.setContextMenu( formatContextMenuInCD( contextMenu, nodeDiagram.getNodeType(), mouseX, mouseY ) );
        } else if( currentType == ContentType.Composition ) {
            RelationshipAttribution relation = classDiagramDrawer.searchDrawnEdge( mouseX, mouseY );
            ContextMenu contextMenu = util.getClassContextMenuInCD( relation.getName(), relation.getType() );
            classDiagramScrollPane.setContextMenu( formatContextMenuInCD( contextMenu, relation.getType(), mouseX, mouseY ) );
        }
    }

    /**
     * クラス図キャンバス上での右クリックメニューの各メニューアイテムの動作を整形する。
     * 名前の変更と内容の追加と内容の変更メニューではテキスト入力ダイアログを表示するが、それ以外はメニューアイテムを選択して直後にキャンバスを再描画する。
     *
     * @param contextMenu 右クリックメニューの見た目が整形済みの右クリックメニュー UtilityJavaFXComponentクラスのgetClassContextMenuInCDメソッドで取得したインスタンスを入れる必要がある。
     * @param type 右クリックした要素の種類
     * @return 動作整形済みの右クリックメニュー UtilityJavaFXComponentクラスで整形していないメニューや未分類の要素の種類を{@code contextMenu}や{@code type}に入れた場合は{@code null}を返す。
     */
    private ContextMenu formatContextMenuInCD( ContextMenu contextMenu, ContentType type, double mouseX, double mouseY ) {
        if( type == ContentType.Class ) {
            if (contextMenu.getItems().size() != 5) return null;
            contextMenu = formatClassContextMenuInCD( contextMenu );

        } else if( type == ContentType.Composition ) {
            if (contextMenu.getItems().size() != 2) return null;
            contextMenu = formatCompositionContextMenuInCD( contextMenu, mouseX, mouseY );

        } else {
            return null;
        }

        return contextMenu;
    }

    private ContextMenu formatClassContextMenuInCD( ContextMenu contextMenu ) {
        // クラス名の変更
        contextMenu.getItems().get(0).setOnAction(event -> {
            String className = showChangeClassNameInputDialog(classDiagramDrawer.getNodes().get(classDiagramDrawer.getCurrentNodeNumber()).getNodeText());
            classDiagramDrawer.changeDrawnNodeText(classDiagramDrawer.getCurrentNodeNumber(), ContentType.Title, 0, className);
            classDiagramDrawer.allReDrawCanvas();
        });
        // クラスの削除
        contextMenu.getItems().get(1).setOnAction(event -> {
            classDiagramDrawer.deleteDrawnNode(classDiagramDrawer.getCurrentNodeNumber());
            classDiagramDrawer.allReDrawCanvas();
        });
        // クラスの属性の追加
        ((Menu) contextMenu.getItems().get(3)).getItems().get(0).setOnAction(event -> {
            String addAttribution = showAddClassAttributionInputDialog();
            classDiagramDrawer.addDrawnNodeText(classDiagramDrawer.getCurrentNodeNumber(), ContentType.Attribution, addAttribution);
            classDiagramDrawer.allReDrawCanvas();
        });
        // クラスの操作の追加
        ((Menu) contextMenu.getItems().get(4)).getItems().get(0).setOnAction(event -> {
            String addOperation = showAddClassOperationInputDialog();
            classDiagramDrawer.addDrawnNodeText(classDiagramDrawer.getCurrentNodeNumber(), ContentType.Operation, addOperation);
            classDiagramDrawer.allReDrawCanvas();
        });
        List<String> attributions = classDiagramDrawer.getDrawnNodeTextList(classDiagramDrawer.getCurrentNodeNumber(), ContentType.Attribution);
        List<String> operations = classDiagramDrawer.getDrawnNodeTextList(classDiagramDrawer.getCurrentNodeNumber(), ContentType.Operation);
        // クラスの各属性の変更
        for (int i = 0; i < attributions.size(); i++) {
            int contentNumber = i;
            ((Menu) ((Menu) contextMenu.getItems().get(3)).getItems().get(1)).getItems().get(i).setOnAction(event -> {
                String changedAttribution = showChangeClassAttributionInputDialog(attributions.get(contentNumber));
                classDiagramDrawer.changeDrawnNodeText(classDiagramDrawer.getCurrentNodeNumber(), ContentType.Attribution, contentNumber, changedAttribution);
                classDiagramDrawer.allReDrawCanvas();
            });
        }
        // クラスの各属性の削除
        for (int i = 0; i < attributions.size(); i++) {
            int contentNumber = i;
            ((Menu) ((Menu) contextMenu.getItems().get(3)).getItems().get(2)).getItems().get(i).setOnAction(event -> {
                classDiagramDrawer.deleteDrawnNodeText(classDiagramDrawer.getCurrentNodeNumber(), ContentType.Attribution, contentNumber);
                classDiagramDrawer.allReDrawCanvas();
            });
        }
        // クラスの各属性の表示選択
        for (int i = 0; i < attributions.size(); i++) {
            int contentNumber = i;
            ((Menu) ((Menu) contextMenu.getItems().get(3)).getItems().get(3)).getItems().get(i).setOnAction(event -> {
                classDiagramDrawer.setDrawnNodeContentBoolean(classDiagramDrawer.getCurrentNodeNumber(), ContentType.Attribution, ContentType.Indication, contentNumber,
                        ((CheckMenuItem) ((Menu) ((Menu) contextMenu.getItems().get(3)).getItems().get(3)).getItems().get(contentNumber)).isSelected());
                classDiagramDrawer.allReDrawCanvas();
            });
        }
        // クラスの各操作の変更
        for (int i = 0; i < operations.size(); i++) {
            int contentNumber = i;
            ((Menu) ((Menu) contextMenu.getItems().get(4)).getItems().get(1)).getItems().get(i).setOnAction(event -> {
                String changedOperation = showChangeClassOperationInputDialog(operations.get(contentNumber));
                classDiagramDrawer.changeDrawnNodeText(classDiagramDrawer.getCurrentNodeNumber(), ContentType.Operation, contentNumber, changedOperation);
                classDiagramDrawer.allReDrawCanvas();
            });
        }
        // クラスの各操作の削除
        for (int i = 0; i < operations.size(); i++) {
            int contentNumber = i;
            ((Menu) ((Menu) contextMenu.getItems().get(4)).getItems().get(2)).getItems().get(i).setOnAction(event -> {
                classDiagramDrawer.deleteDrawnNodeText(classDiagramDrawer.getCurrentNodeNumber(), ContentType.Operation, contentNumber);
                classDiagramDrawer.allReDrawCanvas();
            });
        }
        // クラスの各操作の表示選択
        for (int i = 0; i < operations.size(); i++) {
            int contentNumber = i;
            ((Menu) ((Menu) contextMenu.getItems().get(4)).getItems().get(3)).getItems().get(i).setOnAction(event -> {
                classDiagramDrawer.setDrawnNodeContentBoolean(classDiagramDrawer.getCurrentNodeNumber(), ContentType.Operation, ContentType.Indication, contentNumber,
                        ((CheckMenuItem) ((Menu) ((Menu) contextMenu.getItems().get(4)).getItems().get(3)).getItems().get(contentNumber)).isSelected());
                classDiagramDrawer.allReDrawCanvas();
            });
        }

        return contextMenu;
    }

    private ContextMenu formatCompositionContextMenuInCD( ContextMenu contextMenu, double mouseX, double mouseY ) {
        // コンポジション関係の変更
        contextMenu.getItems().get(0).setOnAction( event -> {
            RelationshipAttribution composition = classDiagramDrawer.searchDrawnEdge( mouseX, mouseY );
            String compositionName = showChangeCompositionNameInputDialog( composition.getName() );
            classDiagramDrawer.changeDrawnEdge( mouseX, mouseY, compositionName );
            classDiagramDrawer.allReDrawCanvas();
        } );
        // コンポジション関係の削除
        contextMenu.getItems().get(1).setOnAction( event -> {
            classDiagramDrawer.deleteDrawnEdge( mouseX, mouseY );
            classDiagramDrawer.allReDrawCanvas();
        } );

        return contextMenu;
    }
}
