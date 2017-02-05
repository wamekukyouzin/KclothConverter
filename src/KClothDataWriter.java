import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by wamek on 17/2/5.
 */

class KClothDataWriter extends Thread {
    private Path path;
    private ArrayList<ClothData> clothDataList = new ArrayList<>();
    KClothDataWriter(Path path) {
        this.path = path;
    }

    @Override
    public void run() {
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(line -> Optional.ofNullable(createClothData(line))
                    .ifPresent(clothDataList::add));
        } catch (IOException e) {
            e.printStackTrace();
        }

        clothDataList.forEach(clothData -> {
            File file = new File(path.getParent().toString() + "/" + clothData.getTypeName());
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(path.getParent().toString() + "/" + clothData.toFileName());
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(0xef);
                fos.write(0xbb);
                fos.write(0xbf);
                PrintWriter printWriter = new PrintWriter((new OutputStreamWriter(fos)));
                printWriter.write(clothData.toERB());
                printWriter.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
//        try (FileOutputStream fos = new FileOutputStream(((new File(path.getParent.toString() + )))))
//        try (FileOutputStream fos = new FileOutputStream((new File(path.getParent().toString() + "/REGISTER_CLOTH.ERB")))) {
//            fos.write(0xef);
//            fos.write(0xbb);
//            fos.write(0xbf);
//            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(fos));
//            clothDataList.forEach(clothData -> {
//                printWriter.write(clothData.toRegister());
//                File file = new File(path.getParent().toString() + "/" + clothData.getTypeName());
//                if (!file.exists()) {
//                    file.mkdir();
//                }
//                file = new File(path.getParent().toString() + "/" + clothData.toFileName());
//                try (FileOutputStream cfos = new FileOutputStream(file)) {
//                    cfos.write(0xef);
//                    cfos.write(0xbb);
//                    cfos.write(0xbf);
//                    PrintWriter cprintWriter = new PrintWriter(new OutputStreamWriter(cfos));
//                    cprintWriter.write(clothData.toERB());
//                    cprintWriter.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private ClothData createClothData(String line) {
        String[] elements = line.split(",");
        try {
            ClothType clothType = ClothType.valueOf(elements[0]);
            int id = Integer.parseInt(elements[1]);
            String name = elements[2];
            return new ClothData(clothType, id, name);
        } catch (IllegalArgumentException e) {
            //タイプが異常、idが数字でない場合にnull返す
            return null;
        }
    };

}
