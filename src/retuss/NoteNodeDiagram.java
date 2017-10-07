package retuss;

import java.util.List;

public class NoteNodeDiagram extends NodeDiagram {

    @Override
    public boolean isAlreadyDrawnNode( double x, double y ) {
        return false;
    }

    @Override
    public String getNodeContentText( ContentType type, int number ) {
        return "";
    }

    @Override
    public void createNodeText( ContentType type, String text ) {
    }

    @Override
    public void changeNodeText( ContentType type, int number, String text ) {}

    @Override
    public void deleteNodeText( ContentType type, int number ) {
    }

    @Override
    public void draw() {
        gc.fillText( "noteOfCD", mouse.getX(), mouse.getY() );
    }

    @Override
    public List< String > getNodeContents( ContentType type ) {
        return null;
    }
}
