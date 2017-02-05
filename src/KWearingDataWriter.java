import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Created by wamek on 17/2/5.
 */
public class KWearingDataWriter extends Thread {
    Path path;

    HashMap<Integer, ClothType> columnNumberMap = new HashMap<>();
    ArrayList<String> rowList = new ArrayList<>();
    ArrayList<ClothType> headerList = new ArrayList<>();
    ArrayList<WearingData> wearList = new ArrayList<>();

    KWearingDataWriter(Path path) {
        this.path = path;
    }

    @Override
    public void run() {
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(rowList::add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //ヘッダ行の格納
        rowList.stream().findFirst().ifPresent(csv -> {
            String[] columns = csv.split(",");
            Arrays.stream(columns)
                    .forEach(column -> Optional.ofNullable(getClothType(column))
                            .ifPresent(headerList::add));
        });
        headerList.forEach(System.out::println);
        //各行データからwearingdata作成
        rowList.stream()
                .skip(1)
                .forEach(aRow -> {
                    String[] columns = aRow.split(",");
                    Arrays.stream(columns)
                            .findFirst()
                            .ifPresent(noStr -> Optional.ofNullable(this.nullableParseInt(noStr))
                                    .ifPresent(no -> {
                                        WearingData wearingData = new WearingData(no);
                                        final AtomicInteger successCount = new AtomicInteger(0);
                                        Arrays.stream(columns)
                                                .skip(1)
                                                .forEach(column -> Optional.ofNullable(nullableParseInt(column))
                                                        .ifPresent(id -> {
                                                            try {
                                                                ClothType clothType = headerList.get(successCount.getAndIncrement());
                                                                wearingData.put(clothType, id);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }));
                                        wearList.add(wearingData);
                                    }));
                });
        try (FileOutputStream fos = new FileOutputStream((new File(path.getParent().toString() + "/INIT_CLOTH.ERB")))) {
            fos.write(0xef);
            fos.write(0xbb);
            fos.write(0xbf);
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(fos));
            wearList.forEach(wearingData -> printWriter.write(wearingData.toERB()));
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClothType getClothType(String name) {
        try {
            return ClothType.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Integer nullableParseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
