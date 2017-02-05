/**
 * Created by wamek on 17/2/5.
 */
public enum ClothType {
    HEAD("頭"),
    NECK("首"),
    TOP("上服"),
    BOTTOM("下服"),
    BODYSUIT("上下服"),
    OUTER("上着"),
    INNER_TOP("下着上"),
    INNER_BOTTOM("下着下"),
    INNER_BODYSUIT("上下下着"),
    GLOVE("腕"),
    SOCKS("靴下"),
    SHOES("靴"),
    ACCESSORY("アクセサリ");

    private String typeName;
    private ClothType(String typeName) {
        this.typeName = typeName;
    }
    String getTypeName() {
        return typeName;
    }

    public String getConstName() {
        return "衣装部位_" + typeName;
    }
}