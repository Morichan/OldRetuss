package retuss;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ClassNodeDiagramTest {

    ClassNodeDiagram obj;

    @Before
    public void setObj() {
        obj = new ClassNodeDiagram();
    }

    @Test
    public void デフォルトのクラスの幅より大きい数値がクラス名に存在する場合大きな値にゆとり分を追加した幅を返す() {
        double wideWidthFromDefault = 100.0;
        List< Double > attributesAreNotWideWidthFromDefault = Arrays.asList( 10.0, 20.0 );
        List< Double > operationsAreNotWideWidthFromDefault = Arrays.asList( 5.0, 25.0 );

        double actual = obj.calculateMaxWidth( wideWidthFromDefault, attributesAreNotWideWidthFromDefault, operationsAreNotWideWidthFromDefault );

        assertThat( actual, is( wideWidthFromDefault + obj.getSpace() ) );
    }

    @Test
    public void デフォルトのクラスの幅より大きい数値がクラス属性の1つ目に存在する場合大きな値にゆとり分を追加した幅を返す() {
        double notWideWidth = 100.0 - obj.getSpace();
        List< Double > attributesAreWideWidthInFirstWidthFromDefault = Arrays.asList( 100.0, 20.0 );
        List< Double > operationsAreNotWideWidthFromDefault = Arrays.asList( 5.0, 25.0 );

        double actual = obj.calculateMaxWidth( notWideWidth, attributesAreWideWidthInFirstWidthFromDefault, operationsAreNotWideWidthFromDefault );

        assertThat( actual, is( attributesAreWideWidthInFirstWidthFromDefault.get( 0 ) + obj.getSpace() ) );
    }

    @Test
    public void デフォルトのクラスの幅より大きい数値がクラス属性の2つ目に存在する場合大きな値にゆとり分を追加した幅を返す() {
        double notWideWidth = 100.0 - obj.getSpace();
        List< Double > attributesAreWideWidthInSecondFromDefault = Arrays.asList( 10.0, 120.0 );
        List< Double > operationsAreNotWideWidthFromDefault = Arrays.asList( 5.0, 25.0 );

        double actual = obj.calculateMaxWidth( notWideWidth, attributesAreWideWidthInSecondFromDefault, operationsAreNotWideWidthFromDefault );

        assertThat( actual, is( attributesAreWideWidthInSecondFromDefault.get( 0 ) + obj.getSpace() ) );
    }

    @Test
    public void デフォルトのクラスの幅より大きい数値がクラス操作の3つ目に存在する場合大きな値にゆとり分を追加した幅を返す() {
        double notWideWidth = 100.0 - obj.getSpace();
        List< Double > attributesAreWideWidthInSecondFromDefault = Arrays.asList( 10.0, 20.0 );
        List< Double > operationsAreNotWideWidthFromDefault = Arrays.asList( 5.0, 200.0 );

        double actual = obj.calculateMaxWidth( notWideWidth, attributesAreWideWidthInSecondFromDefault, operationsAreNotWideWidthFromDefault );

        assertThat( actual, is( operationsAreNotWideWidthFromDefault.get( 0 ) + obj.getSpace() ) );
    }

    @Test
    public void デフォルトのクラスの幅より大きい数値が存在しない場合デフォルト幅を返す() {
        double notWideWidth = 100.0 - obj.getSpace();
        List< Double > attributesAreNotWideWidthFromDefault = Arrays.asList( 10.0, 20.0 );
        List< Double > operationsAreNotWideWidthFromDefault = Arrays.asList( 5.0, 25.0 );

        double actual = obj.calculateMaxWidth( notWideWidth, attributesAreNotWideWidthFromDefault, operationsAreNotWideWidthFromDefault );

        assertThat( actual, is( 100.0 ) );
    }

    @Test
    public void クラス属性が存在しない場合はデフォルト高を返す() {
        List< String > attributesAreNot = new ArrayList<>();

        double actual = obj.calculateMaxAttributeHeight( attributesAreNot );

        assertThat( actual, is( 20.0 ));
    }

    @Test
    public void クラス属性が1つ存在する場合はデフォルト高を返す() {
        List< String > attributeIsOne = Arrays.asList( "attribute1" );

        double actual = obj.calculateMaxAttributeHeight( attributeIsOne );

        assertThat( actual, is( 20.0 ));
    }

    @Test
    public void クラス属性が複数存在する場合はデフォルト高の複数倍を返す() {
        List< String > attributesAreMoreTwo = new ArrayList<>();
        attributesAreMoreTwo.add( "attribute1" );
        attributesAreMoreTwo.add( "attribute2" );

        double actual = obj.calculateMaxAttributeHeight( attributesAreMoreTwo );

        assertThat( actual, is( 20.0 * 2 ));

        attributesAreMoreTwo.add( "attribute3" );

        actual = obj.calculateMaxAttributeHeight( attributesAreMoreTwo );

        assertThat( actual, is( 20.0 * 3 ));

        attributesAreMoreTwo.add( "attribute4" );

        actual = obj.calculateMaxAttributeHeight( attributesAreMoreTwo );

        assertThat( actual, is( 20.0 * 4 ));

        attributesAreMoreTwo.add( "attribute5" );

        actual = obj.calculateMaxAttributeHeight( attributesAreMoreTwo );

        assertThat( actual, is( 20.0 * 5 ));
    }

    @Test
    public void クラス操作が存在しない場合はデフォルト高を返す() {
        List< String > operationsAreNot = new ArrayList<>();

        double actual = obj.calculateMaxOperationHeight( operationsAreNot );

        assertThat( actual, is( 20.0 ));
    }

    @Test
    public void クラス操作が1つ存在する場合はデフォルト高を返す() {
        List< String > operationIsOne = Arrays.asList( "operation1" );

        double actual = obj.calculateMaxOperationHeight( operationIsOne );

        assertThat( actual, is( 20.0 ));
    }

    @Test
    public void クラス操作が複数存在する場合はデフォルト高の複数倍を返す() {
        List< String > operationsAreMoreTwo = new ArrayList<>();
        operationsAreMoreTwo.add( "operation1" );
        operationsAreMoreTwo.add( "operation2" );

        double actual = obj.calculateMaxOperationHeight( operationsAreMoreTwo );

        assertThat( actual, is( 20.0 * 2 ));

        operationsAreMoreTwo.add( "operation3" );

        actual = obj.calculateMaxOperationHeight( operationsAreMoreTwo );

        assertThat( actual, is( 20.0 * 3 ));

        operationsAreMoreTwo.add( "operation4" );

        actual = obj.calculateMaxOperationHeight( operationsAreMoreTwo );

        assertThat( actual, is( 20.0 * 4 ));

        operationsAreMoreTwo.add( "operation5" );

        actual = obj.calculateMaxOperationHeight( operationsAreMoreTwo );

        assertThat( actual, is( 20.0 * 5 ));
    }
}