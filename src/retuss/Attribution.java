package retuss;

public class Attribution extends ClassData {

    Attribution() {
        name = "";
        visibility = "";
        type = "";
        isIndicate = true;
    }

    Attribution( String name ) {
        this.name = name;
        isIndicate = true;
    }
}
