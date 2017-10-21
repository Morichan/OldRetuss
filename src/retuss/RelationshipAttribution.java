package retuss;

public class RelationshipAttribution extends Attribution {
    private int relationId;
    private int relationSourceId;

    RelationshipAttribution() {
        name = "";
        visibility = "";
        type = "";
        isIndicate = true;
    }

    RelationshipAttribution( String name ) {
        this.name = name;
        isIndicate = true;
    }

    public void setRelationId( int id ) {
        relationId = id;
    }

    public int getRelationId() {
        return relationId;
    }

    public void setRelationSourceId( int id ) {
        relationSourceId = id;
    }

    public int getRelationSourceId() {
        return relationSourceId;
    }
}
