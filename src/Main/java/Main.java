import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Arrays;

public class Main {
//    static final Logger logger = LogManager.getLogger(Main.class);
    volatile static RandomAccessFile randomAccessFile;
    public static void main(String[] args) {

        new Program(args).run();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}
