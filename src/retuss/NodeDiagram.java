package retuss;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

abstract public class NodeDiagram {
    private static int nodeCount = 0;
    protected static GraphicsContext gc = null;

    protected Point2D mouse = Point2D.ZERO;
    protected int nodeId;
    protected String nodeText = "";
    protected String diagramFont = "Consolas";

    abstract public boolean isAlreadyDrawnNode( double x, double y );
    abstract public void draw();

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

    public void setNodeText( String text ) {
        nodeText = text;
    }

    public String getNodeText() {
        return nodeText;
    }

    public String getNodeType() {
        return "クラス";
    }

    public int getNodeId() {
        return nodeId;
    }
}
