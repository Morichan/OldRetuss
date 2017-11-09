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
    public void setNodeContentBoolean( ContentType parent, ContentType child, int contentNumber, boolean isChecked ) {}

    @Override
    public List< Boolean > getNodeContentsBoolean(ContentType parent, ContentType type ) { return null; }

    @Override
    public void draw() {
        gc.fillText( "noteOfCD", currentPoint.getX(), currentPoint.getY() );
    }

    @Override
    public void setChosen( boolean isChosen ) {}

    @Override
    public List< String > getNodeContents( ContentType type ) {
        return null;
    }
}
