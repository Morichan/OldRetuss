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

    public void setGraphicsContext( GraphicsContext gc ) {
        this.gc = gc;
    }

    public void setMouseCoordinates( double x, double y ) {
        mouseX = x;
        mouseY = y;
    }

    public void drawNode( int nodeNumber ) {
        nodes.get( nodeNumber ).setGraphicsContext( gc );
        nodes.get( nodeNumber ).setMouseCoordinates( mouseX, mouseY );
        nodes.get( nodeNumber ).draw();
    }

    public void drawNode( List< Button > buttons ) {
        for( Button button : buttons ) {
            if( button.getText().equals( "Normal" ) ) continue;

            if( button.isDefaultButton() ) {
                int number = setDrawnNode( button );
                drawNode( number );
            }
        }
    }

    private int setDrawnNode( Button button ) {
        if( button.getText().equals( "Class" ) ) {
            ClassNodeDiagram classNodeDiagram = new ClassNodeDiagram();
            nodes.add( classNodeDiagram );
        } else if( button.getText().equals( "Note" ) ) {
            NoteNodeDiagram noteNodeDiagram = new NoteNodeDiagram();
            nodes.add( noteNodeDiagram );
        } else if( button.getText().equals( "Normal" ) ) {
        }

        return nodes.size() - 1;
    }
}
