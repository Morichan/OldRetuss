package retuss;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static jdk.nashorn.internal.objects.Global.Infinity;

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

    public List< Point2D > createOneEdgeQuadrangleWithMargin( double margin, Point2D startEdge, Point2D endEdge ) {
        List< Point2D > edgePolygon = new ArrayList<>();

        double inclination = calculateNormalLineInclination( startEdge, endEdge );
        int sign = 1;

        if( inclination == Infinity ) {
            if( startEdge.getX() > endEdge.getX() ) sign = -1;
            edgePolygon.add( new Point2D( startEdge.getX(), startEdge.getY() - margin * sign ) );
            edgePolygon.add( new Point2D( endEdge.getX(), endEdge.getY() - margin * sign ) );
            edgePolygon.add( new Point2D( endEdge.getX(), endEdge.getY() + margin * sign ) );
            edgePolygon.add( new Point2D( startEdge.getX(), startEdge.getY() + margin * sign ) );
        } else if( inclination == 0.0 ) {
            if( startEdge.getY() < endEdge.getY() ) sign = -1;
            edgePolygon.add( new Point2D( startEdge.getX() - margin * sign, startEdge.getY() ) );
            edgePolygon.add( new Point2D( endEdge.getX() - margin * sign, endEdge.getY() ) );
            edgePolygon.add( new Point2D( endEdge.getX() + margin * sign, endEdge.getY() ) );
            edgePolygon.add( new Point2D( startEdge.getX() + margin * sign, startEdge.getY() ) );
        } else {

        }

        return edgePolygon;
    }

    public void draw( Point2D releasePoint, Point2D releaseSourcePoint ) {
        gc.setStroke( Color.BLACK );
        gc.strokeLine( releaseSourcePoint.getX(), releaseSourcePoint.getY(), releasePoint.getX(), releasePoint.getY() );
    }

    private double getRelationMarginLength( ContentType type ) {
        return 2.0;
    }

    /**
     * 直線における法線の傾きを計算する。
     *
     * @param startPoint 直線における開始ポイント
     * @param endPoint 直線における終了ポイント
     * @return 法線の傾き {@code startPoint} と {@code endPoint} のY軸の値が等しい場合に限り {@code Infinity} を返す。
     */
    public double calculateNormalLineInclination( Point2D startPoint, Point2D endPoint ) {
        double inclination;
        double normalLineInclination = 0.0;
        double width = startPoint.getX() - endPoint.getX();
        double height = startPoint.getY() - endPoint.getY();

        if( width != 0.0 ) {
            inclination = height / width;
        } else {
            inclination = Infinity;
        }

        if( inclination != Infinity && inclination != 0.0 ) {
            normalLineInclination = 1 / inclination;
        } else if( inclination == Infinity ) {
            normalLineInclination = 0.0;
        } else if( inclination == 0.0 ) {
            normalLineInclination = Infinity;
        }

        return normalLineInclination;
    }

    public List< Double > getActualEdgeAndAngle( double fromX, double fromY, double toX, double toY, double startX, double startY, double width, double height ) {
        List< Double > actual = new ArrayList<>();

        double angle = Math.acos( ( fromX - toX ) / Math.sqrt( ( fromX - toX ) * ( fromX - toX ) + ( fromY - toY ) * ( fromY - toY ) ) );
        double actualEdgeX = ( height/2 * ( toX - fromX ) ) / ( toY - fromY );
        double actualEdgeY = ( width/2 * ( toY - fromY ) ) / ( toX - fromX );
        if( Double.isInfinite( actualEdgeX ) ) {
            if( fromX > toX ) {
                actualEdgeX = fromX - width/2;
                actualEdgeY = fromY;
            } else {
                actualEdgeX = fromX + width/2;
                actualEdgeY = fromY;
            }
        }

        // 最初のノードが次のノードより上に位置している場合
        if( fromY < toY ) {
            if( actualEdgeX > width/2 || -actualEdgeX > width/2 ) {
                if( actualEdgeX > 0 ) {
                    actualEdgeX = startX + width;
                    actualEdgeY += fromY;
                } else {
                    actualEdgeX = startX;
                    actualEdgeY = fromY - actualEdgeY;
                }
            } else {
                actualEdgeX += startX + width/2;
                actualEdgeY = startY + height;
            }
            angle = 180 - Math.toDegrees( angle );

        } else if( fromY > toY ) {
            if( actualEdgeX > width/2 || -actualEdgeX > width/2 ) {
                if( actualEdgeX > 0 ) {
                    actualEdgeX = startX;
                    actualEdgeY = fromY - actualEdgeY;
                } else {
                    actualEdgeX = startX + width;
                    actualEdgeY += fromY;
                }
            } else {
                actualEdgeX = startX + width/2 - actualEdgeX;
                actualEdgeY = startY;
            }
            angle = 180 + Math.toDegrees( angle );

        } else {
            angle = 180 - Math.toDegrees( angle ); // 2つのノードが真横に存在している場合 angle = 180 or 360;
        }

        actual.add( actualEdgeX );
        actual.add( actualEdgeY );
        actual.add( angle );
        return actual;
    }
}
