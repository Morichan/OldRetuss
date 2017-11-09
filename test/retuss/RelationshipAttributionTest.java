package retuss;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RelationshipAttributionTest {
    RelationshipAttribution attribution;
    List< RelationshipAttribution> attributions = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        attribution = new RelationshipAttribution();

        attributions.add( new RelationshipAttribution() );
        attributions.add( new RelationshipAttribution() );
        attributions.add( new RelationshipAttribution() );
    }

    @Test
    public void 関係属性を追加するとその属性を返す() {
        String expected = "relationshipAttribution";

        attribution.setName( expected );

        assertThat( attribution.getName() ).isEqualTo( expected );
    }

    @Test
    public void 関係属性の表示を真にすると真を返す() {
        boolean expected = true;

        attribution.setIndication( expected );

        assertThat( attribution.isIndicate() ).isEqualTo( expected );
    }

    @Test
    public void 関係属性のリストに名前を追加するとその名前のリストを返す() {
        List< String > expected = Arrays.asList( "attribution1", "attribution2", "attribution3" );

        for( int i = 0; i < expected.size(); i++ )
            attributions.get( i ).setName( expected.get( i ) );

        for( int i = 0; i < expected.size(); i++ )
            assertThat( attributions.get( i ).getName() ).isEqualTo( expected.get( i ) );
    }

    @Test
    public void 関係属性のリストに真偽を追加するとその真偽を返す() {
        List< Boolean > expected = Arrays.asList( true, false, false );

        for( int i = 0; i < expected.size(); i++ )
            attributions.get( i ).setIndication( expected.get( i ) );

        for( int i = 0; i < expected.size(); i++ )
            assertThat( attributions.get( i ).isIndicate() ).isEqualTo( expected.get( i ) );
    }

    @Test
    public void 関係属性の関係先のIDを設定するとそのIDを返す() {
        int expected = 1;

        attribution.setRelationId( expected );

        assertThat( attribution.getRelationId() ).isEqualTo( expected );
    }

    @Test
    public void 関係属性の関係元のIDを設定するとそのIDを返す() {
        int expected = 0;

        attribution.setRelationSourceId( expected );

        assertThat( attribution.getRelationSourceId() ).isEqualTo( expected );
    }

    @Test
    public void 関係属性の関係先のポイントを設定するとそのポイントを返す() {
        Point2D expected = new Point2D( 100.0, 200.0 );

        attribution.setRelationPoint( expected );
        Point2D actual = attribution.getRelationPoint();

        assertThat( actual ).isEqualTo( expected );
    }

    @Test
    public void 関係属性の関係元のポイントを設定するとそのポイントを返す() {
        Point2D expected = new Point2D( 300.0, 400.0 );

        attribution.setRelationSourcePoint( expected );
        Point2D actual = attribution.getRelationSourcePoint();

        assertThat( actual ).isEqualTo( expected );
    }
}
