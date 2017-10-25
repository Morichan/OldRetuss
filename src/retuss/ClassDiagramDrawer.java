package retuss;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ClassDiagramDrawer {
    GraphicsContext gc;
    private List< NodeDiagram > nodes = new ArrayList<>();
    private CompositionEdgeDiagram compositions = new CompositionEdgeDiagram();

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

    public int getCurrentNodeNumber() {
        return currentNodeNumber;
    }

    public void setGraphicsContext( GraphicsContext gc ) {
        this.gc = gc;
        compositions.setGraphicsContext( this.gc );
        drawDiagramCanvasEdge();
    }

    public void setMouseCoordinates( double x, double y ) {
        mouseX = x;
        mouseY = y;
    }

    public void setNodeText( String text ) {
        nodeText = text;
    }

    public void allReDrawNode() {
        gc.clearRect( 0.0, 0.0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight() );

        drawDiagramCanvasEdge();
        allReDrawEdge();
        for( int i = 0; i < nodes.size(); i++ ) {
            drawNode( i );
        }
    }

    public void allReDrawEdge() {
        for( int i = 0; i < compositions.getCompositionsCount(); i++ ) {
            // int relationId = nodes.get( compositions.getRelationId( ContentType.Composition, i ) ).getNodeId();
            // int relationSourceId = nodes.get( compositions.getRelationSourceId( ContentType.Composition, i ) ).getNodeId();
            // Point2D releasePoint = nodes.get( relationId ).getPoint();
            // Point2D releaseSourcePoint = nodes.get( relationSourceId ).getPoint();
            Point2D releasePoint = compositions.getRelationPoint( ContentType.Composition, i );
            Point2D releaseSourcePoint = compositions.getRelationSourcePoint( ContentType.Composition, i );
            compositions.draw( releasePoint, releaseSourcePoint );
        }
    }

    public void createDrawnNode( int number ) {
        if( nodeText.length() <= 0 ) return;

        nodes.get( number ).setGraphicsContext( gc );
        nodes.get( number ).setMouseCoordinates( mouseX, mouseY );
        nodes.get( number ).createNodeText( ContentType.Title, nodeText );
        nodes.get( number ).setChosen( false );
        nodes.get( number ).draw();
    }

    public void drawNode( int number ) {
        nodes.get( number ).draw();
    }

    public void addDrawnNode( List< Button > buttons ) {
        for( Button button : buttons ) {
            if( button.isDefaultButton() ) {
                createDrawnNode( button );
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

    private void createDrawnNode( Button button ) {
        if( nodeText.length() <= 0 ) return;

        if( button.getText().equals( "Class" ) ) {
            ClassNodeDiagram classNodeDiagram = new ClassNodeDiagram();
            nodes.add( classNodeDiagram );
            currentNodeNumber = nodes.size() - 1;
            createDrawnNode( currentNodeNumber );
        } else if( button.getText().equals( "Note" ) ) {
            NoteNodeDiagram noteNodeDiagram = new NoteNodeDiagram();
            nodes.add( noteNodeDiagram );
            currentNodeNumber = nodes.size() - 1;
            createDrawnNode( currentNodeNumber );
        }
    }

    private void createDrawnEdge( Button button, String name, double toMouseX, double toMouseY ) {
        if( nodeText.length() <= 0 ) return;

        if( button.getText().equals( "Composition" ) ) {
            getNodeDiagramId( mouseX, mouseY );
            int fromNodeId = currentNodeNumber;
            getNodeDiagramId( toMouseX, toMouseY );
            int toNodeId = currentNodeNumber;
            compositions.createEdgeText( ContentType.Composition, name );
            compositions.setRelationId( ContentType.Composition, compositions.getCompositionsCount() - 1, toNodeId );
            compositions.setRelationSourceId( ContentType.Composition, compositions.getCompositionsCount() - 1, fromNodeId );
            compositions.setRelationPoint( ContentType.Composition, compositions.getCompositionsCount() - 1, nodes.get( toNodeId ).getPoint() );
            compositions.setRelationSourcePoint( ContentType.Composition, compositions.getCompositionsCount() - 1, nodes.get( fromNodeId ).getPoint() );
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

        return act;
    }

    public boolean hasWaitedCorrectDrawnDiagram( ContentType type, double mouseX, double mouseY ) {
        boolean act = false;

        if( isAlreadyDrawnAnyDiagram( mouseX, mouseY ) ) {
            if( compositions.hasRelationSourceNodeSelected() ) {
                act = true;
                if( type == nowStateType ) {
                    compositions.changeRelationSourceNodeSelectedState();
                    setNodeChosen( currentNodeNumber, false );
                    nowStateType = ContentType.Undefined;
                } else {
                    findNodeDiagram( mouseX, mouseY );
                    setNodeChosen( currentNodeNumber, true );
                    nowStateType = type;
                }
            } else {
                compositions.changeRelationSourceNodeSelectedState();
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
        compositions.resetRelationSourceNodeSelectedState();
    }

    public void setNodeChosen(int number, boolean isChosen ) {
        nodes.get( number ).setChosen( isChosen );
    }

    public ContentType getNowStateType() {
        return nowStateType;
    }

    public int getNodeDiagramId( double mouseX, double mouseY ) {
        int act = -1;

        // if( ! isAlreadyDrawnAnyDiagram( mouseX, mouseY ) ) return act;

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

    public CompositionEdgeDiagram getCompositionEdgeDiagram() {
        return compositions;
    }

    private void drawDiagramCanvasEdge() {
        double space = 5.0;
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();
        gc.setStroke( Color.BLACK );
        gc.strokeRect( space, space, width - space * 2, height - space * 2 );
    }
}
