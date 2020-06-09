import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Arrays;


public class TopicFileViewer {

    public static void main(String[] args) {
        //to view what is being
        try {
            File file = new File("readFromTopicFile.dat");
            System.out.println(file.createNewFile());
            PrintWriter writer = new PrintWriter(file);

            RandomAccessFile randomAccessFile = new RandomAccessFile("data.dat", "r");
            long length = randomAccessFile.length();
            int a  = randomAccessFile.readInt();
            int b = randomAccessFile.readInt();
            System.out.println("a = " + a + ", and b = " + b);
            randomAccessFile.seek(0);
            while (randomAccessFile.getFilePointer()!=length){
                writer.println(randomAccessFile.readInt());
            }
            randomAccessFile.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
