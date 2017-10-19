package retuss;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;

import javax.swing.text.AbstractDocument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ClassNodeDiagram extends NodeDiagram {
    private Point2D upperLeftCorner = Point2D.ZERO;
    private Point2D bottomRightCorner = Point2D.ZERO;

    private int classNameFontSize = 20;
    private int classAttributionFontSize = 15;
    private int classOperationFontSize = 15;
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

    private List< Boolean > attributionIsVisibility = new ArrayList<>();
    private int attributionNotVisibilityCount = 0;

    private List< Boolean > operationIsVisibility = new ArrayList<>();
    private int operationNotVisibilityCount = 0;

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
    public void createNodeText( ContentType type, String text ) {
        if( type == ContentType.Title ) {
            nodeText = text;
        } else if( type == ContentType.Attribution ) {
            attributions.add( text );
            attributionIsVisibility.add( true );
        } else if( type == ContentType.Operation ) {
            operations.add( text );
            operationIsVisibility.add( true );
        }
    }

    @Override
    public void changeNodeText( ContentType type, int number, String text ) {
        if( type == ContentType.Title ) {
            nodeText = text;
        } else if( type == ContentType.Attribution ) {
            attributions.set( number, text );
        } else if( type == ContentType.Operation ) {
            operations.set( number, text );
        }
    }

    @Override
    public void deleteNodeText( ContentType type, int number ) {
        if( type == ContentType.Attribution ) {
            attributions.remove( number );
            attributionIsVisibility.remove( number );
        } else if( type == ContentType.Operation ) {
            operations.remove( number );
            operationIsVisibility.remove( number );
        }
    }

    @Override
    public String getNodeContentText( ContentType type, int number ) {
        String content;

        if( type == ContentType.Attribution ) {
            content = attributions.get( number );
        } else if( type == ContentType.Operation ) {
            content = operations.get(number);
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
    public void setNodeContentBoolean( ContentType parent, ContentType child, int contentNumber, boolean isChecked ) {
        if( parent == ContentType.Attribution ) {
            if( child == ContentType.Visibility ) {
                attributionIsVisibility.set( contentNumber, isChecked );
            }
        } else if( parent == ContentType.Operation ) {
            operationIsVisibility.set( contentNumber, isChecked );
        }
    }

    @Override
    public List< Boolean > getNodeContentsBoolean( ContentType parent, ContentType child ) {
        List< Boolean > list;
        if( parent == ContentType.Attribution ) {
            list = attributionIsVisibility;
        } else if( parent == ContentType.Operation ) {
            list = operationIsVisibility;
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
        List< Text > operationsText = new ArrayList<>();
        for( String attribution: attributions ) {
            Text text = new Text( attribution );
            text.setFont( Font.font( diagramFont, FontWeight.LIGHT, classAttributionFontSize ) );
            attributionsText.add( text );
        }
        for( String operation: operations ) {
            Text text = new Text( operation );
            text.setFont( Font.font( diagramFont, FontWeight.LIGHT, classOperationFontSize ) );
            operationsText.add( text );
        }
        double maxWidth = calculateMaxWidth( classNameText, attributionsText, operationsText );
        double classHeight = defaultClassHeight;
        double attributionHeight = calculateMaxAttributionHeight( attributions );
        double operationHeight = calculateMaxOperationHeight( operations );
        double operationStartHeight = calculateStartOperationHeight( attributions );

        calculateWidthAndHeight( maxWidth, classHeight + attributionHeight + operationHeight );

        drawGraphicsContext( classNameText, attributionsText, operationsText, maxWidth, classHeight, attributionHeight, operationHeight, operationStartHeight );
    }

    public void drawGraphicsContext( Text classNameText, List< Text > attributionsText, List< Text > operationsText, double maxWidth, double classHeight, double attributionHeight, double operationHeight, double operationStartHeight ) {
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
            int notDrawAttributionCount = 0;
            for( int i = 0; i < attributionsText.size(); i++ ) {
                if( attributionIsVisibility.get( i ) ) {
                    gc.fillText( attributionsText.get(i).getText(), upperLeftCorner.getX() + leftSpace, mouse.getY() + 15.0 + ( defaultAttributionHeight * ( i - notDrawAttributionCount ) ) );
                } else {
                    notDrawAttributionCount++;
                }
            }
            if( attributionIsVisibility.contains( false ) ) {
                gc.setTextAlign( TextAlignment.CENTER );
                gc.fillText( "... " + attributionNotVisibilityCount + " more", mouse.getX(), mouse.getY() + 15.0 + ( defaultAttributionHeight * ( attributionsText.size() - attributionNotVisibilityCount ) ) );
            }
        }

        if( operationsText.size() > 0 ) {
            gc.setTextAlign( TextAlignment.LEFT );
            gc.setFont( operationsText.get( 0 ).getFont() );
            int notDrawOperationCount = 0;
            for( int i = 0; i < operationsText.size(); i++ ) {
                if( operationIsVisibility.get( i ) ) {
                    gc.fillText( operationsText.get(i).getText(), upperLeftCorner.getX() + leftSpace, mouse.getY() + 15.0 + ( defaultOperationHeight * ( i - notDrawOperationCount ) ) + operationStartHeight );
                } else {
                    notDrawOperationCount++;
                }
            }
            if( operationIsVisibility.contains( false ) ) {
                gc.setTextAlign( TextAlignment.CENTER );
                gc.fillText( "... " + operationNotVisibilityCount + " more", mouse.getX(), mouse.getY() + 15.0 + ( defaultOperationHeight * ( operationsText.size() - operationNotVisibilityCount ) ) + operationStartHeight );
            }
        }
    }

    public double calculateMaxWidth( Text text, List< Text > attributionsText, List< Text > operationsText ) {
        double width = defaultWidth - classNameSpace;

        List< Double > classAttributions = new ArrayList<>();
        classAttributions.add( 0.0 );
        for( int i = 0; i < attributionsText.size(); i++ ) {
            if( attributionIsVisibility.get( i ) )
                classAttributions.add( attributionsText.get( i ).getLayoutBounds().getWidth() );
        }
        List< Double > classOperations = new ArrayList<>();
        classOperations.add( 0.0 );
        for( int i = 0; i < operationsText.size(); i++ ) {
            if( operationIsVisibility.get( i ) )
                classOperations.add( operationsText.get( i ).getLayoutBounds().getWidth() );
        }

        classAttributions.sort( Comparator.reverseOrder() );
        classOperations.sort( Comparator.reverseOrder() );

        List< Double > classWidth = Arrays.asList( text.getLayoutBounds().getWidth(), classAttributions.get( 0 ), classOperations.get( 0 ) );

        classWidth.sort( Comparator.reverseOrder() );

        if( width < classWidth.get( 0 ) ) width = classWidth.get( 0 ) + classNameSpace;
        else width = defaultWidth;

        return width;
    }

    public double calculateMaxAttributionHeight( List< String > attributions ) {
        double height = defaultAttributionHeight;
        attributionNotVisibilityCount = countNotBooleanContents( attributionIsVisibility );

        if( attributions.size() > 0 ) {
            if( attributionNotVisibilityCount > 0 ) {
                if( attributionNotVisibilityCount != attributions.size() )
                    height = ( attributions.size() - attributionNotVisibilityCount + 1 ) * defaultAttributionHeight;
                else
                    height = defaultAttributionHeight;
            } else {
                height = attributions.size() * defaultAttributionHeight;
            }
        }

        return height;
    }

    public double calculateStartOperationHeight( List< String > attributions ) {
        double height = 20.0;

        if( attributions.size() > 0 ) {
            height = attributions.size() * 20.0;
        }
        if( attributionNotVisibilityCount > 0 ) {
            height -= ( ( attributionNotVisibilityCount - 1 ) * 20 );
        }
        if( attributions.size() == attributionNotVisibilityCount ) {
            height = 20.0;
        }

        return height;
    }

    public double calculateMaxOperationHeight( List< String > operations ) {
        double height = defaultOperationHeight;
        operationNotVisibilityCount = countNotBooleanContents( operationIsVisibility );

        if( operations.size() > 0 ) {
            if( operationNotVisibilityCount > 0 ) {
                if( operationNotVisibilityCount != operations.size() )
                    height = ( operations.size() - operationNotVisibilityCount + 1 ) * defaultOperationHeight;
                else
                    height = defaultOperationHeight;
            } else {
                height = operations.size() * defaultOperationHeight;
            }
        }

        return height;
    }

    public int countNotBooleanContents( List< Boolean > isNotBooleans ) {
        int count = 0;
        for( Boolean isVisibility : isNotBooleans ) {
            if( ! isVisibility ) count++;
        }
        return count;
    }

    public void calculateWidthAndHeight( double maxWidth, double maxHeight ) {
        calculateUpperLeftCorner( mouse, maxWidth );
        calculateBottomRightCorner( mouse, maxWidth, maxHeight );
        width = bottomRightCorner.subtract( upperLeftCorner ).getX();
        height = bottomRightCorner.subtract( upperLeftCorner ).getY();
    }

    private void calculateUpperLeftCorner( Point2D point, double width ) {
        upperLeftCorner = new Point2D( point.getX() - width/2, point.getY() - 40.0 );
    }

    private void calculateBottomRightCorner( Point2D point, double width, double height ) {
        bottomRightCorner = new Point2D( point.getX() + width/2, point.getY() + height - defaultClassHeight );
    }
}
