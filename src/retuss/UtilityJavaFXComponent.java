package retuss;

import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JavaFXコンポーネントにまつわる様々なユーティリティを提供する。
 */
public class UtilityJavaFXComponent {

    /**
     * 表示しているボタンの内、選択したボタンをtrueに、それ以外のボタンをfalseにする。
     *
     * @param buttonSetsToTrue trueにセットするボタン
     */
    public List< Button > setAllDefaultButtonIsFalseWithout( List< Button > buttons, Button buttonSetsToTrue ) {
        for( Button button : buttons ) {
            if( button.equals( buttonSetsToTrue ) ) {
                button.setDefaultButton( true );
            } else {
                button.setDefaultButton( false );
            }
        }

        return buttons;
    }

    /**
     * 選択されているボタンを返す。
     *
     * @param buttons ボタンリスト（1つだけがtrueになっている）
     */
    public Button getDefaultButtonIn( List< Button > buttons ) {
        int count = 0;

        for( int i = 0; i < buttons.size(); i++ ) {
            if( buttons.get( i ).isDefaultButton() ) {
                count = i;
                break;
            }
        }
        return buttons.get( count );
    }

    public ContextMenu getClassContextMenuInCD( String nodeName, ContentType nodeType ) {
        ContextMenu popup = new ContextMenu();

        if( nodeType == ContentType.Composition ) {
            popup.getItems().add( new MenuItem( ( nodeName ) + " の変更" ) );
            popup.getItems().add( new MenuItem( ( nodeName ) + " の削除" ) );
        }

        return popup;
    }

    public ContextMenu getClassContextMenuInCD( String nodeName, ContentType nodeType, List< String > nodeContents1, List< String > nodeContents2, List< Boolean > nodeContents3, List< Boolean > nodeContents4 ) {
        ContextMenu popup = new ContextMenu();

        if( nodeType == ContentType.Class ) {
            popup.getItems().add( new MenuItem( nodeName + "クラスの名前の変更") );
            popup.getItems().add( new MenuItem( nodeName + "クラスをモデルから削除" ) );
            popup.getItems().add( new SeparatorMenuItem() );

            Menu attributionMenu = new Menu( "属性" );
            Menu operationMenu = new Menu( "操作" );
            MenuItem addAttributionMenuItem = new MenuItem( "追加" );
            Menu changeAttributionMenu = new Menu( "変更" );
            Menu deleteAttributionMenu = new Menu( "削除" );
            Menu displayAttributionMenu = new Menu( "表示選択" );
            MenuItem addOperationMenuItem = new MenuItem( "追加" );
            Menu changeOperationMenu = new Menu( "変更" );
            Menu deleteOperationMenu = new Menu( "削除" );
            Menu displayOperationMenu = new Menu( "表示選択" );

            if( nodeContents1.size() > 0 ) {
                for( int i = 0; i < nodeContents1.size(); i++ ) {
                    changeAttributionMenu.getItems().add( new MenuItem( nodeContents1.get( i ) ) );
                    deleteAttributionMenu.getItems().add( new MenuItem( nodeContents1.get( i ) ) );
                    CheckMenuItem checkMenuItem = new CheckMenuItem( nodeContents1.get( i ) );
                    checkMenuItem.setSelected( nodeContents3.get( i ) );
                    displayAttributionMenu.getItems().add( checkMenuItem );
                }
            } else {
                MenuItem hasNotChangeAttributionMenuItem = new MenuItem( "なし" );
                MenuItem hasNotDeleteAttributionMenuItem = new MenuItem( "なし" );
                CheckMenuItem hasNotDisplayAttributionCheckMenuItem = new CheckMenuItem( "なし" );
                hasNotChangeAttributionMenuItem.setDisable( true );
                hasNotDeleteAttributionMenuItem.setDisable( true );
                hasNotDisplayAttributionCheckMenuItem.setDisable( true );
                changeAttributionMenu.getItems().add( hasNotChangeAttributionMenuItem );
                deleteAttributionMenu.getItems().add( hasNotDeleteAttributionMenuItem );
                displayAttributionMenu.getItems().add( hasNotDisplayAttributionCheckMenuItem );
            }

            if( nodeContents2.size() > 0 ) {
                for(int i = 0; i < nodeContents2.size(); i++ ) {
                    changeOperationMenu.getItems().add( new MenuItem( nodeContents2.get( i ) ) );
                    deleteOperationMenu.getItems().add( new MenuItem( nodeContents2.get( i ) ) );
                    CheckMenuItem checkMenuItem = new CheckMenuItem( nodeContents2.get( i ) );
                    checkMenuItem.setSelected( nodeContents4.get( i ) );
                    displayOperationMenu.getItems().add( checkMenuItem );
                }
            } else {
                MenuItem hasNotChangeOperationMenuItem = new MenuItem( "なし" );
                MenuItem hasNotDeleteOperationMenuItem = new MenuItem( "なし" );
                CheckMenuItem hasNotDisplayOperationCheckMenuItem = new CheckMenuItem( "なし" );
                hasNotChangeOperationMenuItem.setDisable( true );
                hasNotDeleteOperationMenuItem.setDisable( true );
                hasNotDisplayOperationCheckMenuItem.setDisable( true );
                changeOperationMenu.getItems().add( hasNotChangeOperationMenuItem );
                deleteOperationMenu.getItems().add( hasNotDeleteOperationMenuItem );
                displayOperationMenu.getItems().add( hasNotDisplayOperationCheckMenuItem );
            }

            attributionMenu.getItems().add( addAttributionMenuItem );
            attributionMenu.getItems().add( changeAttributionMenu );
            attributionMenu.getItems().add( deleteAttributionMenu );
            attributionMenu.getItems().add( displayAttributionMenu );
            operationMenu.getItems().add( addOperationMenuItem );
            operationMenu.getItems().add( changeOperationMenu );
            operationMenu.getItems().add( deleteOperationMenu );
            operationMenu.getItems().add( displayOperationMenu );

            popup.getItems().add( attributionMenu );
            popup.getItems().add( operationMenu );
        } else {
            //popup.getItems().add( new MenuItem( "モデルから削除" ) );
            //popup.getItems().add( new MenuItem( "内容の変更" ) );
        }

        return popup;
    }

    /**
     * <p> 任意の点が多角形の内部に存在しているか否かを確認する。 </p>
     *
     * <p>
     *     アルゴリズムはWinding Number Algorithmを利用している。
     *     また、多角形の点上に存在する点は多角形内部に存在するとして真を返す。
     * </p>
     *
     * <p>
     *     Winding Number Algorithmは、Crossing Number Algorithmの欠点である多角形の自己交差内における内外判定を克服したアルゴリズムである。
     *     多角形の自己交差とは、次のような多角形の点Aが存在する領域を示す。
     *
     *     <p>
     *         凡例<br>
     *         <ui>
     *             <li> - : 多角形の領域外（多角形が作る穴 == Holeは除く） </li>
     *             <li> O : 多角形の領域内（自己交差内は除く） </li>
     *             <li> X : 多角形が作る穴 == Hole（多角形の領域外） </li>
     *             <li> A : 多角形の自己交差内の領域（多角形の領域内） </li>
     *         </ui>
     *     </p>
     *     <p>
     *         多角形の自己交差の図形 <br>
     *         --- --- --- --- --- ---<br>
     *         --- --- OOO --- --- ---<br>
     *         --- OOO AAA OOO OOO ---<br>
     *         --- --- OOO XXX OOO ---<br>
     *         --- --- OOO OOO OOO ---<br>
     *         --- --- --- --- --- ---<br>
     *     </p>
     *
     *     この時、XXXは範囲外であるがAAAは範囲内であるという判定が一般的である。
     *     しかし、Crossing Number AlgorithmではAAAを範囲外として判定してしまう。
     *     Winding Number AlgorithmではAAAを範囲内として判定する。
     * </p>
     *
     * <p>
     *     また、Winding Number Algorithmは本来であれば逆三角関数を使うため計算コストの点で効果であるが、このメソッドでは単純な座標軸の正負の向きによりカウント変数を増減することで判定する。
     *     そのため、Crossing Number Algorithmとほぼ変わらない計算コストにより判定を行う。
     * </p>
     *
     * @param targetPolygon 多角形の各点のXY軸のポイントのリスト
     * @param targetPoint 多角形の内部に存在するか否かを確認する対象のポイント
     * @return 任意の点が多角形の内部に存在しているか否か 要素が3未満の場合はfalseを返す。
     */
    public boolean isInsidePointFromPolygonUsingWNA( List< Point2D > targetPolygon, Point2D targetPoint ) {
        boolean isInsidePoint = false;
        int wn = 0;
        List< Point2D > polygon = new ArrayList<>();

        // 多角形でない場合
        if( targetPolygon.size() < 3 ) return false;

        for( Point2D point : targetPolygon ) {
            polygon.add( point );
            if( point.equals( targetPoint ) ) return true; // ターゲット点が多角形の点と等しい場合
        }

        // 多角形における最後の点と最初の点が異なる場合（例えば四角形ABCDの点配列は ABCDA でなければならないため）
        if( ! targetPolygon.get( 0 ).equals( targetPolygon.get( targetPolygon.size() - 1 ) ) ) polygon.add( targetPolygon.get( 0 ) );

        /*
         * ルール1.　上向きの辺は、開始点を含み終点を含まない。
         * ルール2.　下向きの辺は、開始点を含まず終点を含む。
         * ルール3.　水平線Rと辺Snが水平でない (SnがRと重ならない) 。
         * ルール4.　水平線Rと辺Snの交点は厳密に点Pの右側になくてはならない。
         */
        for( int i = 1; i < polygon.size(); i++ ) {
            // 上向きの辺、下向きの辺によって処理が分かれる。
            // 上向きの辺。点Pがy軸方向について、始点と終点の間にある。ただし、終点は含まない。(ルール1)
            if( ( polygon.get( i - 1 ).getY() <= targetPoint.getY() ) && ( polygon.get( i ).getY() > targetPoint.getY() ) ) {
                // 辺は点pよりも右側にある。ただし、重ならない。(ルール4)
                // 辺が点pと同じ高さになる位置を特定し、その時のxの値と点pのxの値を比較する。
                double vt = ( targetPoint.getY() - polygon.get( i - 1 ).getY() ) / ( polygon.get( i ).getY() - polygon.get( i - 1 ).getY());

                if( targetPoint.getX() < ( polygon.get( i - 1 ).getX() + ( vt * ( polygon.get( i ).getX() - polygon.get( i - 1 ).getX() ) ) ) ) {
                    ++wn;  // ここが重要。上向きの辺と交差した場合は +1
                }
            }
            // 下向きの辺。点Pがy軸方向について、始点と終点の間にある。ただし、始点は含まない。(ルール2)
            else if( ( polygon.get( i - 1 ).getY() > targetPoint.getY() ) && ( polygon.get( i ).getY() <= targetPoint.getY() ) ) {
                // 辺は点pよりも右側にある。ただし、重ならない。(ルール4)
                // 辺が点pと同じ高さになる位置を特定し、その時のxの値と点pのxの値を比較する。
                double vt = ( targetPoint.getY() - polygon.get( i - 1 ).getY() ) / ( polygon.get( i ).getY() - polygon.get( i - 1 ).getY() );

                if( targetPoint.getX() < ( polygon.get( i - 1 ).getX() + ( vt * ( polygon.get( i ).getX() - polygon.get( i - 1 ).getX() ) ) ) ) {
                    --wn;  // ここが重要。下向きの辺と交差した場合は -1
                }
            }
            // ルール1,ルール2を確認することで、ルール3も確認できている。
        }

        if( wn != 0 ) isInsidePoint = true;

        return isInsidePoint;
    }

    /**
     * <p> 任意の点が多角形の内部に存在しているか否かを確認する。 </p>
     *
     * <p>
     *     内側の定義やアルゴリズムは{@link java.awt.Polygon#contains(int, int)}に依存する。
     *     また、多角形の境界線上に存在する点においては別のアルゴリズムを用いて存在しているとして真を返す。
     * </p>
     *
     * @param polygonPoints 多角形の各点のXY軸のポイントのリスト
     * @param targetPoint 多角形の内部に存在するか否かを確認する対象のポイント
     * @return 任意の点が多角形の内部に存在しているか否か 要素が3未満の場合はfalseを返す。
     *
     * @deprecated
     * <p>
     *     現在このメソッドは、次の点により使用を推奨しない。
     *     <ui>
     *         <li> {@link Polygon} クラスのcontainsメソッドのアルゴリズムが不明瞭である点（特に {@link java.awt.Shape} に記述している判定計算の負荷が非常に大きい場合はfalseを返す恐れがあるとの解釈が不安材料） </li>
     *         <li> {@link Polygon#contains(int, int)} の引数が {@code int} である点（ {@link javafx.geometry.Point2D} のXY軸のポイントはどちらも {@code double} 型） </li>
     *         <li> {@link javafx.scene.shape.Polygon} クラスではなく {@link java.awt.Polygon} を使っており名称被りを起こす可能性がある点（もしこのクラス内で {@link javafx.scene.shape.Polygon} を用いることがあればこのメソッドを削除してよい） </li>
     *         <li> 多角形の自己交差内のポイントを多角形外に存在すると判定する点（Crossing Number Algorithmを用いていると予測するが正確なアルゴリズムは不明） </li>
     *     </ui>
     *     今後は {@link #isInsidePointFromPolygonUsingWNA(List, Point2D)} の利用を推奨する。
     * </p>
     */
    @Deprecated
    public boolean isInsidePointFromPolygonUsingJavaAwtPolygonMethod( List< Point2D > polygonPoints, Point2D targetPoint ) {
        boolean isInside;
        boolean isOnLine = false;
        boolean isOnEdge = false;

        // 多角形でない場合
        if( polygonPoints.size() < 3 ) return false;

        Polygon polygon = new Polygon();
        for( Point2D point : polygonPoints ) {
            polygon.addPoint( ( int ) point.getX(), ( int ) point.getY() );
        }

        for( int i = 1; i < polygonPoints.size(); i++ ) {
            Point2D startPoint = polygonPoints.get( i - 1 );
            Point2D endPoint = polygonPoints.get( i );
            if( startPoint.getX() > endPoint.getX() ) {
                Point2D tmp = startPoint;
                startPoint = endPoint;
                endPoint = tmp;
            }
            if( startPoint.equals( targetPoint ) || endPoint.equals( targetPoint ) ) isOnEdge = true; // ターゲット点が多角形の点と等しい場合

            isOnLine = startPoint.getX() <= targetPoint.getX() && targetPoint.getX() <= endPoint.getX() &&
                    ( ( startPoint.getY() <= endPoint.getY() && startPoint.getY() <= targetPoint.getY() && targetPoint.getY() <= endPoint.getY() ) ||
                            ( startPoint.getY() > endPoint.getY() && endPoint.getY() <= targetPoint.getY() && targetPoint.getY() <= startPoint.getY() ) )
                    && ( targetPoint.getY() - startPoint.getY() ) * ( endPoint.getX() - startPoint.getX() ) == ( endPoint.getY() - startPoint.getY() ) * ( targetPoint.getX() - startPoint.getX() );

            if( isOnLine ) break;
            if( isOnEdge ) break;
        }

        if( isOnLine || isOnEdge ) {
            isInside = true;
        } else {
            isInside = polygon.contains( targetPoint.getX(), targetPoint.getY() );
        }

        return isInside;
    }
}
