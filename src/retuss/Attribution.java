package retuss;

/**
 * <p> クラスの属性に関するクラス </p>
 *
 * <p>
 *     このクラスは、クラス図におけるクラスの属性に関するクラスです。
 *     {@link ClassData} クラスを継承しています。
 * </p>
 */
public class Attribution extends ClassData {

    Attribution() {
        name = "";
        visibility = "";
        type = ContentType.Undefined;
        isIndicate = true;
    }

    Attribution( String name ) {
        this.name = name;
        isIndicate = true;
    }
}
