package retuss;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> クラス図描画クラス </p>
 *
 * <p>
 *     このクラスは、クラス図を描画する際にキャンバスの操作などを行うクラスです。
 * </p>
 */
public class ClassDiagramDrawer {
    GraphicsContext gc;
    private List< NodeDiagram > nodes = new ArrayList<>();
    private EdgeDiagram relations = new EdgeDiagram();

    private int currentNodeNumber = -1;
    private double mouseX = 0.0;
    private double mouseY = 0.0;
    private String nodeText;
    private ContentType nowStateType = ContentType.Undefined;

    /**
     * 生成したクラス図のノードのリストを返す。
     * テストコードで主に用いる。
     * @return 生成したノードリスト sizeが0の可能性あり
     */
    List< NodeDiagram > getNodes() {
        return nodes;
    }

    /**
     * 操作中のノード番号を返す。
     *
     * @return 操作中のノード番号 何も操作していない場合は -1 を返す。
     */
    public int getCurrentNodeNumber() {
        return currentNodeNumber;
    }

    /**
     * {@link Controller} クラスから {@link javafx.scene.canvas.Canvas} クラスが持つ {@link GraphicsContext} クラスのインスタンスを受け取り、 {@link EdgeDiagram} クラスのインスタンスに渡す。
     * また、キャンバスの縁を描画する。
     *
     * @param gc {@link Controller} クラスから受け取るグラフィックスコンテキスト
     */
    public void setGraphicsContext( GraphicsContext gc ) {
        this.gc = gc;
        relations.setGraphicsContext( this.gc );
        drawDiagramCanvasEdge();
    }

    /**
     * クラス図キャンバスにおいて操作しているマウスの位置を受け取る。
     *
     * @param x マウスのX軸
     * @param y マウスのY軸
     */
    public void setMouseCoordinates( double x, double y ) {
        mouseX = x;
        mouseY = y;
    }

    /**
     * クラス図においてダイアログなどで取得したテキストを受け取る。
     *
     * @param text 名前や内容の文字列
     */
    public void setNodeText( String text ) {
        nodeText = text;
    }

    /**
     * クラス図キャンバスにおける全てのノード、エッジおよびキャンバスの縁を描画する。
     * 上書きするのを防ぐために、最初にキャンバスをまっさらにする。
     */
    public void allReDrawCanvas() {
        gc.clearRect( 0.0, 0.0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight() );

        drawDiagramCanvasEdge();
        allReDrawEdge();
        allReDrawNode();
    }

    /**
     * クラス図キャンバスにおける全てのノードを描画する。
     */
    public void allReDrawNode() {
        for( int i = 0; i < nodes.size(); i++ ) {
            drawNode( i );
        }
    }

    /**
     * クラス図キャンバスにおける全てのエッジ（関係）を描画する。
     */
    public void allReDrawEdge() {
        for(int i = 0; i < relations.getCompositionsCount(); i++ ) {
            int relationId = nodes.get( relations.getRelationId( ContentType.Composition, i ) ).getNodeId();
            int relationSourceId = nodes.get( relations.getRelationSourceId( ContentType.Composition, i ) ).getNodeId();
            double relationWidth = nodes.get( relationId ).getWidth();
            double relationHeight = nodes.get( relationId ).getHeight();
            double relationSourceWidth = nodes.get( relationSourceId ).getWidth();
            double relationSourceHeight = nodes.get( relationSourceId ).getHeight();

            relations.draw( relationWidth, relationHeight, relationSourceWidth, relationSourceHeight, i );
        }
    }

    /**
     * クラス図キャンバスにおけるノードの初期化を行う。
     *
     * @param number
     */
    public void setupDrawnNode( int number ) {
        if( nodeText.length() <= 0 ) return;

        nodes.get( number ).setGraphicsContext( gc );
        nodes.get( number ).setMouseCoordinates( mouseX, mouseY );
        nodes.get( number ).createNodeText( ContentType.Title, nodeText );
        nodes.get( number ).setChosen( false );
    }

    public void drawNode( int number ) {
        nodes.get( number ).draw();
    }

    public double getNodeWidth( int number ) {
        return nodes.get( number ).getWidth();
    }
    public double getNodeHeight( int number ) {
        return nodes.get( number ).getHeight();
    }

    public void addDrawnNode( List<Button> buttons ) {
        for( Button button : buttons ) {
            if( button.isDefaultButton() ) {
                setupDrawnNode( button );
                break;
            }
        }
    }

    public void addDrawnEdge( List< Button > buttons, String name, double toMouseX, double toMouseY ) {
        for( Button button : buttons ) {
            if( button.isDefaultButton() ) {
                createDrawnEdge( button, name, toMouseX, toMouseY );
                break;
            }
        }
    }

    public List< String > getDrawnNodeTextList( int nodeNumber, ContentType type ) {
        return nodes.get( nodeNumber ).getNodeContents( type );
    }

    public void setDrawnNodeContentBoolean( int nodeNumber, ContentType parent, ContentType child, int contentNumber, boolean isChecked ) {
        nodes.get( nodeNumber ).setNodeContentBoolean( parent, child, contentNumber, isChecked );
    }

    public List< Boolean > getDrawnNodeContentsBooleanList( int nodeNumber, ContentType parent, ContentType child ) {
        return nodes.get( nodeNumber ).getNodeContentsBoolean( parent, child );
    }

    public void addDrawnNodeText( int number, ContentType type, String text ) {
        if( text.length() <= 0 ) return;
        nodes.get( number ).createNodeText( type, text );
    }

    public void changeDrawnNodeText( int nodeNumber, ContentType type, int contentNumber, String text ) {
        if( text.length() <= 0 ) return;
        nodes.get( nodeNumber ).changeNodeText( type, contentNumber, text );
    }

    public void deleteDrawnNode( int number ) {
        nodes.remove( number );
    }

    public void deleteDrawnNodeText( int nodeNumber, ContentType type, int contentNumber ) {
        nodes.get( nodeNumber ).deleteNodeText( type, contentNumber );
    }

    private void setupDrawnNode( Button button ) {
        if( nodeText.length() <= 0 ) return;

        if( button.getText().equals( "Class" ) ) {
            ClassNodeDiagram classNodeDiagram = new ClassNodeDiagram();
            nodes.add( classNodeDiagram );
            currentNodeNumber = nodes.size() - 1;
            setupDrawnNode( currentNodeNumber );
        } else if( button.getText().equals( "Note" ) ) {
            NoteNodeDiagram noteNodeDiagram = new NoteNodeDiagram();
            nodes.add( noteNodeDiagram );
            currentNodeNumber = nodes.size() - 1;
            setupDrawnNode( currentNodeNumber );
        }
    }

    private void createDrawnEdge( Button button, String name, double toMouseX, double toMouseY ) {
        if( nodeText.length() <= 0 ) return;

        if( button.getText().equals( "Composition" ) ) {
            getNodeDiagramId( mouseX, mouseY );
            int fromNodeId = currentNodeNumber;
            getNodeDiagramId( toMouseX, toMouseY );
            int toNodeId = currentNodeNumber;
            relations.createEdgeText( ContentType.Composition, name );
            relations.setRelationId( ContentType.Composition, relations.getCompositionsCount() - 1, toNodeId );
            relations.setRelationSourceId( ContentType.Composition, relations.getCompositionsCount() - 1, fromNodeId );
            relations.setRelationPoint( ContentType.Composition, relations.getCompositionsCount() - 1, nodes.get( toNodeId ).getPoint() );
            relations.setRelationSourcePoint( ContentType.Composition, relations.getCompositionsCount() - 1, nodes.get( fromNodeId ).getPoint() );
        }
    }

    public NodeDiagram findNodeDiagram( double mouseX, double mouseY ) {
        int number = getNodeDiagramId( mouseX, mouseY );

        if( number == -1 ) return null;
        else return nodes.get( currentNodeNumber );
    }

    public boolean isAlreadyDrawnAnyDiagram( double mouseX, double mouseY ) {
        boolean act = false;

        for( NodeDiagram nodeDiagram: nodes ) {
            if( nodeDiagram.isAlreadyDrawnNode( mouseX, mouseY ) ) {
                act = true;
                break;
            }
        }
        for(int i = 0; i < relations.getCompositionsCount(); i++ ) {
            if( relations.isAlreadyDrawnAnyEdge( ContentType.Composition, i, new Point2D( mouseX, mouseY ) ) ) {
                act = true;
                break;
            }
        }

        return act;
    }

    public RelationshipAttribution searchDrawnEdge( double mouseX, double mouseY ) {
        RelationshipAttribution relation = relations.searchCurrentRelation( new Point2D( mouseX, mouseY ) );
        return relation;
    }

    public void changeDrawnEdge( double mouseX, double mouseY, String content ) {
        relations.changeCurrentRelation( new Point2D( mouseX, mouseY ), content );
    }

    public void deleteDrawnEdge( double mouseX, double mouseY ) {
        relations.deleteCurrentRelation( new Point2D( mouseX, mouseY ) );
    }

    public ContentType searchDrawnAnyDiagramType( double mouseX, double mouseY ) {
        ContentType type = ContentType.Undefined;

        for( NodeDiagram nodeDiagram: nodes ) {
            if( nodeDiagram.isAlreadyDrawnNode( mouseX, mouseY ) ) {
                type = nodeDiagram.getNodeType();
                break;
            }
        }
        for(int i = 0; i < relations.getCompositionsCount(); i++ ) {
            if( relations.isAlreadyDrawnAnyEdge( ContentType.Composition, i, new Point2D( mouseX, mouseY ) ) ) {
                type = relations.getContentType( i );
                break;
            }
        }

        return type;
    }

    public boolean hasWaitedCorrectDrawnDiagram( ContentType type, double mouseX, double mouseY ) {
        boolean act = false;

        if( isAlreadyDrawnAnyDiagram( mouseX, mouseY ) ) {
            if( relations.hasRelationSourceNodeSelected() ) {
                act = true;
                if( type == nowStateType ) {
                    relations.changeRelationSourceNodeSelectedState();
                    setNodeChosen( currentNodeNumber, false );
                    nowStateType = ContentType.Undefined;
                } else {
                    findNodeDiagram( mouseX, mouseY );
                    setNodeChosen( currentNodeNumber, true );
                    nowStateType = type;
                }
            } else {
                relations.changeRelationSourceNodeSelectedState();
                findNodeDiagram( mouseX, mouseY );
                setNodeChosen( currentNodeNumber, true );
                nowStateType = type;
            }
        } else {
            nowStateType = ContentType.Undefined;
        }

        return act;
    }

    public void resetNodeChosen( int number ) {
        nodes.get( number ).setChosen( false );
        nowStateType = ContentType.Undefined;
        relations.resetRelationSourceNodeSelectedState();
    }

    public void setNodeChosen(int number, boolean isChosen ) {
        nodes.get( number ).setChosen( isChosen );
    }

    public ContentType getNowStateType() {
        return nowStateType;
    }

    public int getNodeDiagramId( double mouseX, double mouseY ) {
        int act = -1;

        // 重なっているノードの内1番上に描画しているノードはnodesリストの1番後半に存在するため、1番上に描画しているノードを取るためには尻尾から見なければならない。
        for( int i = nodes.size() - 1; i >= 0; i-- ) {
            if( nodes.get( i ).isAlreadyDrawnNode( mouseX, mouseY ) ) {
                act = nodes.get( i ).getNodeId();
                currentNodeNumber = i;
                break;
            }
        }

        return act;
    }

    public EdgeDiagram getEdgeDiagram() {
        return relations;
    }

    private void drawDiagramCanvasEdge() {
        double space = 5.0;
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();
        gc.setStroke( Color.BLACK );
        gc.strokeRect( space, space, width - space * 2, height - space * 2 );
    }
}
