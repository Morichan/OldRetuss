package retuss;

public class NoteNodeDiagram extends NodeDiagram {

    @Override
    public boolean isAlreadyDrawnNode( double x, double y ) {
        return false;
    }

    @Override
    public String getNodeContentText() {
        return "";
    }

    @Override
    public void draw() {
        gc.fillText( "noteOfCD", mouse.getX(), mouse.getY() );
    }
}
