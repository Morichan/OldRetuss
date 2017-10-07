package retuss;

import javafx.scene.text.Text;
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
        Text longClassName = new Text( "VeryVeryLongClassName" );
        List< Text > attributionsAreNotWideWidthFromDefault = Arrays.asList( new Text( "short" ), new Text( "attribution" ) );
        List< Double > operationsAreNotWideWidthFromDefault = Arrays.asList( 5.0, 25.0 );

        double actual = obj.calculateMaxWidth( longClassName, attributionsAreNotWideWidthFromDefault, operationsAreNotWideWidthFromDefault );

        assertThat( actual, is( longClassName.getLayoutBounds().getWidth() + obj.getClassNameSpace() ) );
    }

    @Test
    public void デフォルトのクラスの幅より大きい数値がクラス属性の1つ目に存在する場合大きな値にゆとり分を追加した幅を返す() {
        Text shortClassName = new Text( "a" );
        List< Text > attributionsAreNotWideWidthFromDefault = Arrays.asList( new Text( "- veryVeryLongClassAttribution : double" ), new Text( "attribution" ) );
        List< Double > operationsAreNotWideWidthFromDefault = Arrays.asList( 5.0, 25.0 );

        double actual = obj.calculateMaxWidth( shortClassName, attributionsAreNotWideWidthFromDefault, operationsAreNotWideWidthFromDefault );

        assertThat( actual, is( attributionsAreNotWideWidthFromDefault.get( 0 ).getLayoutBounds().getWidth() + obj.getClassNameSpace() ) );
    }

    @Test
    public void デフォルトのクラスの幅より大きい数値がクラス属性の2つ目に存在する場合大きな値にゆとり分を追加した幅を返す() {
        Text shortClassName = new Text( "a" );
        List< Text > attributionsAreNotWideWidthFromDefault = Arrays.asList( new Text( "attribution" ), new Text( "- veryVeryLongClassAttribution : double" ) );
        List< Double > operationsAreNotWideWidthFromDefault = Arrays.asList( 5.0, 25.0 );

        double actual = obj.calculateMaxWidth( shortClassName, attributionsAreNotWideWidthFromDefault, operationsAreNotWideWidthFromDefault );

        assertThat( actual, is( attributionsAreNotWideWidthFromDefault.get( 1 ).getLayoutBounds().getWidth() + obj.getClassNameSpace() ) );
    }

    @Test
    public void デフォルトのクラスの幅より大きい数値がクラス操作の3つ目に存在する場合大きな値にゆとり分を追加した幅を返す() {
        Text shortClassName = new Text( "a" );
        List< Text > attributionsAreNotWideWidthFromDefault = Arrays.asList( new Text( "short" ), new Text( "attribution" ) );
        List< Double > operationsAreNotWideWidthFromDefault = Arrays.asList( 5.0, 200.0 );

        double actual = obj.calculateMaxWidth( shortClassName, attributionsAreNotWideWidthFromDefault, operationsAreNotWideWidthFromDefault );

        assertThat( actual, is( operationsAreNotWideWidthFromDefault.get( 0 ) + obj.getClassNameSpace() ) );
    }

    @Test
    public void デフォルトのクラスの幅より大きい数値が存在しない場合デフォルト幅を返す() {
        Text shortClassName = new Text( "a" );
        List< Text > attributionsAreNotWideWidthFromDefault = Arrays.asList( new Text( "short" ), new Text( "attribution" ) );
        List< Double > operationsAreNotWideWidthFromDefault = Arrays.asList( 5.0, 25.0 );

        double actual = obj.calculateMaxWidth( shortClassName, attributionsAreNotWideWidthFromDefault, operationsAreNotWideWidthFromDefault );

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

        assertThat( actual, is( 20.0 * 2 ) );

        operationsAreMoreTwo.add( "operation3" );

        actual = obj.calculateMaxOperationHeight( operationsAreMoreTwo );

        assertThat( actual, is( 20.0 * 3 ) );

        operationsAreMoreTwo.add( "operation4" );

        actual = obj.calculateMaxOperationHeight( operationsAreMoreTwo );

        assertThat( actual, is( 20.0 * 4 ) );

        operationsAreMoreTwo.add( "operation5" );

        actual = obj.calculateMaxOperationHeight( operationsAreMoreTwo );

        assertThat( actual, is( 20.0 * 5 ) );
    }

    @Test
    public void クラスの位置に存在するかどうかを判定する() {
        double clickedX = 100;
        double clickedY = 200;
        obj.createNodeText( ContentType.Title, "ClassName" );
        obj.setMouseCoordinates( clickedX, clickedY );
        obj.calculateWidthAndHeight( 100 );

        boolean actual = obj.isAlreadyDrawnNode( clickedX, clickedY );

        assertThat( actual, is( true ) );
    }

    @Test
    public void クラス名を取得する() {
        String className = "ClassName";
        obj.createNodeText( ContentType.Title, className );

        String actual = obj.getNodeText();

        assertThat( actual, is( className ) );
    }
}