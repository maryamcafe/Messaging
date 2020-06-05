package Producer;

import Broker.MessageBroker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Producer extends Thread{
    private MessageBroker messageBroker;
    private String topicName;
    private String producerName;
    private File producerFile;
    private Integer temp;
    static final Logger logger = LogManager.getLogger(Producer.class);



    Producer(MessageBroker messageBroker, String topicName, String producerName, File producerFile) {
        this.messageBroker = messageBroker;
        this.topicName = topicName;
        this.producerName = producerName;
        this.producerFile = producerFile;
        setName(producerName);
    }

    public void run() {
        logger.trace("Started to run.");
        try {
            Scanner scanner = new Scanner(producerFile);
            while(scanner.hasNext()) {
                temp = scanner.nextInt();
//                logger.info("This producer read: " + temp);
                put(temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void put(int value) {
        messageBroker.put(topicName, producerName, value);
    }
}
