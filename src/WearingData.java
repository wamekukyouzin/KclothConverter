import java.util.HashMap;
import java.util.StringJoiner;
import java.util.TreeMap;

/**
 * Created by wamek on 17/2/5.
 */
public class WearingData {
    private int no;
    private TreeMap<ClothType, Integer> wearData = new TreeMap<>();
    private String crlf = System.getProperty("line.separator");

    WearingData(int no) {
        this.no = no;
    }

    public Integer put(ClothType clothType, Integer i) {
        return wearData.put(clothType, i);
    }

    @Override
    public String toString() {
        return String.valueOf(no);
    }

    public String toERB() {
        StringJoiner sj = new StringJoiner(crlf);
        sj.add("@CLOTH_INIT_" + no);
        sj.add("LOCAL:1 = NO_TO_CHARA(" + no + ")");
        sj.add("FOR LOCAL, 0, CLOTH_PART_NUM");
        sj.add("\tCLOTH:(LOCAL:1):LOCAL = 0");
        sj.add("NEXT");
        wearData.forEach((clothType, id) -> {
            sj.add("CALL SET_CLOTH(LOCAL:1, " + clothType.getConstName() +  ", " + id + ")");
        });
        sj.add("");
        return sj.toString();
    }
}

