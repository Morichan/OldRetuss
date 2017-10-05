package retuss;

public class NoteNodeDiagram extends NodeDiagram {

    @Override
    public boolean isAlreadyDrawnNode( double x, double y ) {
        return false;
    }

    @Override
    public void draw() {
        gc.fillText( "noteOfCD", mouse.getX(), mouse.getY() );
    }
}
