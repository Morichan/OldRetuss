package retuss;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ClassNodeDiagram extends NodeDiagram {
    private Point2D upperLeftCorner = Point2D.ZERO;
    private Point2D bottomRightCorner = Point2D.ZERO;

    private int classNameFontSize = 20;
    private int classAttributionFontSize = 15;
    private final double defaultWidth = 100.0;
    private final double defaultClassHeight = 40.0;
    private final double defaultAttributionHeight = 20.0;
    private final double defaultOperationHeight = 20.0;
    private final double classNameSpace = 20.0;
    private final double leftSpace = 5.0;

    private double width = 0.0;
    private double height = 0.0;

    private List< String > attributions = new ArrayList<>();
    private List< String > operations = new ArrayList<>();

    public double getClassNameSpace() {
        return classNameSpace;
    }

    @Override
    public boolean isAlreadyDrawnNode( double x, double y ) {
        boolean act = false;

        if( upperLeftCorner.getX() < x && upperLeftCorner.getY() < y
                && x < bottomRightCorner.getX() && y < bottomRightCorner.getY() )
            act = true;

        return act;
    }

    @Override
    public void createNodeText(ContentType type, String text ) {
        if( type == ContentType.Title ) {
            nodeText = text;
        } else if( type == ContentType.Attribution ) {
            attributions.add( text );
        }
    }

    @Override
    public void changeNodeText( ContentType type, int number, String text ) {
        if( type == ContentType.Title ) {
            nodeText = text;
        } else if( type == ContentType.Attribution ) {
            attributions.set( number, text );
        }
    }

    @Override
    public void deleteNodeText( ContentType type, int number ) {
        if( type == ContentType.Attribution ) {
            attributions.remove( number );
        }
    }

    @Override
    public String getNodeContentText( ContentType type, int number ) {
        String content;

        if( type == ContentType.Attribution ) {
            content = attributions.get( number );
        } else {
            content = "";
        }
        return content;
    }

    @Override
    public List< String > getNodeContents( ContentType type ) {
        List< String > list;
        if( type == ContentType.Attribution ) {
            list = attributions;
        } else if( type == ContentType.Operation ) {
            list = operations;
        } else {
            list = null;
        }
        return list;
    }

    @Override
    public void draw() {
        if( nodeText.length() <= 0 ) return;

        Text classNameText = new Text( nodeText );
        classNameText.setFont( Font.font( diagramFont , FontWeight.BOLD, classNameFontSize ) );
        List< Text > attributionsText = new ArrayList<>();
        for( String attribution: attributions ) {
            Text text = new Text( attribution );
            text.setFont( Font.font( diagramFont, FontWeight.LIGHT, classAttributionFontSize ) );
            attributionsText.add( text );
        }
        double maxWidth = calculateMaxWidth( classNameText, attributionsText, Arrays.asList( 5.0, 25.0 ) );
        double classHeight = defaultClassHeight;
        double attributeHeight = calculateMaxAttributeHeight( attributions );
        double operationHeight = calculateMaxOperationHeight( Arrays.asList() );

        calculateWidthAndHeight( maxWidth );

        drawGraphicsContext( classNameText, attributionsText, maxWidth, classHeight, attributeHeight, operationHeight );
    }

    public void drawGraphicsContext( Text classNameText, List< Text > attributionsText, double maxWidth, double classHeight, double attributionHeight, double operationHeight ) {
        gc.setFill( Color.BEIGE );
        gc.fillRect( upperLeftCorner.getX(), upperLeftCorner.getY(), maxWidth, classHeight + attributionHeight + operationHeight );

        gc.setStroke( Color.BLACK );
        gc.strokeRect( upperLeftCorner.getX(), upperLeftCorner.getY(), maxWidth, classHeight + attributionHeight + operationHeight );
        gc.strokeLine( upperLeftCorner.getX(), upperLeftCorner.getY() + classHeight, bottomRightCorner.getX(), upperLeftCorner.getY() + classHeight );
        gc.strokeLine( upperLeftCorner.getX(), upperLeftCorner.getY() + classHeight + attributionHeight, bottomRightCorner.getX(), upperLeftCorner.getY() + classHeight + attributionHeight );

        gc.setFill( Color.BLACK );
        gc.setTextAlign( TextAlignment.CENTER );
        gc.setFont( classNameText.getFont() );
        gc.fillText( classNameText.getText(), mouse.getX(), mouse.getY() - classHeight/2 );

        if( attributionsText.size() > 0 ) {
            gc.setTextAlign( TextAlignment.LEFT );
            gc.setFont( attributionsText.get( 0 ).getFont() );
            for( int i = 0; i < attributionsText.size(); i++ ) {
                gc.fillText( attributionsText.get( i ).getText(), upperLeftCorner.getX() + leftSpace, mouse.getY() + 15.0 + ( defaultAttributionHeight * i ) );
            }
        }

        // gc.setFill( Color.GREEN );
        // gc.fillOval( ( mouse.getX() - hoge.getWidth() / 2 ) - 5, mouse.getY() - 5, 10, 10);
        // gc.setFill( Color.BLUE );
        // gc.fillOval( ( mouse.getX() + hoge.getWidth() / 2 ) - 5, mouse.getY() - 5, 10, 10);
    }

    public double calculateMaxWidth( Text text, List< Text > attributionsText, List< Double > classOperations ) {
        double width = defaultWidth - classNameSpace;

        List< Double > classAttributions = new ArrayList<>();
        classAttributions.add( 0.0 );
        for( Text attribution: attributionsText ) classAttributions.add( attribution.getLayoutBounds().getWidth() );

        classAttributions.sort( Comparator.reverseOrder() );
        classOperations.sort( Comparator.reverseOrder() );

        List< Double > classWidth = Arrays.asList( text.getLayoutBounds().getWidth(), classAttributions.get( 0 ), classOperations.get( 0 ) );

        classWidth.sort( Comparator.reverseOrder() );

        if( width < classWidth.get( 0 ) ) width = classWidth.get( 0 ) + classNameSpace;
        else width = defaultWidth;

        return width;
    }

    public double calculateMaxAttributeHeight( List< String > attributes ) {
        double height = defaultAttributionHeight;

        if( attributes.size() > 0 ) {
            height = attributes.size() * defaultAttributionHeight;
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

    public void calculateWidthAndHeight( double maxWidth ) {
        calculateUpperLeftCorner( mouse, maxWidth );
        calculateBottomRightCorner( mouse, maxWidth );
        width = bottomRightCorner.subtract( upperLeftCorner ).getX();
        height = bottomRightCorner.subtract( upperLeftCorner ).getY();
    }

    private void calculateUpperLeftCorner( Point2D point, double width ) {
        upperLeftCorner = new Point2D( point.getX() - width/2, point.getY() - 40.0 );
    }

    private void calculateBottomRightCorner( Point2D point, double width ) {
        bottomRightCorner = new Point2D( point.getX() + width/2, point.getY() + 40.0 );
    }
}
