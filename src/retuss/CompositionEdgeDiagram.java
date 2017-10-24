package retuss;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class CompositionEdgeDiagram {
    GraphicsContext gc;
    List< RelationshipAttribution > compositions = new ArrayList<>();

    public void setGraphicsContext( GraphicsContext gc ) {
        this.gc = gc;
    }

    public void createEdgeText( ContentType type, String text ) {
        compositions.add( new RelationshipAttribution( text ) );
    }
    public void changeEdgeText( ContentType type, int number, String text ) {
        compositions.get( number ).setName( text );
    }
    public void deleteEdgeText( ContentType type, int number ) {
        compositions.remove( number );
    }
    public String getEdgeContentText( ContentType type, int number ) {
        return compositions.get( number ).getName();
    }

    public void setRelationId( ContentType type, int number, int id ) {
        compositions.get( number ).setRelationId( id );
    }

    public int getRelationId( ContentType type, int number ) {
        return compositions.get( number ).getRelationId();
    }

    public void setRelationSourceId( ContentType type, int number, int id ) {
        compositions.get( number ).setRelationSourceId( id );
    }

    public int getRelationSourceId( ContentType type, int number ) {
        return compositions.get( number ).getRelationSourceId();
    }

    public int getCompositionsCount() {
        return compositions.size();
    }

    public void draw( Point2D releasePoint, Point2D releaseSourcePoint ) {
        gc.setStroke( Color.BLACK );
        gc.strokeLine( releaseSourcePoint.getX(), releaseSourcePoint.getY(), releasePoint.getX(), releasePoint.getY() );
    }
}
