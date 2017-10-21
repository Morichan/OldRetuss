package retuss;

public class ClassData {
    protected String name;
    protected String visibility;
    protected String type;

    protected boolean isIndicate;

    public void setName( String name ) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIndication( boolean isIndicate ) {
        this.isIndicate = isIndicate;
    }

    public boolean isIndicate() {
        return isIndicate;
    }
}
