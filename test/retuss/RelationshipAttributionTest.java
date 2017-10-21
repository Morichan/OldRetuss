package retuss;

import com.sun.org.apache.regexp.internal.RE;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class RelationshipAttributionTest {
    RelationshipAttribution attribution;
    List< RelationshipAttribution> attributions = new ArrayList<>();

    @Before
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

        assertThat( attribution.getName(), is( expected ) );
    }

    @Test
    public void 関係属性の表示を真にすると真を返す() {
        boolean expected = true;

        attribution.setIndication( expected );

        assertThat( attribution.isIndicate(), is( expected ) );
    }

    @Test
    public void 関係属性のリストに名前を追加するとその名前のリストを返す() {
        List< String > expected = Arrays.asList( "attribution1", "attribution2", "attribution3" );

        for( int i = 0; i < expected.size(); i++ )
            attributions.get( i ).setName( expected.get( i ) );

        for( int i = 0; i < expected.size(); i++ )
            assertThat( attributions.get( i ).getName(), is( expected.get( i ) ) );
    }

    @Test
    public void 関係属性のリストに真偽を追加するとその真偽を返す() {
        List< Boolean > expected = Arrays.asList( true, false, false );

        for( int i = 0; i < expected.size(); i++ )
            attributions.get( i ).setIndication( expected.get( i ) );

        for( int i = 0; i < expected.size(); i++ )
            assertThat( attributions.get( i ).isIndicate(), is( expected.get( i ) ) );
    }

    @Test
    public void 関係属性の関係先のIDを設定するとそのIDを返す() {
        int expected = 1;

        attribution.setRelationId( expected );

        assertThat( attribution.getRelationId(), is( expected ) );
    }

    @Test
    public void 関係属性の関係元のIDを設定するとそのIDを返す() {
        int expected = 0;

        attribution.setRelationSourceId( expected );

        assertThat( attribution.getRelationSourceId(), is( expected ) );
    }
}