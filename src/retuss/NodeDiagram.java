package retuss;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

abstract public class NodeDiagram {
    protected static GraphicsContext gc = null;

    protected Point2D mouse = Point2D.ZERO;
    protected String nodeText = "";
    protected String diagramFont = "Consolas";

    abstract public void draw();

    public void setGraphicsContext( GraphicsContext gc ) {
        this.gc = gc;
    }

    public void setMouseCoordinates( double x, double y ) {
        mouse = new Point2D( x, y );
    }

    public void setNodeText( String text ) {
        nodeText = text;
    }
}
