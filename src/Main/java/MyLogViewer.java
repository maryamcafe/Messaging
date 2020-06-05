import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;


public class MyLogViewer {

    static final Logger logger = LogManager.getLogger(MyLogViewer.class);
    public static void main(String[] args) {
//        logger.info("This is in the Main.main\n");
//        logger.error("Nothing especial.\n");
//        System.out.println(Thread.currentThread().getContextClassLoader().getResource("log4j2.xml"));
        //to view logs
        try {
            File file = new File("readFrom1.dat");
            System.out.println(file.createNewFile());
            FileWriter writer = new FileWriter(file);

            RandomAccessFile randomAccessFile = new RandomAccessFile("data.txt", "r");
            randomAccessFile.seek(0);
            long length = randomAccessFile.length();
            byte[] values = new byte[(int)length];
            randomAccessFile.readFully(values);
            writer.write(Arrays.deepToString(new byte[][]{values}));
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
