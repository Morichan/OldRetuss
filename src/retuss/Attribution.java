package retuss;

public class Attribution extends ClassData {

    Attribution() {
        name = "";
        visibility = "";
        type = ContentType.Undefined;
        isIndicate = true;
    }

    Attribution( String name ) {
        this.name = name;
        isIndicate = true;
    }
}
