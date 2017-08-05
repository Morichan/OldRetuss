package retuss;

import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class ClassNodeDiagram extends NodeDiagram {
    private double upperLeftCornerX = 0.0;
    private double upperLeftCornerY = 0.0;
    private double bottomRightCornerX = 0.0;
    private double bottomRightCornerY = 0.0;
    private double width = 0.0;
    private double height = 0.0;

    @Override
    public void draw() {
        if( nodeText.length() <= 0 ) return;

        calculateUpperLeftCorner( mouseX, mouseY );
        calculateBottomRightCorner( mouseX, mouseY );
        calculateWidthAndHeight();

        gc.setFill( Color.BEIGE );
        gc.fillRect( upperLeftCornerX, upperLeftCornerY, width, height );

        gc.setFill( Color.BLACK );
        gc.strokeRect( upperLeftCornerX, upperLeftCornerY, width, height );

        gc.setFill( Color.BLACK );
        gc.setTextAlign( TextAlignment.CENTER );
        Text text = new Text( nodeText );
        text.setFont( Font.font( "Consolas" , FontWeight.BOLD, 35 ) );
        Bounds hoge = text.getLayoutBounds();
        System.out.println( hoge + "\n");
        gc.setFont( text.getFont() );
        gc.fillText( text.getText(), mouseX, mouseY );

        gc.setFill( Color.GREEN );
        gc.fillOval( ( mouseX - hoge.getWidth() / 2 ) - 5, mouseY - 5, 10, 10);
        gc.setFill( Color.BLUE );
        gc.fillOval( ( mouseX + hoge.getWidth() / 2 ) - 5, mouseY - 5, 10, 10);
    }

    private void calculateUpperLeftCorner( double x, double y ) {
        upperLeftCornerX = x - 50.0;
        upperLeftCornerY = y - 40.0;
    }

    private void calculateBottomRightCorner( double x, double y ) {
        bottomRightCornerX = x + 50.0;
        bottomRightCornerY = y + 40.0;
    }

    private void calculateWidthAndHeight() {
        width = bottomRightCornerX - upperLeftCornerX;
        height = bottomRightCornerY - upperLeftCornerY;
    }
}
