package retuss;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;

abstract public class NodeDiagram {
    private static int nodeCount = 0;
    protected static GraphicsContext gc = null;

    protected Point2D mouse = Point2D.ZERO;
    protected int nodeId;
    protected String nodeText = "";
    protected String diagramFont = "Consolas";

    abstract public boolean isAlreadyDrawnNode( double x, double y );
    abstract public void draw();
    abstract public void createNodeText( ContentType type, String text );
    abstract public void changeNodeText( ContentType type, int number, String text );
    abstract public void deleteNodeText( ContentType type, int number );
    abstract public String getNodeContentText( ContentType type, int number );
    abstract public List< String > getNodeContents( ContentType type );

    NodeDiagram() {
        nodeId = nodeCount;
        nodeCount++;
    }

    /**
     * 静的カウント変数をリセットする。
     * テストコードで主に用いる。
     */
    void resetNodeCount() {
        nodeCount = 0;
    }

    public void setGraphicsContext( GraphicsContext gc ) {
        this.gc = gc;
    }

    public void setMouseCoordinates( double x, double y ) {
        mouse = new Point2D( x, y );
    }

    public String getNodeText() {
        return nodeText;
    }

    public ContentType getNodeType() {
        return ContentType.Class;
    }

    public int getNodeId() {
        return nodeId;
    }
}
