package retuss;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AttributionTest {
    Attribution attribution;
    List< Attribution > attributions = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        attribution = new Attribution();

        attributions.add( new Attribution() );
        attributions.add( new Attribution() );
        attributions.add( new Attribution() );
    }

    @Test
    public void 属性を追加するとその属性を返す() {
        String expected = "attribution";

        attribution.setName( expected );

        assertThat( attribution.getName() ).isEqualTo( expected );
    }

    @Test
    public void 属性の表示を真にすると真を返す() {
        boolean expected = true;

        attribution.setIndication( expected );

        assertThat( attribution.isIndicate() ).isEqualTo( expected );
    }

    @Test
    public void 属性のリストに名前を追加するとその名前のリストを返す() {
        List< String > expected = Arrays.asList( "attribution1", "attribution2", "attribution3" );

        for( int i = 0; i < expected.size(); i++ )
            attributions.get( i ).setName( expected.get( i ) );

        for( int i = 0; i < expected.size(); i++ )
            assertThat( attributions.get( i ).getName() ).isEqualTo( expected.get( i ) );
    }

    @Test
    public void 属性のリストに真偽を追加するとその真偽を返す() {
        List< Boolean > expected = Arrays.asList( true, false, false );

        for( int i = 0; i < expected.size(); i++ )
            attributions.get( i ).setIndication( expected.get( i ) );

        for( int i = 0; i < expected.size(); i++ )
            assertThat( attributions.get( i ).isIndicate() ).isEqualTo( expected.get( i ) );
    }

}