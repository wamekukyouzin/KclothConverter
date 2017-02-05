import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

/**
 * Created by wamek on 17/2/5.
 */
public class KClothConverter {
    public static void main(String[] args) {
        //if (args.length != 2) System.exit(0);
        KClothConverter kcc = new KClothConverter();
        kcc.doIt(args);
    }

    private void doIt(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(new KClothDataWriter(Paths.get(args[0])));
        executor.submit(new KWearingDataWriter((Paths.get(args[1]))));
        //KWearingDataWriter kcdw = new KWearingDataWriter((Paths.get(args[0])));
        //kcdw.run();
        executor.shutdown();

    }

}
