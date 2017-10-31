package retuss;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static jdk.nashorn.internal.objects.Global.Infinity;
import static jdk.nashorn.internal.objects.Global.exit;

public class CompositionEdgeDiagram {
    GraphicsContext gc;
    private List< RelationshipAttribution > compositions = new ArrayList<>();
    private boolean hasRelationSourceNodeSelected = false;
    private UtilityJavaFXComponent util = new UtilityJavaFXComponent();

    public void setGraphicsContext( GraphicsContext gc ) {
        this.gc = gc;
    }

    public void createEdgeText( ContentType type, String text ) {
        compositions.add( new RelationshipAttribution( text ) );
        compositions.get( compositions.size() - 1 ).setType( ContentType.Composition );
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

    public ContentType getContentType( int number ) {
        return compositions.get( number ).getType();
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

    public RelationshipAttribution searchCurrentRelation( Point2D mousePoint ) {
        RelationshipAttribution content = null;
        int number = searchCurrentRelationNumber( mousePoint );
        if( number > -1 ) content = compositions.get( number );
        return content;
    }

    public void changeCurrentRelation( Point2D mousePoint, String content ) {
        int number = searchCurrentRelationNumber( mousePoint );
        if( number > -1 ) changeEdgeText( compositions.get( number ).getType(), number, content );
    }

    public void deleteCurrentRelation( Point2D mousePoint ) {
        int number = searchCurrentRelationNumber( mousePoint );
        if( number > -1 ) deleteEdgeText( compositions.get( number ).getType(), number );
    }

    public int searchCurrentRelationNumber( Point2D mousePoint ) {
        int number = -1;

        // 重なっているエッジの内1番上に描画しているエッジはcompositionsリストの1番後半に存在するため、1番上に描画しているエッジを取るためには尻尾から見なければならない。
        for( int i = compositions.size() - 1; i >= 0; i-- ) {
            if( isAlreadyDrawnAnyEdge( compositions.get( i ).getType(), i, mousePoint ) ) {
                number = i;
                break;
            }
        }

        return number;
    }

    public boolean isAlreadyDrawnAnyEdge( ContentType type, int number, Point2D mousePoint ) {
        boolean isAlreadyDrawnAnyEdge = false;
        Point2D relationPoint = compositions.get( number ).getRelationPoint();
        Point2D relationSourcePoint = compositions.get( number ).getRelationSourcePoint();

        List< Point2D > edgePolygon = createOneEdgeQuadrangleWithMargin( getRelationMarginLength( ContentType.Composition ), relationPoint, relationSourcePoint );
        if( util.isInsidePointFromPolygonUsingWNA( edgePolygon, mousePoint ) ) isAlreadyDrawnAnyEdge = true;

        return isAlreadyDrawnAnyEdge;
    }

    public List< Point2D > createOneEdgeQuadrangleWithMargin( double margin, Point2D startEdge, Point2D endEdge ) {
        List< Point2D > edgePolygon = new ArrayList<>();

        // 傾き
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
            // 切片
            double intercept = startEdge.getY() / ( inclination * startEdge.getX() );
            double radian = Math.atan( inclination );
            double xAxisLength = margin * Math.cos( radian );
            double yAxisLength = inclination * xAxisLength;

            // ( 切片が0超 && 終点が始点より大きい ) の排他的論理和
            if( ( intercept < 0 && startEdge.getX() < endEdge.getX() ) ||
                    ( ! (intercept < 0) && ! (startEdge.getX() < endEdge.getX() ) ) ) sign = -1;

            edgePolygon.add( new Point2D( startEdge.getX() + xAxisLength * sign, startEdge.getY() - yAxisLength * sign ) );
            edgePolygon.add( new Point2D( endEdge.getX() + xAxisLength * sign, endEdge.getY() - yAxisLength * sign ) );
            edgePolygon.add( new Point2D( endEdge.getX() - xAxisLength * sign, endEdge.getY() + yAxisLength * sign ) );
            edgePolygon.add( new Point2D( startEdge.getX() - xAxisLength * sign, startEdge.getY() + yAxisLength * sign ) );
        }

        return edgePolygon;
    }

    public void draw( Point2D releasePoint, Point2D releaseSourcePoint ) {
        gc.setStroke( Color.BLACK );
        gc.strokeLine( releaseSourcePoint.getX(), releaseSourcePoint.getY(), releasePoint.getX(), releasePoint.getY() );
    }

    private double getRelationMarginLength( ContentType type ) {
        return 10.0;
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
}
