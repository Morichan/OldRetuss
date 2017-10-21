package retuss;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class OperationTest {
    Operation operation;
    List< Operation > operations = new ArrayList<>();

    @Before
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

        assertThat( operation.getName(), is( expected ) );
    }

    @Test
    public void 操作の表示を真にすると真を返す() {
        boolean expected = true;

        operation.setIndication( expected );

        assertThat( operation.isIndicate(), is( expected ) );
    }

    @Test
    public void 操作のリストに名前を追加するとその名前のリストを返す() {
        List< String > expected = Arrays.asList( "operation1", "operation2", "operation3" );

        for( int i = 0; i < expected.size(); i++ )
            operations.get( i ).setName( expected.get( i ) );

        for( int i = 0; i < expected.size(); i++ )
            assertThat( operations.get( i ).getName(), is( expected.get( i ) ) );
    }

    @Test
    public void 操作のリストに真偽を追加するとその真偽を返す() {
        List< Boolean > expected = Arrays.asList( true, false, false );

        for( int i = 0; i < expected.size(); i++ )
            operations.get( i ).setIndication( expected.get( i ) );

        for( int i = 0; i < expected.size(); i++ )
            assertThat( operations.get( i ).isIndicate(), is( expected.get( i ) ) );
    }
}