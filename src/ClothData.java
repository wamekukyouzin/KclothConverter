import java.util.StringJoiner;

/**
 * Created by wamek on 17/2/5.
 */
public class ClothData {
    private ClothType type;
    private int id;
    private String name;
    private String crlf = System.getProperty("line.separator");

    ClothData(ClothType type, int id, String name) {
        this.type = type;
        this.id = id;
        this.name = name;
    }

    public String getTypeName() {
        return type.toString();
    }

    public String toERB() {
        StringJoiner sj = new StringJoiner(crlf);
        sj.add("@CLOTH_NAME_" + type.toString() + "_" + id);
        sj.add("RESULTS = " + name);
        return sj.toString();
    }

    public String toFileName() {
        return type.toString() + "\\CLOTH_" + type.toString() + "_" + id + "_" + name + ".ERB";
    }

    public String toRegister() {
        return "CALL REGISTER_CLOTH(" + type.getConstName() + "," + id + ", " + name + ")" + crlf;
    }

    public String toString() {
        return name;
    }
}
