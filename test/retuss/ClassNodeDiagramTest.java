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
    public void デフォルトのクラスの幅より大きい数値がクラス名に存在する場合大きな値にゆとり分を追加した値を返す() {
        double wideWidthFromDefault = 100.0;
        List< Double > attributesAreNotWideWidthFromDefault = Arrays.asList( 10.0, 20.0 );
        List< Double > operationsAreNotWideWidthFromDefault = Arrays.asList( 5.0, 25.0 );

        double actual = obj.calculateMaxWidth( wideWidthFromDefault, attributesAreNotWideWidthFromDefault, operationsAreNotWideWidthFromDefault );

        assertThat( actual, is( wideWidthFromDefault + obj.getSpace() ) );
    }

    @Test
    public void デフォルトのクラスの幅より大きい数値がクラス属性の1つ目に存在する場合大きな値にゆとり分を追加した値を返す() {
        double notWideWidth = 100.0 - obj.getSpace();
        List< Double > attributesAreWideWidthInFirstWidthFromDefault = Arrays.asList( 100.0, 20.0 );
        List< Double > operationsAreNotWideWidthFromDefault = Arrays.asList( 5.0, 25.0 );

        double actual = obj.calculateMaxWidth( notWideWidth, attributesAreWideWidthInFirstWidthFromDefault, operationsAreNotWideWidthFromDefault );

        assertThat( actual, is( attributesAreWideWidthInFirstWidthFromDefault.get( 0 ) + obj.getSpace() ) );
    }

    @Test
    public void デフォルトのクラスの幅より大きい数値がクラス属性の2つ目に存在する場合大きな値にゆとり分を追加した値を返す() {
        double notWideWidth = 100.0 - obj.getSpace();
        List< Double > attributesAreWideWidthInSecondFromDefault = Arrays.asList( 10.0, 120.0 );
        List< Double > operationsAreNotWideWidthFromDefault = Arrays.asList( 5.0, 25.0 );

        double actual = obj.calculateMaxWidth( notWideWidth, attributesAreWideWidthInSecondFromDefault, operationsAreNotWideWidthFromDefault );

        assertThat( actual, is( attributesAreWideWidthInSecondFromDefault.get( 0 ) + obj.getSpace() ) );
    }

    @Test
    public void デフォルトのクラスの幅より大きい数値がクラス操作の3つ目に存在する場合大きな値にゆとり分を追加した値を返す() {
        double notWideWidth = 100.0 - obj.getSpace();
        List< Double > attributesAreWideWidthInSecondFromDefault = Arrays.asList( 10.0, 20.0 );
        List< Double > operationsAreNotWideWidthFromDefault = Arrays.asList( 5.0, 200.0 );

        double actual = obj.calculateMaxWidth( notWideWidth, attributesAreWideWidthInSecondFromDefault, operationsAreNotWideWidthFromDefault );

        assertThat( actual, is( operationsAreNotWideWidthFromDefault.get( 0 ) + obj.getSpace() ) );
    }

    @Test
    public void デフォルトのクラスの幅より大きい数値が存在しない場合デフォルト値を返す() {
        double notWideWidth = 100.0 - obj.getSpace();
        List< Double > attributesAreNotWideWidthFromDefault = Arrays.asList( 10.0, 20.0 );
        List< Double > operationsAreNotWideWidthFromDefault = Arrays.asList( 5.0, 25.0 );

        double actual = obj.calculateMaxWidth( notWideWidth, attributesAreNotWideWidthFromDefault, operationsAreNotWideWidthFromDefault );

        assertThat( actual, is( 100.0 ) );
    }

}