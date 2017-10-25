package retuss;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class CompositionEdgeDiagram {
    GraphicsContext gc;
    private List< RelationshipAttribution > compositions = new ArrayList<>();
    private boolean hasRelationSourceNodeSelected = false;

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

    public void setRelationPoint( ContentType type, int number, Point2D point ) {
        compositions.get( number ).setRelationPoint( point );
    }

    public Point2D getRelationPoint( ContentType type, int number ) {
        return compositions.get( number ).getRelationPoint();
    }

    public void setRelationSourcePoint( ContentType type, int number, Point2D point ) {
        compositions.get( number ).setRelationSourcePoint( point );
    }

    public Point2D getRelationSourcePoint( ContentType type, int number ) {
        return compositions.get( number ).getRelationSourcePoint();
    }

    public int getCompositionsCount() {
        return compositions.size();
    }

    boolean hasRelationSourceNodeSelected() {
        return hasRelationSourceNodeSelected;
    }

    public void changeRelationSourceNodeSelectedState() {
        hasRelationSourceNodeSelected = ! hasRelationSourceNodeSelected;
    }

    public void resetRelationSourceNodeSelectedState() {
        hasRelationSourceNodeSelected = false;
    }

    public boolean isAlreadyDrawnAnyEdge( ContentType type, int number, Point2D mousePoint ) {
        boolean isAlreadyDrawnAnyEdge = false;
        Point2D relationPoint = compositions.get( number ).getRelationPoint();
        Point2D relationSourcePoint = compositions.get( number ).getRelationSourcePoint();

        if( relationPoint.getX() - getRelationMarginLength( ContentType.Composition ) < mousePoint.getX() &&
                mousePoint.getX() < relationPoint.getX() + getRelationMarginLength( ContentType.Composition ) ) isAlreadyDrawnAnyEdge = true;

        if( relationPoint.getY() - getRelationMarginLength( ContentType.Composition ) < mousePoint.getY() &&
                mousePoint.getY() < relationPoint.getY() + getRelationMarginLength( ContentType.Composition ) ) isAlreadyDrawnAnyEdge = true;

        return isAlreadyDrawnAnyEdge;
    }

    public void draw( Point2D releasePoint, Point2D releaseSourcePoint ) {
        gc.setStroke( Color.BLACK );
        gc.strokeLine( releaseSourcePoint.getX(), releaseSourcePoint.getY(), releasePoint.getX(), releasePoint.getY() );
    }

    private double getRelationMarginLength( ContentType type ) {
        return 5.0;
    }
}
