package retuss;

/**
 * <p> クラスのデータに関するクラス </p>
 *
 * <p>
 *     このクラスは、クラス図におけるクラスのデータに関するクラスです。
 *     次の属性を持ちます。
 *     <ui>
 *         <li> 名前 </li>
 *         <li> 種類 </li>
 *         <li> 可視性 </li>
 *         <li> 表示しているか否か </li>
 *     </ui>
 * </p>
 */
public class ClassData {
    /**
     * データの名前
     */
    protected String name;

    /**
     * データの可視性
     */
    protected String visibility;

    /**
     * データの種類
     */
    protected ContentType type;

    /**
     * データを図中に表示しているか否か
     */
    protected boolean isIndicate;

    public void setName( String name ) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIndication( boolean isIndicate ) {
        this.isIndicate = isIndicate;
    }

    public boolean isIndicate() {
        return isIndicate;
    }

    public void setType( ContentType type ) {
        this.type = type;
    }

    public ContentType getType() {
        return type;
    }
}
