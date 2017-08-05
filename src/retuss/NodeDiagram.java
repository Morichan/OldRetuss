package retuss;

import javafx.scene.canvas.GraphicsContext;

abstract public class NodeDiagram {
    protected static GraphicsContext gc = null;

    protected double mouseX = 0.0;
    protected double mouseY = 0.0;
    protected String nodeText = "";

    abstract public void draw();

    public void setGraphicsContext( GraphicsContext gc ) {
        this.gc = gc;
    }

    public void setMouseCoordinates( double x, double y ) {
        mouseX = x;
        mouseY = y;
    }

    public void setNodeText( String text ) {
        nodeText = text;
    }
}
