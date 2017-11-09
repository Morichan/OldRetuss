package retuss;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OperationTest {
    Operation operation;
    List< Operation > operations = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        operation = new Operation();

        operations.add( new Operation() );
        operations.add( new Operation() );
        operations.add( new Operation() );
    }

    @Test
    public void 操作を追加するとその操作を返す() {
        String expected = "operation";

        operation.setName( expected );

        assertThat( operation.getName() ).isEqualTo( expected );
    }

    @Test
    public void 操作の表示を真にすると真を返す() {
        boolean expected = true;

        operation.setIndication( expected );

        assertThat( operation.isIndicate() ).isEqualTo( expected );
    }

    @Test
    public void 操作のリストに名前を追加するとその名前のリストを返す() {
        List< String > expected = Arrays.asList( "operation1", "operation2", "operation3" );

        for( int i = 0; i < expected.size(); i++ )
            operations.get( i ).setName( expected.get( i ) );

        for( int i = 0; i < expected.size(); i++ )
            assertThat( operations.get( i ).getName() ).isEqualTo( expected.get( i ) );
    }

    @Test
    public void 操作のリストに真偽を追加するとその真偽を返す() {
        List< Boolean > expected = Arrays.asList( true, false, false );

        for( int i = 0; i < expected.size(); i++ )
            operations.get( i ).setIndication( expected.get( i ) );

        for( int i = 0; i < expected.size(); i++ )
            assertThat( operations.get( i ).isIndicate() ).isEqualTo( expected.get( i ) );
    }
}
