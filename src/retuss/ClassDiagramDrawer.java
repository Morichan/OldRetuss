package retuss;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class ClassDiagramDrawer {
    GraphicsContext gc;
    private List< NodeDiagram > nodes = new ArrayList<>();

    private double mouseX = 0.0;
    private double mouseY = 0.0;
    private String nodeText;

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

    public void drawNode( NodeDiagram nodeDiagram ) {
        nodeDiagram.setGraphicsContext(gc);
        nodeDiagram.setMouseCoordinates(mouseX, mouseY);
        nodeDiagram.setNodeText(nodeText);
        nodeDiagram.draw();
    }

    public void addDrawnNode( List< Button > buttons ) {
        for( Button button : buttons ) {
            if( button.isDefaultButton() ) {
                setDrawnNode( button );
            }
        }
    }

    private void setDrawnNode( Button button ) {
        if( button.getText().equals( "Class" ) ) {
            ClassNodeDiagram classNodeDiagram = new ClassNodeDiagram();
            nodes.add( classNodeDiagram );
            drawNode( classNodeDiagram );
        } else if( button.getText().equals( "Note" ) ) {
            NoteNodeDiagram noteNodeDiagram = new NoteNodeDiagram();
            nodes.add( noteNodeDiagram );
            drawNode( noteNodeDiagram );
        } else if( button.getText().equals( "Normal" ) ) {
        }
    }
}
