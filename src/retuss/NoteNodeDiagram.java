package retuss;

public class NoteNodeDiagram extends NodeDiagram {

    @Override
    public void draw() {
        gc.fillText( "noteOfCD", mouseX, mouseY );
    }
}
