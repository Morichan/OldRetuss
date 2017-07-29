package retuss;

public class ClassNodeDiagram extends NodeDiagram {

    @Override
    public void draw() {
        gc.fillText( "classOfCD", mouseX, mouseY );
    }
}
