package retuss;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ClassNodeDiagram extends NodeDiagram {
    private Point2D upperLeftCorner = Point2D.ZERO;
    private Point2D bottomRightCorner = Point2D.ZERO;

    private int classNameFontSize = 20;
    private final double defaultWidth = 100.0;
    private final double defaultClassHeight = 40.0;
    private final double defaultAttributeHeight = 20.0;
    private final double defaultOperationHeight = 20.0;
    private final double space = 20.0;

    private double width = 0.0;
    private double height = 0.0;

    ClassNodeDiagram() {
    }

    public double getSpace() {
        return space;
    }

    @Override
    public void draw() {
        if( nodeText.length() <= 0 ) return;

        Text text = new Text( nodeText );
        text.setFont( Font.font( diagramFont , FontWeight.BOLD, classNameFontSize ) );
        Bounds className = text.getLayoutBounds();
        double maxWidth = calculateMaxWidth( className.getWidth(), Arrays.asList( 10.0, 20.0 ), Arrays.asList( 5.0, 25.0 ) );
        double classHeight = defaultClassHeight;
        double attributeHeight = calculateMaxAttributeHeight( Arrays.asList() );
        double operationHeight = calculateMaxOperationHeight( Arrays.asList() );

        calculateUpperLeftCorner( mouse, maxWidth );
        calculateBottomRightCorner( mouse, maxWidth );
        calculateWidthAndHeight();

        gc.setFill( Color.BEIGE );
        gc.fillRect( upperLeftCorner.getX(), upperLeftCorner.getY(), maxWidth, classHeight + attributeHeight + operationHeight );

        gc.setStroke( Color.BLACK );
        gc.strokeRect( upperLeftCorner.getX(), upperLeftCorner.getY(), maxWidth, classHeight + attributeHeight + operationHeight );
        gc.strokeLine( upperLeftCorner.getX(), upperLeftCorner.getY() + classHeight, bottomRightCorner.getX(), upperLeftCorner.getY() + classHeight );
        gc.strokeLine( upperLeftCorner.getX(), upperLeftCorner.getY() + classHeight + attributeHeight, bottomRightCorner.getX(), upperLeftCorner.getY() + classHeight + attributeHeight );

        gc.setFill( Color.BLACK );
        gc.setTextAlign( TextAlignment.CENTER );
        gc.setFont( text.getFont() );
        gc.fillText( text.getText(), mouse.getX(), mouse.getY() - classHeight/2 );

        // gc.setFill( Color.GREEN );
        // gc.fillOval( ( mouse.getX() - hoge.getWidth() / 2 ) - 5, mouse.getY() - 5, 10, 10);
        // gc.setFill( Color.BLUE );
        // gc.fillOval( ( mouse.getX() + hoge.getWidth() / 2 ) - 5, mouse.getY() - 5, 10, 10);
    }

    public double calculateMaxWidth( double classNameWidth, List< Double > classAttributes, List< Double > classOperations ) {
        double width = defaultWidth - space;

        classAttributes.sort( Comparator.reverseOrder() );
        classOperations.sort( Comparator.reverseOrder() );

        List< Double > classWidth = Arrays.asList( classNameWidth, classAttributes.get( 0 ), classOperations.get( 0 ) );

        classWidth.sort( Comparator.reverseOrder() );

        if( width < classWidth.get( 0 ) ) width = classWidth.get( 0 ) + space;
        else width = defaultWidth;

        return width;
    }

    public double calculateMaxAttributeHeight( List< String > attributes ) {
        double height = defaultAttributeHeight;

        if( attributes.size() > 0 ) {
            height = attributes.size() * defaultAttributeHeight;
        }

        return height;
    }

    public double calculateMaxOperationHeight( List< String > attributes ) {
        double height = defaultOperationHeight;

        if( attributes.size() > 0 ) {
            height = attributes.size() * defaultOperationHeight;
        }

        return height;
    }

    private void calculateUpperLeftCorner( Point2D point, double width ) {
        upperLeftCorner = new Point2D( point.getX() - width/2, point.getY() - 40.0 );
    }

    private void calculateBottomRightCorner( Point2D point, double width ) {
        bottomRightCorner = new Point2D( point.getX() + width/2, point.getY() + 40.0 );
    }

    private void calculateWidthAndHeight() {
        width = bottomRightCorner.subtract( upperLeftCorner ).getX();
        height = bottomRightCorner.subtract( upperLeftCorner ).getY();
    }
}
