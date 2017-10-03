package retuss;

import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class ClassDiagramCanvas {
    private List< NodeDiagram > Nodes = new ArrayList<>();

    public void drawNode( List< Button > buttons ) {
        for( Button button : buttons ) {
            if( button.isDefaultButton() ) {
                selectDrawnNode( button );
            }
        }
    }

    private void selectDrawnNode( Button button ) {
        if( button.getText().equals( "Class" ) ) {
            ClassNodeDiagram classNodeDiagram = new ClassNodeDiagram();
            classNodeDiagram.draw();
            Nodes.add(classNodeDiagram);
        } else if( button.getText().equals( "Note" ) ) {
            NoteNodeDiagram noteNodeDiagram = new NoteNodeDiagram();
            noteNodeDiagram.draw();
            Nodes.add( noteNodeDiagram );
        }
    }
}
