package retuss;

public class Operation extends ClassData {

    Operation() {
        name = "";
        visibility = "";
        type = ContentType.Undefined;
        isIndicate = true;
    }

    Operation( String name ) {
        this.name = name;
        isIndicate = true;
    }
}
