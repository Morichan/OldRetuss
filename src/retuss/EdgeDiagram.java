package retuss;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class EdgeDiagram {
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

        if( Double.isInfinite( inclination ) ) {
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

            // ( 切片が0未満 xor 終点のX軸が始点のX軸より大きい )
            if( ( intercept < 0 && startEdge.getX() < endEdge.getX() ) ||
                    ( ! (intercept < 0) && ! (startEdge.getX() < endEdge.getX() ) ) ) sign = -1;

            edgePolygon.add( new Point2D( startEdge.getX() + xAxisLength * sign, startEdge.getY() - yAxisLength * sign ) );
            edgePolygon.add( new Point2D( endEdge.getX() + xAxisLength * sign, endEdge.getY() - yAxisLength * sign ) );
            edgePolygon.add( new Point2D( endEdge.getX() - xAxisLength * sign, endEdge.getY() + yAxisLength * sign ) );
            edgePolygon.add( new Point2D( startEdge.getX() - xAxisLength * sign, startEdge.getY() + yAxisLength * sign ) );
        }

        return edgePolygon;
    }

    public void draw( double relationWidth, double relationHeight, double relationSourceWidth, double relationSourceHeight, int number ) {
        Point2D relationPoint = getRelationPoint( ContentType.Composition, number );
        Point2D relationSourcePoint = getRelationSourcePoint( ContentType.Composition, number );
        Point2D relationIntersectPoint = calculateIntersectionPointLineAndEndNodeSide( relationSourcePoint, relationPoint, relationWidth, relationHeight );
        Point2D relationSourceIntersectPoint = calculateIntersectionPointLineAndEndNodeSide( relationPoint, relationSourcePoint, relationSourceWidth, relationSourceHeight );

        drawEdge( relationIntersectPoint, relationSourceIntersectPoint, number );
    }

    private void drawEdge( Point2D relationIntersectPoint, Point2D relationSourceIntersectPoint, int number ) {
        drawLine( relationIntersectPoint, relationSourceIntersectPoint );
        drawEdgeUmbrella( relationIntersectPoint, relationSourceIntersectPoint, number );
        drawEdgeName( relationIntersectPoint, relationSourceIntersectPoint, number );
    }

    private void drawLine( Point2D relationPoint, Point2D relationSourcePoint ) {
        gc.setStroke( Color.BLACK );
        gc.strokeLine( relationSourcePoint.getX(), relationSourcePoint.getY(), relationPoint.getX(), relationPoint.getY() );
    }

    private void drawEdgeUmbrella( Point2D relationIntersectPoint, Point2D relationSourceIntersectPoint, int number ) {
        ContentType type = getContentType( number );

        if( type == ContentType.Composition ) {
            drawEdgeNavigation( relationIntersectPoint, relationSourceIntersectPoint );
            drawEdgeComposition( relationIntersectPoint, relationSourceIntersectPoint );
        }
    }

    private void drawEdgeName( Point2D relationIntersectPoint, Point2D relationSourceIntersectPoint, int number ) {
        double angle = calculateDegreeFromStart( relationIntersectPoint, relationSourceIntersectPoint );
        double length = 20.0;
        Point2D fontPoint = calculateUmbrellaPoint( relationIntersectPoint, angle - 20.0, length );
        Text text = new Text( getEdgeContentText( ContentType.Composition, number ) );
        text.setFont( Font.font( "Consolas" , FontWeight.LIGHT, 15 ) );
        gc.setFill( Color.BLACK );
        gc.setFont( text.getFont() );
        gc.setTextAlign( TextAlignment.LEFT );
        gc.fillText( text.getText(), fontPoint.getX(), fontPoint.getY() );
    }

    private void drawEdgeNavigation( Point2D relationIntersectPoint, Point2D relationSourceIntersectPoint ) {
        double angle = calculateDegreeFromStart( relationIntersectPoint, relationSourceIntersectPoint );
        double length = 20.0;
        Point2D firstNavigationPoint = calculateUmbrellaPoint( relationIntersectPoint, angle - 20.0, length );
        Point2D secondNavigationPoint = calculateUmbrellaPoint( relationIntersectPoint, angle + 20.0, length );

        gc.strokeLine( relationIntersectPoint.getX(), relationIntersectPoint.getY(), firstNavigationPoint.getX(), firstNavigationPoint.getY() );
        gc.strokeLine( relationIntersectPoint.getX(), relationIntersectPoint.getY(), secondNavigationPoint.getX(), secondNavigationPoint.getY() );
    }

    private void drawEdgeComposition( Point2D relationIntersectPoint, Point2D relationSourceIntersectPoint ) {
        List< Point2D > compositionPoints = calculateEdgeCompositionPoints( relationIntersectPoint, relationSourceIntersectPoint );
        double[] xPoints = new double[ 5 ];
        double[] yPoints = new double[ 5 ];

        for( int i = 0; i < compositionPoints.size(); i++ ) {
            xPoints[ i ] = compositionPoints.get( i ).getX();
            yPoints[ i ] = compositionPoints.get( i ).getY();
        }

        gc.setFill( Color.BLACK );
        gc.fillPolygon( xPoints, yPoints, 5 );
        gc.setStroke( Color.BLACK );
        gc.strokePolygon( xPoints, yPoints, 5 );
    }

    private List< Point2D > calculateEdgeCompositionPoints( Point2D relationIntersectPoint, Point2D relationSourceIntersectPoint ) {
        double angle = calculateDegreeFromStart( relationSourceIntersectPoint, relationIntersectPoint ); // 関係の始点における角度
        double length = 15.0;
        List< Point2D > compositionPoints = new ArrayList<>();

        compositionPoints.add( new Point2D( relationSourceIntersectPoint.getX(), relationSourceIntersectPoint.getY() ) );
        compositionPoints.add( calculateUmbrellaPoint( compositionPoints.get( 0 ), angle + 20.0, length ) );
        compositionPoints.add( calculateUmbrellaPoint( compositionPoints.get( 1 ), angle - 20.0, length ) );
        compositionPoints.add( calculateUmbrellaPoint( compositionPoints.get( 0 ), angle - 20.0, length ) );
        compositionPoints.add( new Point2D( relationSourceIntersectPoint.getX(), relationSourceIntersectPoint.getY() ) );

        return compositionPoints;
    }

    private Point2D calculateUmbrellaPoint( Point2D intersectionPoint, double angle, double length ) {
        Point2D point = new Point2D(
                calculateUmbrellaTipX( length, angle, intersectionPoint.getX() ),
                calculateUmbrellaTipY( length, angle, intersectionPoint.getY() )
        );
        return point;
    }
    private double calculateUmbrellaTipX( double length, double angle, double actualX ) {
        return length * Math.cos( Math.toRadians( angle ) ) + actualX;
    }
    private double calculateUmbrellaTipY( double length, double angle, double actualY ) {
        return length * Math.sin( Math.toRadians( angle ) ) + actualY;
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
        double inclination = calculateInclination( startPoint, endPoint );
        double normalLineInclination = 0.0;

        if( ! Double.isInfinite( inclination ) && inclination != 0.0 ) {
            normalLineInclination = 1 / inclination;
        } else if( Double.isInfinite( inclination ) ) {
            normalLineInclination = 0.0;
        } else if( inclination == 0.0 ) {
            normalLineInclination = Double.POSITIVE_INFINITY;
        }

        return normalLineInclination;
    }

    public double calculateDegreeFromStart( Point2D startPoint, Point2D endPoint ) {
        double inclination = Math.acos( ( startPoint.getX() - endPoint.getX() )
                / Math.sqrt( ( startPoint.getX() - endPoint.getX() ) * ( startPoint.getX() - endPoint.getX() ) + ( startPoint.getY() - endPoint.getY() ) * ( startPoint.getY() - endPoint.getY() ) ) );
        double angle = Math.toDegrees( inclination );

        if( startPoint.getY() < endPoint.getY() ) angle = 180 - angle; // 開始ノードが終了ノードより上に位置している場合
        else if( startPoint.getY() > endPoint.getY() ) angle = 180 + angle;
        else angle = 180 - angle; // 2つのノードが真横に存在している場合 angle == 180 or 360

        return angle;
    }

    public Point2D calculateIntersectionPointLineAndEndNodeSide( Point2D startPoint, Point2D endPoint, double endNodeWidth, double endNodeHeight ) {
        Point2D point;
        List< Point2D > tops = util.calculateTopListFromNode( endPoint, endNodeWidth, endNodeHeight );
        Point2D upperLeft = tops.get( 3 );
        Point2D upperRight = tops.get( 0 );
        Point2D bottomLeft = tops.get( 2 );
        Point2D bottomRight = tops.get( 1 );
        boolean isHigherThatStartNode = isHigherThanSecondNodeThatFirstNode( startPoint, endPoint );
        boolean isLefterThatStartNode = isLefterThanSecondNodeThatFirstNode( startPoint, endPoint );
        boolean isIntersectedFromUpper = isIntersectedFromUpperOrBottomSideInSecondNode( startPoint, endPoint, endNodeWidth, endNodeHeight );
        boolean isIntersectedFromBottom = isIntersectedFromUpper; // コードの可視化を上げるために作ってるだけだよ

        // 終点に位置するノードを原点とすると、始点ノードは...

        if( isHigherThatStartNode ) {
            if( isLefterThatStartNode ) {
                // 第二象限
                if( isIntersectedFromUpper ) point = util.calculateCrossPoint( startPoint, endPoint, upperLeft, upperRight );
                else point = util.calculateCrossPoint( startPoint, endPoint, upperLeft, bottomLeft );

            } else {
                // 第一象限（Y軸上に位置する場合を含む）
                if( isIntersectedFromUpper ) point = util.calculateCrossPoint( startPoint, endPoint, upperLeft, upperRight );
                else point = util.calculateCrossPoint( startPoint, endPoint, upperRight, bottomRight );
            }

        } else {
            if( isLefterThatStartNode ) {
                // 第三象限（X軸上に位置する場合を含む）
                if( isIntersectedFromBottom ) point = util.calculateCrossPoint( startPoint, endPoint, bottomLeft, bottomRight );
                else point = util.calculateCrossPoint( startPoint, endPoint, upperLeft, bottomLeft );

            } else {
                // 第四象限（X軸上およびY軸上に位置する場合を含む）
                if( isIntersectedFromBottom ) point = util.calculateCrossPoint( startPoint, endPoint, bottomLeft, bottomRight );
                else point = util.calculateCrossPoint( startPoint, endPoint, upperRight, bottomRight );
            }
        }

        return point;
    }

    public boolean isHigherThanSecondNodeThatFirstNode( Point2D firstNode, Point2D secondNode ) {
        return firstNode.subtract( secondNode ).getY() < 0;
    }

    public boolean isLefterThanSecondNodeThatFirstNode( Point2D firstNode, Point2D secondNode ) {
        return firstNode.subtract( secondNode ).getX() < 0;
    }

    public boolean isIntersectedFromUpperOrBottomSideInSecondNode( Point2D firstNode, Point2D secondNode, double secondNodeWidth, double secondNodeHeight ) {
        boolean isIntersected = false;

        List< Point2D > tops = util.calculateTopListFromNode( secondNode, secondNodeWidth, secondNodeHeight );
        Point2D upperLeft = tops.get( 3 );
        Point2D upperRight = tops.get( 0 );
        Point2D bottomLeft = tops.get( 2 );
        Point2D bottomRight = tops.get( 1 );

        if( util.isIntersected( firstNode, secondNode, upperLeft, upperRight ) || util.isIntersected( firstNode, secondNode, bottomLeft, bottomRight ) ) isIntersected = true;

        return isIntersected;
    }

    /**
     * 2点を結ぶ直線の傾きを計算する。
     *
     * @param startPoint 直線における開始ポイント
     * @param endPoint 直線における終了ポイント
     * @return 法線の傾き {@code startPoint} と {@code endPoint} のX軸の値が等しい場合に限り {@code Infinity} を返す。
     */
    private double calculateInclination( Point2D startPoint, Point2D endPoint ) {
        double inclination;

        double width = startPoint.getX() - endPoint.getX();
        double height = startPoint.getY() - endPoint.getY();

        if( width != 0.0 ) {
            inclination = height / width;
        } else {
            inclination = Double.POSITIVE_INFINITY;
        }

        return inclination;
    }
}
